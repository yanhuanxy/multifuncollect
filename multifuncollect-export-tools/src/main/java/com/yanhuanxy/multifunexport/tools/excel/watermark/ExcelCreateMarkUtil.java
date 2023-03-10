package com.yanhuanxy.multifunexport.tools.excel.watermark;

import com.yanhuanxy.multifunexport.tools.domain.emuns.excel.ExcelTypeEnums;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于添加透明水印
 * 1、支持自己定义水印样式
 * 2、支持实体excel文件插入水印
 * 3、支持动态创建excel插入水印
 * 4、支持添加数据
 * 5、http文件流下载
 * @author yym
 * @date 2021/6/8
 */
public class ExcelCreateMarkUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelCreateMarkUtil.class);

    private final String fileName;

    private List list;
    private String[] columnLabel;
    private String sheetName;

    private OPCPackage aPackage;

    private final List<PackagePart> delPicture = new ArrayList<>();

    private BaseCreateWaterMarkImgImpl createWaterMarkImg;

    /**
     * excel 类型
     */
    private ExcelTypeEnums excelType;

    public ExcelCreateMarkUtil(String fileName){
        super();
        this.fileName = fileName;
    }

    public ExcelCreateMarkUtil(String fileName, List list, String[] columnLabel,String sheetName){
        this.list = list;
        this.fileName = fileName;
        this.columnLabel = columnLabel;
        this.sheetName = sheetName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setCreateWaterMarkImg(BaseCreateWaterMarkImgImpl createWaterMarkImg) {
        this.createWaterMarkImg = createWaterMarkImg;
    }

    /**
     * 为文件地址添加透明水印并通过http流写出
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param filePath 文件地址
     */
    public void exportExcelAddWaterMark(String filePath, HttpServletResponse response){
        try{
            File file = new File(filePath);
            this.exportExcelAddWaterMark(file,response);
        }catch (Exception e){
            logger.error("excel 水印工具添加水印异常！ exportExcelAddWaterMark [fileName: {}] ->{}", fileName, e.getMessage());
        }
    }

    /**
     * 为文件添加透明水印并通过http流写出
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param file 文件对象
     */
    public void exportExcelAddWaterMark(File file, HttpServletResponse response) throws IOException{
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        try {
            excelType = ExcelTypeEnums.checkFile(file, fileName);
            setResponseHeader(response,fileName);
            fileInputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            this.resetExcelCreateMark(fileInputStream, outputStream);
        } finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
            if(outputStream != null){
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    /**
     * 为excel文件流添加透明水印并通过http流写出
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param inputstream 输入流文件
     * @param response http返回对象
     */
    public void exportExcelAddWaterMark(InputStream inputstream, HttpServletResponse response) throws Exception{
        OutputStream outputStream = null;
        try {
            excelType = ExcelTypeEnums.checkFile(inputstream, fileName);
            setResponseHeader(response,fileName);
            outputStream = response.getOutputStream();
            this.resetExcelCreateMark(inputstream, outputStream);
        } finally {
            if(outputStream != null){
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    /**
     * 设置写入消息头
     * @param response http
     * @param fileName 文件名
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
    }

    /**
     * 通过文件地址添加透明水印
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param filePath 文件地址
     */
    public void readExcelAddWaterMark(String filePath, String outputFileName) {
        try{
            File file = new File(filePath);
            File outputFile = new File(outputFileName);
            this.readExcelAddWaterMark(file,outputFile);
        }catch (Exception e){
            logger.error("excel 水印工具添加水印异常！ readExcelAddWaterMark [fileName: {}] ->{}", fileName, e.getMessage());
        }
    }

    /**
     * 通过文件添加透明水印
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param file 文件对象
     */
    public void readExcelAddWaterMark(File file, File outputFile) throws Exception{
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            excelType = ExcelTypeEnums.checkFile(file, fileName);
            fileInputStream = new FileInputStream(file);
            fileOutputStream = new FileOutputStream(outputFile);
            this.resetExcelCreateMark(fileInputStream, fileOutputStream);
        } finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
        }
    }

    /**
     * 通过文件流添加透明水印
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param inputstream 文件流
     */
    public void readExcelAddWaterMark(InputStream inputstream, OutputStream outputStream) throws Exception{
        excelType = ExcelTypeEnums.checkFile(inputstream, fileName);
        this.resetExcelCreateMark(inputstream, outputStream);
    }


    /**
     * 通过网络文件流添加透明水印
     * excel2003L  处理excel2003文件
     * excel2007U  处理excel2007文件
     * @param multipartFile 网络文件对象
     */
    public void readExcelAddWaterMark(MultipartFile multipartFile, OutputStream outputStream) throws Exception{
        InputStream inputStream = null;
        try {
            excelType = ExcelTypeEnums.checkFile(multipartFile, fileName);
            inputStream = multipartFile.getInputStream();
            this.resetExcelCreateMark(inputStream, outputStream);
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }

    /**
     * 校验文件是否是excel类型文件 createWaterMarkImg 水印图片对象 水印为空时 调用默认的水印
     * @param inputStream 文件流
     */
    private void resetExcelCreateMark(InputStream inputStream, OutputStream outputStream){
        try{
            if(createWaterMarkImg == null){
                this.createWaterMarkImg = defaultBaseCreateWaterMark();
            }
            if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
                ExcelCreateMarkxls excelCreateMarkxls = resetExcelCreateMarkxls(createWaterMarkImg);
                excelCreateMarkxls.setSheetName(sheetName);
                excelCreateMarkxls.setColumnLabel(columnLabel);
                excelCreateMarkxls.setList(list);
                excelCreateMarkxls.process(inputStream,outputStream);
            } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
                ExcelCreateMarkxlsx excelCreateMarkxlsx = resetExcelCreateMarkxlsx(createWaterMarkImg);
                excelCreateMarkxlsx.setSheetName(sheetName);
                excelCreateMarkxlsx.setColumnLabel(columnLabel);
                excelCreateMarkxlsx.setList(list);
                excelCreateMarkxlsx.process(inputStream,outputStream);
            } else {
                throw new IOException("文件格式错误，fileName的扩展名只能是xls或xlsx。");
            }
        } catch (Exception e){
            logger.error("excel 水印工具添加水印异常！ resetExcelCreateMark [fileName: {}] ->{}", fileName, e.getMessage());
        }
    }

    /**
     * 创建添加excel 2003的水印对象
     * @param createWaterMarkImg 水印图片对象
     * @return ExcelCreateMarkxls
     */
    private ExcelCreateMarkxls resetExcelCreateMarkxls(BaseCreateWaterMarkImgImpl createWaterMarkImg){
        return new ExcelCreateMarkxls(createWaterMarkImg.getBufferedImage(),
                createWaterMarkImg.getWaterMarkWidth(),createWaterMarkImg.getWaterMarkHeight(),createWaterMarkImg.getUserCode());
    }

    /**
     *创建添加excel 2007的水印对象
     * @param createWaterMarkImg 水印图片对象
     * @return ExcelCreateMarkxlsx
     */
    private ExcelCreateMarkxlsx resetExcelCreateMarkxlsx(BaseCreateWaterMarkImgImpl createWaterMarkImg){
        return new ExcelCreateMarkxlsx(createWaterMarkImg.getBufferedImage(),
                createWaterMarkImg.getWaterMarkWidth(),createWaterMarkImg.getWaterMarkHeight());
    }

    /**
     *
     * @param workbook work
     * @param sheet sheet
     */
    public void setWaterMarkToExcel(ExcelTypeEnums excelType, Workbook workbook, Sheet sheet){
        try{
            if(createWaterMarkImg == null){
                this.createWaterMarkImg = defaultBaseCreateWaterMark();
            }

            if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
                HSSFWorkbook hssfWorkbook = (HSSFWorkbook) workbook;
                HSSFSheet hssfSheet = (HSSFSheet) sheet;
                HSSFPatriarch patriarch = hssfSheet.createDrawingPatriarch();
//                List<HSSFPictureData> allPictures = hssfWorkbook.getAllPictures();
//                List<HSSFShape> children = patriarch.getChildren();
//                HSSFShape hssfShape = children.get(0);
//                EscherOptRecord optRecord = hssfShape.getOptRecord();
//                HSSFPatriarch drawingPatriarch = hssfSheet.getDrawingPatriarch();
                if (patriarch == null) {
                    this.setWaterMarkToExcelXls(hssfWorkbook,hssfSheet, createWaterMarkImg);
                }
            }else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
                XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;
                XSSFSheet xssfSheet = (XSSFSheet) sheet;
                //是否存在水印
                if(xssfSheet.getCTWorksheet().isSetPicture()){
                    if(aPackage == null){
                        aPackage = xssfWorkbook.getPackage();
                    }
                    //获取旧的水印图片
                    CTWorksheet ctWorksheet = xssfSheet.getCTWorksheet();
                    CTSheetBackgroundPicture picture = ctWorksheet.getPicture();
                    POIXMLDocumentPart relationById = xssfSheet.getRelationById(picture.getId());
                    PackagePart packagePart = relationById.getPackagePart();
                    packagePart.setDeleted(true);
                    delPicture.add(packagePart);
                    // 解除水印
                    ctWorksheet.unsetPicture();
                }
                this.setWaterMarkToExcelXlsx(xssfWorkbook,xssfSheet, createWaterMarkImg);
            }else {
                throw new IOException("文件格式错误，fileName的扩展名只能是xls或xlsx。");
            }
        } catch (Exception e){
            logger.error("excel 水印工具添加水印异常！ setWaterMarkToExcel [fileName: {}] ->{}", fileName, e.getMessage());
        }
    }

    /**
     * 删除旧的水印图片
     */
    public void deleteOldWaterMark(){
        if(ExcelTypeEnums.EXCEL_2007U.equals(excelType) && !delPicture.isEmpty() && aPackage != null){
            delPicture.forEach(packagePart-> aPackage.removePart(packagePart));
        }
    }

    /**
     * poi excel 2003 类型
     * @param workbook work
     * @param sheet sheet
     */
    public void setWaterMarkToExcelXls(HSSFWorkbook workbook, HSSFSheet sheet, BaseCreateWaterMarkImgImpl createWaterMarkImg){
        ExcelCreateMarkxls excelCreateMarkxls = resetExcelCreateMarkxls(createWaterMarkImg);
        excelCreateMarkxls.initExportFileToWb(workbook, sheet);
    }

    /**
     * poi excel 2007+ 类型
     * @param workbook work
     * @param sheet seet
     */
    public void setWaterMarkToExcelXlsx(XSSFWorkbook workbook, XSSFSheet sheet, BaseCreateWaterMarkImgImpl createWaterMarkImg){
        ExcelCreateMarkxlsx excelCreateMarkxlsx = resetExcelCreateMarkxlsx(createWaterMarkImg);
        excelCreateMarkxlsx.initExportFileToWb(workbook, sheet);
    }

    /**
     * default 创建水印图片
     * @author yym
     * @date 2021/6/8
     */
    public abstract class BaseCreateWaterMarkImgImpl implements ICreateWaterMarkImg {
        private int waterMarkWidth = 254;
        private int waterMarkHeight = 254;
        private BufferedImage bufferedImage;
        private String userCode;

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public int getWaterMarkWidth() {
            return waterMarkWidth;
        }

        public void setWaterMarkWidth(int waterMarkWidth) {
            this.waterMarkWidth = waterMarkWidth;
        }

        public int getWaterMarkHeight() {
            return waterMarkHeight;
        }

        public void setWaterMarkHeight(int waterMarkHeight) {
            this.waterMarkHeight = waterMarkHeight;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public void setBufferedImage(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }

        public BaseCreateWaterMarkImgImpl(){
            int width = 900,height = 400;
            String waterText = "中国电信股份有限公司上海分公司-电信局";
            this.bufferedImage = this.getWaterImage(width,height,waterText);
        }

        public BaseCreateWaterMarkImgImpl(int width, int height, String waterText) {
            this.bufferedImage = this.getWaterImage(width,height,waterText);
        }

        public BaseCreateWaterMarkImgImpl(int width, int height, String waterText, Color color, Font font, Double degree, float alpha) {
            this.bufferedImage = this.getWaterImage(width,height,waterText,color,font,degree,alpha);
        }

        /**
         * 通过属性生成水印图片
         * @param width 图片宽度
         * @param height 图片高度
         * @param waterText 图片内容
         * @return BufferedImage
         */
        @Override
        public BufferedImage getWaterImage(int width, int height, String waterText){
            Color color = Color.BLACK;
            String fontType = "微软雅黑";
            int fontStyle = Font.PLAIN;
            int fontSize = 40;
            Font font = new Font(fontType,fontStyle,fontSize);
            Double degree = 15.000;
            float alpha = 0.2f;
            return waterMarkByText(width, height, waterText, color, font, degree, alpha);
        }

        /**
         * 自定以水印图片（纯手动版）
         * @param width 图片宽度
         * @param height 图片高度
         * @param waterText 图片内容
         * @param color 字体颜色
         * @param font 字体类型（windows 字体库基本都可以）
         * @param degree 字体倾斜角度
         * @param alpha 字体透明度
         * @return BufferedImage
         */
        @Override
        public BufferedImage getWaterImage(int width, int height, String waterText, Color color, Font font, Double degree, float alpha){

            return waterMarkByText(width, height, waterText, color, font, degree, alpha);
        }
    }

    /**
     * 生成背景透明的 文字水印，文字位于透明区域正中央，可设置旋转角度
     * @param width 生成图片宽度
     * @param heigth 生成图片高度
     * @param text 水印文字
     * @param color 颜色对象
     * @param font awt字体
     * @param degree 水印文字旋转角度
     * @param alpha 水印不透明度0f-1.0f
     */
    private static BufferedImage waterMarkByText(int width, int heigth, String text, Color color, Font font, Double degree, float alpha) {
        BufferedImage buffImg = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = buffImg.createGraphics();
        buffImg = g2d.getDeviceConfiguration().createCompatibleImage(width, heigth, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = buffImg.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (null != degree) {
            g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 4, (double) buffImg.getHeight() / 2);
        }
        g2d.setColor(color);
        g2d.setFont(font);
        //设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        //计算绘图偏移x、y，使得使得水印文字在图片中居中
        //这里需要理解x、y坐标是基于Graphics2D.rotate过后的坐标系
        int x = 0;
        int y = -heigth / 2;
        int markWidth = font.getSize() * getTextLength (text);
        int markHeight = font.getSize();
        float widthratio = 1.5f,heigthratio = 1.5f;
        while (x < width * widthratio) {
            y = -heigth / 2;
            while (y < heigth * heigthratio) {
                g2d.drawString (text, x, y);
                y += markHeight + 300;
            }
            x += markWidth + 300;
        }
        g2d.dispose();
        return buffImg;
    }

    /**
     * 获取字符串的长度
     * @param text 字符串
     * @return int
     */
    private static int getTextLength(String text){
        if(StringUtils.isEmpty(text)){
            return 1;
        }
        return text.length();
    }

    /**
     * 默认图片
     * @return ExcelCreateMarkUtil.BaseCreateWaterMarkImgImpl
     */
    public BaseCreateWaterMarkImgImpl defaultBaseCreateWaterMark(){
        BaseCreateWaterMarkImgImpl baseCreateWaterMarkImg = new BaseCreateWaterMarkImgImpl() {

            @Override
            public BufferedImage getWaterImage(int width, int height, String waterText) {
                return super.getWaterImage(width, height, waterText);
            }

            @Override
            public BufferedImage getWaterImage(int width, int height, String waterText, Color color, Font font, Double degree, float alpha) {
                return super.getWaterImage(width, height, waterText, color, font, degree, alpha);
            }
        };
        String userCode = "Qy@123!!!";
        baseCreateWaterMarkImg.setUserCode(userCode);
        return baseCreateWaterMarkImg;
    }

    /**
     * 默认图片
     * @return ExcelCreateMarkUtil.BaseCreateWaterMarkImgImpl
     */
    public BaseCreateWaterMarkImgImpl defaultBaseCreateWaterMark(int width, int height, String waterText, String userCode){
        BaseCreateWaterMarkImgImpl baseCreateWaterMarkImg = new BaseCreateWaterMarkImgImpl(width, height, waterText) {

            @Override
            public BufferedImage getWaterImage(int width, int height, String waterText) {
                return super.getWaterImage(width, height, waterText);
            }

            @Override
            public BufferedImage getWaterImage(int width, int height, String waterText, Color color, Font font, Double degree, float alpha) {
                return super.getWaterImage(width, height, waterText, color, font, degree, alpha);
            }
        };
        baseCreateWaterMarkImg.setUserCode(userCode);
        return baseCreateWaterMarkImg;
    }
}
