package com.yanhuanxy.multifunexport.tools.excel.read;

import com.yanhuanxy.multifunexport.tools.domain.emuns.excel.CellDataEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 *  * poi 事件模式读取excel 2007
 *  * @author yym
 *  * @date 20190916 18:07
 */
public class BatchReaderExcelxlsx extends DefaultHandler implements IBatchReaderExcel {
    private static final Logger logger = LoggerFactory.getLogger(BatchReaderExcelxlsx.class);
    // boolean 类型列
    private final static String CELL_TYPE_B = "b";
    // 新的单元格
    private final static String CELL_TYPE_C = "c";
    // 错误类型列
    private final static String CELL_TYPE_E = "e";
    private final static String CELL_TYPE_R = "r";
    private final static String CELL_TYPE_S = "s";
    private final static String CELL_TYPE_T = "t";
    private final static String CELL_TYPE_V = "v";
    private final static String CELL_TYPE_X = "x";
    private final static String CELL_TYPE_INLINESTR = "inlineStr";
    private final static String CELL_TYPE_STR = "str";
    // 行最后标识
    private final static String LABEL_STR = "row";
    private final static String EXCEL_DATEFORMAT = "m/d/yy";

    /**
     * 匹配数值
     */
    private final static Pattern ISNUMBER = Pattern.compile("^-?[0-9]\\d*$");
    /**
     * 共享字符串表
     */
    private SharedStringsTable sst;

    /**
     * 上一次的内容
     */
    private String lastContents;

    /**
     * 字符串标识
     */
    private boolean nextIsString;

    /**
     * 工作表索引
     */
    private int sheetIndex = -1;

    /**
     * 行集合
     */
    private final List<String> rowlist = new ArrayList<String>();

    /**
     * 当前行
     */
    private int curRow = 0;

    /**
     * 当前列
     */
    private int curCol = 0;

    /**
     * T元素标识
     */
    private boolean isTelement;

    /**
     * 标记单元格的值
     */
    private String isCellValue;

    private boolean isCellStartEndFlag;
    /**
     * 异常信息，如果为空则表示没有异常
     */
    private String exceptionMessage;

    /**
     * 单元格数据类型，默认为字符串类型
     */
    private CellDataEnums nextDataType = CellDataEnums.SSTINDEX;

    private final DataFormatter formatter = new DataFormatter();

    private short formatIndex;

    private String formatString;

    /**
     * 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
     */
    private String preRef = null, ref = null;

    /**
     * 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
     */
    private String maxRef = null;

    /**
     * 最小从A 开始
     */
    private String minRef = "A1";
    /**
     * 单元格
     */
    private StylesTable stylesTable;

    private IExcelRowReader rowReader;

    @Override
    public void setRowReader(IExcelRowReader rowReader) {
        this.rowReader = rowReader;
    }

    /**
     * 处理一个sheet
     *
     * @param filename
     * @throws Exception
     */
    public void processOneSheet(String filename) throws Exception {
        InputStream sheet2 = null;
        OPCPackage pkg = null;
        try {
            pkg = OPCPackage.open(filename);
            XSSFReader r = new XSSFReader(pkg);
            stylesTable = r.getStylesTable();
//            SharedStringsTable sst = r.getSharedStringsTable();
            r.setUseReadOnlySharedStringsTable(true);
            stylesTable = r.getStylesTable();
            SharedStringsTable sst = (SharedStringsTable) r.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            sheet2 = r.getSheet("rId1");
            InputSource sheetSource = new InputSource(sheet2);
            parser.parse(sheetSource);
        } catch (Exception e) {
            logger.error("事件模式解析excel2007+时读取sheet页异常！", e);
        } finally {
            if (pkg != null) {
                pkg.close();
            }
            if (sheet2 != null) {
                sheet2.close();
            }
        }
    }

    /**
     * 获取所有 表格
     *
     * @param filename
     * @throws IOException
     * @throws OpenXML4JException
     * @throws SAXException
     */
    @Override
    public void process(String filename) throws IOException {
        try(FileInputStream fileInputStream= new FileInputStream(new File(filename))) {
            process(fileInputStream);
        }
    }

    /**
     * 获取所有 表格
     *
     * @param file
     * @throws IOException
     * @throws OpenXML4JException
     * @throws SAXException
     */
    @Override
    public void process(File file) throws IOException {
        try(FileInputStream fileInputStream= new FileInputStream(file)){
            process(fileInputStream);
        }
    }

