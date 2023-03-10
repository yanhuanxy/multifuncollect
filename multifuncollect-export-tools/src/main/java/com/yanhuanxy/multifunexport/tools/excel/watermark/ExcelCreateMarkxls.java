package com.yanhuanxy.multifunexport.tools.excel.watermark;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

/**
 * 用于添加透明水印
 * 1、支持自己定义水印样式
 * 2、支持实体excel文件插入水印
 * 3、支持动态创建excel插入水印
 * @author yym
 * @date 2021/6/8
 */
public class ExcelCreateMarkxls {
    private static final Logger logger = LoggerFactory.getLogger(ExcelCreateMarkxls.class);

    private final static Integer MAX_ROW = 65535;
    private final static String SHEET_DEFAULT = "sheet0";

    /** 添加单sheet数据 */
    private List<Object[]> list;
    private String[] columnLabel;
    private String sheetName = SHEET_DEFAULT;
    private BufferedImage waterMarkImg;
    private int waterMarkWidth;
    private int waterMarkHeight;
    private String userCode;

    public List<Object[]> getList() {
        return list;
    }

    public void setList(List<Object[]> list) {
        this.list = list;
    }

    public String[] getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String[] columnLabel) {
        this.columnLabel = columnLabel;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetname) {
        this.sheetName = sheetname;
    }

    public ExcelCreateMarkxls(BufferedImage waterMarkImg, int waterMarkWidth, int waterMarkHeight, String userCode) {
        this.waterMarkImg = waterMarkImg;
        this.waterMarkWidth = waterMarkWidth;
        this.waterMarkHeight = waterMarkHeight;
        this.userCode = userCode;
    }

    /**
     *根据文件地址对文件添加水印
     * @param fileName 文件名称
     * @throws IOException
     */
    public void process(String fileName) throws IOException{
        File file = new File(fileName);
        process(file);
    }

    /**
     *根据文件对象对文件添加水印
     * @param file 文件对象
     * @throws IOException
     */
    public void process(File file) throws IOException{
        ReaderInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new ReaderInputStream(new FileReader(file));
            fileOutputStream = new FileOutputStream(file);
            process(fileInputStream,fileOutputStream);
        }finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }
    }

    /**
     * 添加sheet 数据
     * @param sheet excl2003 poi对象
     */
    private void setHssfWorkbooksData(HSSFSheet sheet){
        HSSFRow row0 = sheet.getRow(0);
        if(row0 == null){
            row0 = sheet.createRow(0);
        }
        for(int i=0;i<columnLabel.length;i++){
            HSSFCell cell = row0.getCell(i);
            if(cell == null){
                cell = row0.createCell(i);
            }
            cell.setCellValue(columnLabel[i]);
        }
        for(int i=0;i < list.size();i++){
            HSSFRow row = sheet.getRow(1+i);
            if(row == null){
                row = sheet.createRow(1+i);
            }
            Object[] objArray = list.get(i);
            for(int j=0;j<objArray.length;j++){
                HSSFCell cell = row.getCell(j);
                if(cell == null){
                    cell = row.createCell(j);
                }
                cell.setCellValue(objArray[j] == null ? "" :objArray[j].toString());
            }
        }
    }

    /**
     * 是否需要添加数据
     * @return
     */
    private boolean isAddSheetData(){
        return list != null || columnLabel != null;
    }

    /**
     * 根据文件流对文件添加水印
     * 存在实体文件时调用
     * @param inputStream 文件流
     */
    public void process(InputStream inputStream,OutputStream outputStream){
        HSSFWorkbook workbook = null;
        try {
            boolean addSheetData = this.isAddSheetData();
            if(addSheetData){
                HSSFSheet sheet = null;
                if(inputStream == null){
                    workbook = new HSSFWorkbook();
                    sheet = workbook.createSheet(sheetName);
                }else{
                    int defsheetindex = 0;
                    workbook = new HSSFWorkbook(inputStream);
                    if(!sheetName.equals(SHEET_DEFAULT)){
                        workbook.setSheetName(defsheetindex,sheetName);
                    }
                    sheet = workbook.getSheetAt(defsheetindex);
                }
                this.setHssfWorkbooksData(sheet);
            }else{
                workbook = new HSSFWorkbook(inputStream);
            }
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }
                this.putWaterRemarkToExcelAllRow(workbook,sheet);
            }
            workbook.setForceFormulaRecalculation(true);

            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error("设置excel水印异常！", e);
        } finally {
            if(workbook != null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error("excel workbook 关闭IO异常！", e);
                }
            }
        }
    }

    /**
     * 在 HSSFWorkbook 的时候调用
     * 生成导出文件
     * @param workbook
     * @param sheet
     * @throws IOException
     */
    public void initExportFileToWb(HSSFWorkbook workbook, HSSFSheet sheet) {
        try {
            this.putWaterRemarkToExcelAllRow(workbook,sheet);
        } catch (IOException e) {
            logger.error("设置excel水印异常！", e);
        }
    }

    /**
     * 为Excel打上水印工具函数
     * 请自行确保参数值，以保证水印图片之间不会覆盖。
     * 在计算水印的位置的时候，并没有考虑到单元格合并的情况，请注意
     * waterRemarkPath  水印地址，classPath，目前只支持png格式的图片，
     *                  因为非png格式的图片打到Excel上后可能会有图片变红的问题，且不容易做出透明效果。
     *                  同时请注意传入的地址格式，应该为类似："\\excelTemplate\\test.png"
     * waterMarkWidth 水印图片宽度为多少列
     * waterMarkHeight 水印图片高度为多少行
     * @param wb       Excel Workbook
     * @param sheet    需要打水印的Excel
     * @throws IOException
     */
    private void putWaterRemarkToExcel(HSSFWorkbook wb, HSSFSheet sheet) throws IOException{
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        if(null == waterMarkImg) {
            throw new RuntimeException("向Excel上面打印水印，读取水印图片失败(2)。");
        }
        sheet.protectSheet(userCode);
        ImageIO.write(waterMarkImg,"png",byteArrayOut);
        Drawing drawing = sheet.createDrawingPatriarch();
        /*
         * 参数定义：
         * 第一个参数是（x轴的开始节点）；
         * 第二个参数是（是y轴的开始节点）；
         * 第三个参数是（是x轴的结束节点）；
         * 第四个参数是（是y轴的结束节点）；
         * 第五个参数是（是从Excel的第几列开始插入图片，从0开始计数）；
         * 第六个参数是（是从excel的第几行开始插入图片，从0开始计数）；
         * 第七个参数是（图片宽度，共多少列）；
         * 第8个参数是（图片高度，共多少行）；
         */
        ClientAnchor anchor = drawing.createAnchor(0, 0, 255, 255, 0, 0, waterMarkWidth, waterMarkHeight);
        Picture pic = drawing.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG));
        pic.resize();
    }

    private void putWaterRemarkToExcelAllRow(HSSFWorkbook wb, HSSFSheet sheet) throws IOException{
        if(null == waterMarkImg) {
            throw new RuntimeException("向Excel上面打印水印，读取水印图片失败(2)。");
        }
        sheet.protectSheet(userCode);
        int firstRowNum = 0;
        int lastRowNum = sheet.getLastRowNum();
        HSSFRow firstHssfRow = sheet.getRow(firstRowNum);
        if(ObjectUtils.isEmpty(firstHssfRow)){
            return;
        }
        short lastCellNum = firstHssfRow.getLastCellNum();

        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(waterMarkImg,"png",byteArrayOut);

        for(int j=0; j<lastRowNum; j++){
            boolean islastrow = (j == (lastRowNum -1));
            if(j % 50 == 0 || islastrow){
                int firstrow = j>= 50 ? j : 0;
                int lastrow = islastrow ? j : (firstrow + 50) > MAX_ROW ? MAX_ROW : firstrow + 50;
                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 250, (short) 0, firstrow, (short) lastCellNum, lastrow);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
                patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
            }
        }
    }

}
