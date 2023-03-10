package com.yanhuanxy.multifunexport.tools.excel.watermark;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
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
public class ExcelCreateMarkxlsx {
    private static final Logger logger = LoggerFactory.getLogger(ExcelCreateMarkxlsx.class);

    private final static String SHEET_DEFAULT = "sheet0";

    /** 添加单sheet数据 */
    private List<Object[]> list;
    private String[] columnLabel;
    private String sheetName;
    /** 获取有文字水印的透明色图片 */
    private BufferedImage waterMarkImg;
    private int waterMarkWidth;
    private int waterMarkHeight;

    public ExcelCreateMarkxlsx(BufferedImage waterMarkImg, int waterMarkWidth, int waterMarkHeight) {
        this.waterMarkImg = waterMarkImg;
        this.waterMarkWidth = waterMarkWidth;
        this.waterMarkHeight = waterMarkHeight;
    }

    public ExcelCreateMarkxlsx(BufferedImage waterMarkImg) {
        this.waterMarkImg = waterMarkImg;
    }

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

    /**
     * 根据文件地址对文件添加水印
     * @param fileName 文件地址
     * @throws IOException
     */
    public void process(String fileName,String outputFileName) throws IOException{
        File file = new File(fileName);
        File outputfile = new File(outputFileName);
        process(file,outputfile);
    }

    /**
     *根据文件对象对文件添加水印
     * @param file 文件对象
     * @throws IOException
     */
    public void process(File file,File outputfile) throws IOException{
        ReaderInputStream fileReadInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileReadInputStream = new ReaderInputStream(new FileReader(file));
            fileOutputStream = new FileOutputStream(outputfile);
            process(fileReadInputStream,fileOutputStream);
        }finally {
            if(fileReadInputStream != null){
                fileReadInputStream.close();
            }
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }
    }

    /**
     * 添加sheet 数据
     */
    private void setXssfWorkbooks(XSSFSheet sheet){
        XSSFRow row0 = sheet.getRow(0);
        if(row0 == null){
            row0 = sheet.createRow(0);
        }
        for(int i=0;i<columnLabel.length;i++){
            XSSFCell cell = row0.getCell(i);
            if(cell == null){
                cell = row0.createCell(i);
            }
            cell.setCellValue(columnLabel[i]);
        }
        for(int i=0;i<list.size();i++){
            XSSFRow row = sheet.getRow(1+i);
            if(row == null){
                row = sheet.createRow(1+i);
            }
            Object[] objArray = list.get(i);
            for(int j=0;j<objArray.length;j++){
                XSSFCell cell = row.getCell(j);
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
     *根据文件流对文件添加水印
     * @param inputStream 文件流
     */
    public void process(InputStream inputStream,OutputStream outputStream){
        XSSFWorkbook workbook = null;
        try {
            boolean addSheetData = this.isAddSheetData();
            if(addSheetData){
                XSSFSheet sheet;
                if(inputStream == null){
                    workbook = new XSSFWorkbook();
                    sheet = workbook.createSheet(sheetName);
                }else{
                    int defsheetindex = 0;
                    workbook = new XSSFWorkbook(inputStream);
                    if(!sheetName.equals(SHEET_DEFAULT)){
                        workbook.setSheetName(defsheetindex,sheetName);
                    }
                    sheet = workbook.getSheetAt(defsheetindex);
                }
                this.setXssfWorkbooks(sheet);
            }else{
                workbook = new XSSFWorkbook(inputStream);
            }
            for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }
                this.putWaterRemarkToExcel(workbook,sheet);
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
                    logger.warn("创建excel 水印 workbook 关闭异常！", e);
                }
            }
        }
    }


    /**
     * 在 XSSFWorkbook 的时候调用
     * 生成导出文件
     * @param workbook work
     * @param sheet 页
     * @throws IOException
     */
    public void initExportFileToWb(XSSFWorkbook workbook, XSSFSheet sheet) {
        try {
            this.putWaterRemarkToExcel(workbook,sheet);
        } catch (IOException e) {
            logger.error("设置excel水印异常！", e);
        }
    }

    /**
     * 为Excel打上水印工具函数
     * @param workbook Excel Workbook
     * @param sheet    需要打水印的Excel
     * @throws IOException
     */
    private void putWaterRemarkToExcel(XSSFWorkbook workbook, XSSFSheet sheet) throws IOException{

        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        if(null == waterMarkImg) {
            throw new RuntimeException("向Excel上面打印水印，读取水印图片失败(2)。");
        }
        ImageIO.write(waterMarkImg,"png",byteArrayOut);
        int pictureIdx = workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG);
        String rId = sheet.addRelation(null, XSSFRelation.IMAGES, workbook.getAllPictures().get(pictureIdx)).getRelationship().getId();
        sheet.getCTWorksheet().addNewPicture().setId(rId);
    }

}