    /**
     * 获取所有 表格
     *
     * @param inputStream
     * @throws IOException
     * @throws OpenXML4JException
     * @throws SAXException
     */
    @Override
    public void process(InputStream inputStream) throws IOException{
        OPCPackage pkg = null;
        InputStream sheet = null;
        try {
            pkg = OPCPackage.open(inputStream);
            XSSFReader xssfReader = new XSSFReader(pkg);
            xssfReader.setUseReadOnlySharedStringsTable(true);
            stylesTable = xssfReader.getStylesTable();
            SharedStringsTable sst = (SharedStringsTable) xssfReader.getSharedStringsTable();
            XMLReader parser = fetchSheetParser(sst);
            Iterator<InputStream> sheets = xssfReader.getSheetsData();
            while (sheets.hasNext()) {
                curRow = 0;
                sheetIndex++;
                sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
            }
        } catch (OpenXML4JException | SAXException e){
            logger.error("excel xlsx 读取异常", e);
            throw new IOException(e.getMessage());
        } catch (Exception e) {
            logger.error("事件模式读取excel2007+时文件解析异常！", e);
        } finally {
            if (pkg != null) {
                pkg.close();
            }
            if (sheet != null) {
                sheet.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    }


    /**
     * 解析文档的规则
     *
     * @param sst
     * @return
     * @throws SAXException
     */
    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        //org.apache.xerces.parsers.SAXParser
//        XMLReader parser = XMLReaderFactory.createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser")

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);

        XMLReader parser = parserFactory.newSAXParser().getXMLReader();
        parser.setContentHandler(this);
        this.sst = sst;

        return parser;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        isCellStartEndFlag = true;
        // c => 单元格
        if (CELL_TYPE_C.equals(name)) {
            // 前一个单元格的位置
            if (preRef == null) {
                preRef = attributes.getValue(CELL_TYPE_R);
                int len = countNullCell(preRef, minRef);
                for (int i = 0; i <= len; i++) {
                    rowlist.add(curCol, "");
                    curCol++;
                }
            } else {
                preRef = ref;
            }
            // 当前单元格的位置
            ref = attributes.getValue(CELL_TYPE_R);
            // 设定单元格类型
            this.setNextDataType(attributes);

            String cellType = attributes.getValue(CELL_TYPE_T);
            if(cellType == null){
                nextIsString = true;
                isCellValue = CELL_TYPE_X;
            } else if (cellType != null && cellType.equals(CELL_TYPE_S)) {
                nextIsString = true;
                isCellValue = CELL_TYPE_S;
            } else {
                nextIsString = false;
                isCellValue = "";
            }
        }

        // 当元素为t时
        if (CELL_TYPE_T.equals(name)) {
            isTelement = true;
        } else {
            isTelement = false;
        }

        // 置空
        lastContents = "";
    }

