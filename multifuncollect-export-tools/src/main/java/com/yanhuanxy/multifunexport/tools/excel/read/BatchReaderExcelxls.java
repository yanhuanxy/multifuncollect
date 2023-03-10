package com.yanhuanxy.multifunexport.tools.excel.read;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *  * poi 事件模式读取excel 2003
 *  * @author yym
 *  * @date 20190916 18:07
 */
public class BatchReaderExcelxls implements IBatchReaderExcel,HSSFListener  {
    private static final Logger logger = LoggerFactory.getLogger(BatchReaderExcelxls.class);

    private int minColumns = -1;

    private POIFSFileSystem fs;

    private int lastRowNumber;

    private int lastColumnNumber;

    /** 输出公式 */
    private boolean outputFormulaValues = true;

    /** 公式转换 */
    private SheetRecordCollectingListener workbookBuildingListener;

    /** excel2003工作薄 */
    private HSSFWorkbook stubWorkbook;

    /** 记录 */
    private SSTRecord sstRecord;

    /** 监听 */
    private FormatTrackingHSSFListener formatListener;

    /** 表索引 */
    private int sheetIndex = -1;

    private BoundSheetRecord[] orderedBsrs;

    @SuppressWarnings("unchecked")
    private ArrayList boundSheetRecords = new ArrayList();

    private int nextRow;

    private int nextColumn;

    private boolean outputNextStringRecord;

    /** 当前行 */
    private int curRow = 0;

    /** 存储行记录的容器 */
    private List<String> rowlist = new ArrayList<String>();;

    @SuppressWarnings("unused")
    private String sheetName;

    private IExcelRowReader rowReader;

    @Override
    public void setRowReader(IExcelRowReader rowReader) {
        this.rowReader = rowReader;
    }

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    @Override
    public void process(String fileName) throws IOException {
        try(FileInputStream fileInputStream= new FileInputStream(new File(fileName))){
            process(fileInputStream);
        }
    }

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    @Override
    public void process(File file) throws IOException {
        try(FileInputStream fileInputStream= new FileInputStream(file)){
            process(fileInputStream);
        }
    }

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    @Override
    public void process(InputStream inputStream) throws IOException {
        try{
            this.fs = new POIFSFileSystem(inputStream);
            MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
            formatListener = new FormatTrackingHSSFListener(listener);
            HSSFEventFactory factory = new HSSFEventFactory();
            HSSFRequest request = new HSSFRequest();
            if (outputFormulaValues) {
                request.addListenerForAllRecords(formatListener);
            } else {
                workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
                request.addListenerForAllRecords(workbookBuildingListener);
            }
            factory.processWorkbookEvents(request, fs);
        }finally {
            if(inputStream!=null){
                inputStream.close();
            }
        }
    }

    @Override
    public void processRecord(Record record) {
        int thisRow = -1;
        int thisColumn = -1;
        String thisStr = null;
        String value = null;
        switch (record.getSid()) {
            case BoundSheetRecord.sid:
                boundSheetRecords.add(record);
                break;
            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    // 如果有需要，则建立子工作薄
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }

                    sheetIndex++;
                    if (orderedBsrs == null) {
                        orderedBsrs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    sheetName = orderedBsrs[sheetIndex].getSheetname();
                }
                break;

            case SSTRecord.sid:
                sstRecord = (SSTRecord) record;
                break;

            case BlankRecord.sid:
                BlankRecord brec = (BlankRecord) record;
                thisRow = brec.getRow();
                thisColumn = brec.getColumn();
                thisStr = "";
                rowlist.add(thisColumn, thisStr);
                break;
            case BoolErrRecord.sid:
                // 单元格为布尔类型
                BoolErrRecord berec = (BoolErrRecord) record;
                thisRow = berec.getRow();
                thisColumn = berec.getColumn();
                thisStr = berec.getBooleanValue() + "";
                rowlist.add(thisColumn, thisStr);
                break;
            case FormulaRecord.sid:
                // 单元格为公式类型
                FormulaRecord frec = (FormulaRecord) record;
                thisRow = frec.getRow();
                thisColumn = frec.getColumn();
                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        // 返回一个string 类型的公式
                        // 在下个单元格中获取值
                        outputNextStringRecord = true;
                        nextRow = frec.getRow();
                        nextColumn = frec.getColumn();
                    } else {
                        thisStr = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
                }
                rowlist.add(thisColumn, thisStr);
                break;
            case StringRecord.sid:
                // 单元格中公式的字符串
                if (outputNextStringRecord) {
                    StringRecord srec = (StringRecord) record;
                    thisStr = srec.getString();
                    thisRow = nextRow;
                    thisColumn = nextColumn;
                    outputNextStringRecord = false;
                }
                break;
            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) record;
                curRow = thisRow = lrec.getRow();
                thisColumn = lrec.getColumn();
                value = lrec.getValue().trim();
                value = checkValIsNull(value);
                this.rowlist.add(thisColumn, value);
                break;
            case LabelSSTRecord.sid:
                // 单元格为字符串类型
                LabelSSTRecord lsrec = (LabelSSTRecord) record;
                curRow = thisRow = lsrec.getRow();
                thisColumn = lsrec.getColumn();
                if (sstRecord == null) {
                    rowlist.add(thisColumn, " ");
                } else {
                    value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                    value = checkValIsNull(value);
                    rowlist.add(thisColumn, value);
                }
                break;
            case NumberRecord.sid:
                // 单元格为数字类型
                NumberRecord numrec = (NumberRecord) record;
                curRow = thisRow = numrec.getRow();
                thisColumn = numrec.getColumn();
                value = formatListener.formatNumberDateCell(numrec).trim();
                value = checkValIsNull(value);
                // 向容器加入列值
                rowlist.add(thisColumn, value);
                break;
            default:
                break;
        }

        // 遇到新行的操作
        if (thisRow != -1 && thisRow != lastRowNumber) {
            lastColumnNumber = -1;
        }

        // 空值的操作
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            curRow = thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            rowlist.add(thisColumn, " ");
        }

        // 更新行和列的值
        if (thisRow > -1) {
            lastRowNumber = thisRow;
        }
        if (thisColumn > -1) {
            lastColumnNumber = thisColumn;
        }
        // 行结束时的操作
        if (record instanceof LastCellOfRowDummyRecord) {
            if (minColumns > 0) {
                // 列值重新置空
                if (lastColumnNumber == -1) {
                    lastColumnNumber = 0;
                }
            }
            lastColumnNumber = -1;

            // 每行结束时， 调用getRows() 方法
            rowReader.getRows(sheetIndex, curRow, rowlist);
            // 清空容器
            rowlist.clear();
        }
    }

    /**
     * 校验空字符串时 返回 " "空格
     * @param tmpVal 值
     * @return string
     */
    private String checkValIsNull(String tmpVal){
        if("".equals(tmpVal)){
            return " ";
        }
        return tmpVal;
    }
}