    /**
     * 处理数据类型
     *
     * @param attributes
     */
    public void setNextDataType(Attributes attributes) {
        nextDataType = CellDataEnums.NUMBER;
        formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue(CELL_TYPE_T);
        String cellStyleStr = attributes.getValue(CELL_TYPE_S);
        String columData = attributes.getValue(CELL_TYPE_R);

        if (CELL_TYPE_B.equals(cellType)) {
            nextDataType = CellDataEnums.BOOL;
        } else if (CELL_TYPE_E.equals(cellType)) {
            nextDataType = CellDataEnums.ERROR;
        } else if (CELL_TYPE_INLINESTR.equals(cellType)) {
            nextDataType = CellDataEnums.INLINESTR;
        } else if (CELL_TYPE_S.equals(cellType)) {
            nextDataType = CellDataEnums.SSTINDEX;
        } else if (CELL_TYPE_STR.equals(cellType)) {
            nextDataType = CellDataEnums.FORMULA;
        }

        if (cellStyleStr != null) {
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();

            if (EXCEL_DATEFORMAT.equals(formatString)) {
                nextDataType = CellDataEnums.DATE;
                formatString = "yyyy-MM-dd hh:mm:ss.SSS";
            }

            if (formatString == null) {
                nextDataType = CellDataEnums.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }

    /**
     * 对解析出来的数据进行类型处理
     *
     * @param value   单元格的值（这时候是一串数字）
     * @param thisStr 一个空字符串
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getDataValue(String value, String thisStr) {
        String result;
        switch (nextDataType) {
            // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL:
                char first = value.charAt(0);
                result = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                result = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA:
                result = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                result = rtsi.getString();
                rtsi = null;
                break;
            case SSTINDEX:
                String sstIndex = value.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
//                    XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
//                    result = rtss.toString();
                    RichTextString itemAt = sst.getItemAt(idx);
                    result = itemAt.getString();
                    itemAt = null;
                } catch (Exception ex) {
                    result = value.toString();
                }
                break;
            case NUMBER:
                if (formatString != null) {
                    result = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                } else {
                    result = value;
                }
                if(result.contains("_")){
                    result = result.replace("_", "").trim();
                }
                break;
            case DATE:
                boolean number = ISNUMBER.matcher(value).find();
                if(number){
                    String tmpresult = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                    // 对日期字符串作特殊处理
                    result = tmpresult.replace(" ", "T");
                }else{
                    result = "";
                }
                break;
            default:
                result = " ";
                break;
        }

        return result;
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString){
            if(CELL_TYPE_S.equals(isCellValue) && StringUtils.isNotEmpty(lastContents) && StringUtils.isNumeric(lastContents)){
                int idx = Integer.parseInt(lastContents);
                lastContents = sst.getItemAt(idx).getString();
                nextIsString = false;
            }
            if(CELL_TYPE_C.equals(name) && CELL_TYPE_X.equals(isCellValue)){
                if(isCellStartEndFlag){
                    // 跨行之前 补充空单元格
                    if (!ref.equals(preRef)) {
                        addCellNullVal(ref, preRef);
                    }
                    rowlist.add(curCol, "");
                    curCol++;
                }
            }
        }
        isCellStartEndFlag = false;
        // t元素也包含字符串
        if (isTelement) {
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            isTelement = false;
        } else if (CELL_TYPE_V.equals(name)) {
            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            String value = this.getDataValue(lastContents.trim(), "");
            // 补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                addCellNullVal(ref, preRef);
            }
            rowlist.add(curCol, value);
            curCol++;
        } else {
            // 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (LABEL_STR.equals(name)) {
                // 默认第一行为表头，以该行单元格数目为最大数目
                if (curRow == 0) {
                    maxRef = ref;
                }
                // 补全一行尾部可能缺失的单元格
                if (maxRef != null && (ref != null || preRef != null)) {
                    String tmpcurRef = ref == null ? preRef : ref;
                    addCellNullVal(maxRef, tmpcurRef);
                }
                // 补全行数

                if(ref != null){
                    int tmpcurRow = Integer.parseInt(ref.replaceAll("[A-Z]+","")) -1;
                    if(curRow < tmpcurRow){
                        curRow = tmpcurRow;
                    }
                }
                rowReader.getRows(sheetIndex, curRow, rowlist);
                rowlist.clear();
                curRow++;
                curCol = 0;
                preRef = null;
                ref = null;
            }
        }
    }

    /**
     * // 补全单元格之间的空单元格
     */
    private void addCellNullVal(String refcel, String prefcel){
        int len = countNullCell(refcel, prefcel);
        for (int i = 0; i < len; i++) {
            rowlist.add(curCol, "");
            curCol++;
        }
    }

    /**
     * 计算两个单元格之间的单元格数目(同一行)
     *
     * @param ref
     * @param preRef
     * @return
     */
    public int countNullCell(String ref, String preRef) {
        // excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String preXfd = preRef.replaceAll("\\d+", "");

        xfd = fillChar(xfd, 3, '@', true);
        preXfd = fillChar(preXfd, 3, '@', true);

        char[] letter = xfd.toCharArray();
        char[] preLetter = preXfd.toCharArray();
        int res = (letter[0] - preLetter[0]) * 26 * 26
                + (letter[1] - preLetter[1]) * 26
                + (letter[2] - preLetter[2]);
        return res - 1;
    }

    /**
     * 字符串的填充
     *
     * @param str 字符串
     * @param len 长度
     * @param let 字符
     * @param isPre 是否转换
     * @return String
     */
    public String fillChar(String str, int len, char let, boolean isPre) {
        int tmplen = str.length();
        if (tmplen < len) {
            if (isPre) {
                for (int i = 0; i < (len - tmplen); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - tmplen); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到单元格内容的值
        lastContents += new String(ch, start, length);
    }

    /**
     * @return exceptionMessage
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
