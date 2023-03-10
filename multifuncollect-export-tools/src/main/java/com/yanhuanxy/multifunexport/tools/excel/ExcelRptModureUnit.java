package com.yanhuanxy.multifunexport.tools.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanhuanxy.multifunexport.tools.domain.emuns.excel.ExcelTypeEnums;
import com.yanhuanxy.multifunexport.tools.excel.watermark.ExcelCreateMarkUtil;
import com.yanhuanxy.multifunexport.tools.exception.operation.ReadException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yym
 */
public class ExcelRptModureUnit {
    private static final Logger logger = LoggerFactory.getLogger(ExcelRptModureUnit.class);

    private static final String SYMBOL_COMMA = ",";

    private static final String DEFAULT_VAL = "0";

    private final String fileName;

    private int nowSheetShiftIndex;

    /**
     * excel 类型
     */
    private ExcelTypeEnums excelType;

    /**
     * 水印类
     */
    private ExcelCreateMarkUtil excelCreateMarkUtil;

    /**
     * jackson 转换类
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 修改
     */
    private IExcelRptModure iExcelRptModure;

    public void setExcelRptModure(IExcelRptModure iExcelRptModure) {
        this.iExcelRptModure = iExcelRptModure;
    }

    public String getFileName() {
        return fileName;
    }

    public int getNowSheetShiftIndex() {
        return nowSheetShiftIndex;
    }

    public void setNowSheetShiftIndex(int nowSheetShiftIndex) {
        this.nowSheetShiftIndex = nowSheetShiftIndex;
    }

    public ExcelRptModureUnit(String fileName) {
        super();
        this.fileName = fileName;
    }

    public ExcelRptModureUnit(String fileName, boolean isMarkWater) {
        this.fileName = fileName;
        if (isMarkWater) {
            try {
                this.excelCreateMarkUtil = new ExcelCreateMarkUtil(fileName);
            } catch (Exception e) {
                logger.error("模板导出水印工具注入失败！！", e);
            }
        }
    }

    /**
     * 导出接口
     *
     * @param response http
     * @param file 文件
     * @param targetmap 插入行列
     * @param dataList  数据
     */
    public void exportExcel(HttpServletResponse response,
                            HttpServletRequest request,
                            String name,
                            MultipartFile file,
                            Map<String, Object> targetmap,
                            Map<String, Object> dataList) throws Exception {
        try {
            //清除Buffer
            response.reset();
            response.setHeader("Cache-Control", "no-cache");
            String originalUrl = request.getHeader("Origin");
            if (originalUrl != null) {
                logger.info(" Origin= {}", request.getHeader("Origin"));
                response.addHeader("Access-Control-Allow-Origin", originalUrl);
            }
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(name.getBytes(), StandardCharsets.ISO_8859_1));
            updateExcl(response.getOutputStream(), file, targetmap, dataList);
        } catch (IOException e) {
            logger.error("模板导出写入HttpServletResponse流出错!", e);
        }
    }

    /**
     * 文件导出接口
     * @param response http
     * @param file 文件
     */
    public void exportExcel(HttpServletResponse response,
                            HttpServletRequest request,
                            String name,
                            File file) throws IOException{

        //清除Buffer
        response.reset();
        response.setHeader("Cache-Control", "no-cache");
        String originalUrl = request.getHeader("Origin");
        if (originalUrl != null) {
            logger.info(" Origin= {}", request.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Origin", originalUrl);
        }
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));

        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(name.concat(fileSuffix).getBytes(), StandardCharsets.ISO_8859_1));
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)); ServletOutputStream outputStream = response.getOutputStream()) {
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            int totalLen = 0;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                totalLen+=bytesRead;
                bos.write(buff, 0, bytesRead);
            }
            response.setContentLength(totalLen);
            bos.close();
            outputStream.flush();
        } catch (IOException e) {
            logger.error("模板导出写入HttpServletResponse流出错!", e);
            throw new IOException("模板导出写入HttpServletResponse流出错");
        }
    }

    /**
     * @param out 输出流
     * @param file 文件模板
     * @param targetmap 位置信息
     * @param datamap 数据集合
     */
    public synchronized void createNewExcl(OutputStream out, MultipartFile file,
                                           Map<String, Object> targetmap,
                                           Map<String, Object> datamap) throws IOException {
        updateExcl(out, file, targetmap, datamap);
    }

    /**
     * 将数据写入本地文件
     * @param inFile 输入文件
     */
    public void writeToExcel(File inFile) throws IOException{
        try(InputStream inputStream = new FileInputStream(inFile)) {
            Workbook workbook = updateExcl(inputStream);
            if(workbook != null){
                try(OutputStream fileOutputStream = new FileOutputStream(inFile)) {
                    workbook.write(fileOutputStream);
                    fileOutputStream.flush();
                    workbook.close();
                }
            }
        }
    }


    /**
     * 将数据写入本地文件
     * @param inFile 输入文件
     * @param targetmap 配置
     * @param datamap 数据
     */
    public void writeToExcel(File inFile, Map<String, Object> targetmap,
                             Map<String, Object> datamap) throws IOException{
        try(InputStream inputStream = new FileInputStream(inFile)) {
            Workbook workbook = updateExcl(inputStream, targetmap, datamap);
            if(workbook != null){
                try(OutputStream fileOutputStream = new FileOutputStream(inFile)) {
                    workbook.write(fileOutputStream);
                    fileOutputStream.flush();
                    workbook.close();
                }
            }
        }
    }


    /**
     * 修改excel
     *
     * @param out 输出流
     * @param file 文件模板
     * @param targetmap 位置信息
     * @param datamap 数据集合
     */
    private void updateExcl(OutputStream out, MultipartFile file,
                            Map<String, Object> targetmap,
                            Map<String, Object> datamap) throws IOException{

        try(InputStream inputStream = file.getInputStream()) {
            Workbook workbook = updateExcl(inputStream, targetmap, datamap);
            if(workbook != null){
                workbook.write(out);
                out.flush();
                workbook.close();
            }
        }
    }

    /**
     * 修改excel
     *
     * @param inputStream 文件模板流
     * @param targetmap 位置信息
     * @param datamap 数据集合
     */
    private Workbook updateExcl(InputStream inputStream,
                           Map<String, Object> targetmap,
                           Map<String, Object> datamap) throws IOException {
        Workbook workbook = null;
        try {
            //检查文件是否存在 获取excel文件类型
            excelType = ExcelTypeEnums.checkFile(inputStream, fileName);
            workbook = getWorkBook(inputStream);
            if (workbook != null) {
                boolean isExcelCreateMark = ObjectUtils.isNotEmpty(excelCreateMarkUtil);
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    Sheet sheet = workbook.getSheetAt(sheetNum);
                    if (sheet == null) {
                        continue;
                    }
                    String sheetNumIndex = String.valueOf(sheetNum + 1);
                    if (targetmap.containsKey(sheetNumIndex) && datamap.containsKey(sheetNumIndex)) {
                        Object o = targetmap.get(sheetNumIndex);
                        List<?> thistargetmap = objectMapper.readValue(targetmap.get(sheetNumIndex).toString(), List.class);
                        List<?> thisdatalist = (List) datamap.get(sheetNumIndex);
                        updateSheet(sheet, thistargetmap, thisdatalist);
                    }
                    sheet.setForceFormulaRecalculation(true);
                    //添加水印
                    if (isExcelCreateMark) {
                        excelCreateMarkUtil.setWaterMarkToExcel(excelType, workbook, sheet);
                    }
                }
                if(isExcelCreateMark){
                    excelCreateMarkUtil.deleteOldWaterMark();
                }

                workbook.setForceFormulaRecalculation(true);
            }
        } catch (FileNotFoundException e) {
            logger.error("模板文件未找到！" + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("模板写入异常！" + e.getMessage(), e);
            throw new IOException("模板写入异常!");
        }
        return workbook;
    }


    /**
     * 修改excel
     *
     * @param inputStream 文件模板流
     */
    private Workbook updateExcl(InputStream inputStream) throws IOException {
        Workbook workbook = null;
        try {
            //检查文件是否存在 获取excel文件类型
            excelType = ExcelTypeEnums.checkFile(inputStream, fileName);
            workbook = getWorkBook(inputStream);
            if (workbook != null) {
                boolean isExcelCreateMark = ObjectUtils.isNotEmpty(excelCreateMarkUtil);
                for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                    Sheet sheet = workbook.getSheetAt(sheetNum);
                    if (sheet == null) {
                        continue;
                    }
                    String sheetNumIndex = String.valueOf(sheetNum + 1);
                    Map<String, Map<String, Object>> excelChangeData = iExcelRptModure.updateSheet(sheetNumIndex);
                    Map<String, Object> orderTargetMap = excelChangeData.get("orderTargetMap");
                    Map<String, Object> orderBaseDataMap = excelChangeData.get("orderBaseDataMap");
                    if (orderTargetMap.containsKey(sheetNumIndex) && orderBaseDataMap.containsKey(sheetNumIndex)) {
                        List<?> thistargetmap = objectMapper.readValue(orderTargetMap.get(sheetNumIndex).toString(), List.class);
                        List thisdatalist = (List) orderBaseDataMap.get(sheetNumIndex);
                        updateSheet(sheet, thistargetmap, thisdatalist);
                    }
                    sheet.setForceFormulaRecalculation(true);
                    //添加水印
                    if (isExcelCreateMark) {
                        excelCreateMarkUtil.setWaterMarkToExcel(excelType, workbook, sheet);
                    }
                    excelChangeData.clear();
                    excelChangeData = null;
                }
                if(isExcelCreateMark){
                    excelCreateMarkUtil.deleteOldWaterMark();
                }

                workbook.setForceFormulaRecalculation(true);
            }
        } catch (FileNotFoundException e) {
            logger.error("模板文件未找到！" + e.getMessage(), e);
        } catch (ReadException e) {
            throw new IOException(e.getMessage());
        } catch (Exception e) {
            logger.error("模板写入异常！" + e.getMessage(), e);
            throw new IOException("模板写入异常!");
        }
        return workbook;
    }

    /**
     * 转字符串
     * @param val 值
     * @return String
     */
    private String convertToStr(Object val){

        return Objects.isNull(val) ? DEFAULT_VAL : String.valueOf(val);
    }

    /**
     * 计算 excle sheet页的起始行列 结束行列
     *
     * @param sheet 页
     * @param thistargetmap 坐标
     * @param thisdatalist  数据
     */
    private void updateSheet(Sheet sheet, List thistargetmap, List thisdatalist){

    }
//    private void updateSheet(Sheet sheet, JSONArray thistargetmap, List thisdatalist) {
//        //初始化当前sheet页下移数
//        setNowSheetShiftIndex(0);
//        //重新排序
//        String oldthistargetmapstr = thistargetmap.toString();
//        JSONArray oldthistargetmap = JSONObject.parseArray(oldthistargetmapstr);
////        setSortOrder(thistargetmap, true);
//        for (int i = 0; i < thistargetmap.size(); i++) {
//            JSONObject tmpobj = JSONObject.parseObject(thistargetmap.get(i).toString());
//            String tempRow = convertToStr(tmpobj.get("row"));
//            String tempColumn = convertToStr(tmpobj.get("column"));
//            Integer tempindex = null;
//            for (int p = 0; p < oldthistargetmap.size(); p++) {
//                JSONObject oldtmpobj = JSONObject.parseObject(oldthistargetmap.get(p).toString());
//                String oldtempRow = convertToStr(oldtmpobj.get("row"));
//                String oldtempColumn = convertToStr(oldtmpobj.get("column"));
//                if (tempRow.equals(oldtempRow) && tempColumn.equals(oldtempColumn)) {
//                    tempindex = p;
//                    break;
//                }
//            }
//            if (tempindex == null) {
//                continue;
//            }
//            List<Object[]> dataList = (List<Object[]>) thisdatalist.get(tempindex);
//
//            int startRow = 0;
//            if(ObjectUtils.isNotEmpty(dataList)){
//                int dataListSize = dataList.size();
//                int oldSheetShiftIndex = getNowSheetShiftIndex();
//                startRow = addExcelRowNumAndSetRowNum(sheet, dataListSize, tempRow);
//                if (oldSheetShiftIndex != 0) {
//                    startRow += oldSheetShiftIndex;
//                }
//
//                String startColumn = convertToStr(tmpobj.get("column"));
//                int countx = 0, county = 0;
//                for (int rowNumcopy = startRow; rowNumcopy < (startRow + dataListSize); rowNumcopy++) {
//                    Object[] columnData = dataList.get(countx);
//                    countx++;
//                    county = 0;
//                    //计算开始结束列
//                    int lastEndColumnNum = 0;
//                    int lastStartColumnNum = 0;
//                    int columnDataSize = columnData.length;
//                    if (startColumn != null && startColumn.contains(",")) {
//                        String[] tempColumnArr = startColumn.split(",");
//                        lastStartColumnNum = Integer.parseInt(tempColumnArr[0]);
//                        if (lastStartColumnNum > 0) {
//                            lastStartColumnNum = lastStartColumnNum - 1;
//                        }
//                        int endColumnNum = Integer.parseInt(tempColumnArr[1]);
//                        if (endColumnNum > 0) {
//                            endColumnNum = endColumnNum - 1;
//                        }
//                        lastEndColumnNum = endColumnNum;
//                        int hasColumnNum = lastEndColumnNum - lastStartColumnNum;
//                        if (columnDataSize > hasColumnNum) {
//                            lastEndColumnNum = lastStartColumnNum + columnDataSize;
//                        }
//                    } else {
//                        lastStartColumnNum = Integer.parseInt(startColumn);
//                        if (lastStartColumnNum > 0) {
//                            lastStartColumnNum = lastStartColumnNum - 1;
//                        }
//                        lastEndColumnNum = lastStartColumnNum + columnDataSize;
//                    }
//                    if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
//                        HSSFRow thisrow = (HSSFRow) sheet.getRow(rowNumcopy);
//                        if (thisrow == null) {
//                            thisrow = (HSSFRow) sheet.createRow(rowNumcopy);
//                        }
//                        updateExcel2003RowAndCell(thisrow, county, columnData, lastStartColumnNum, lastEndColumnNum);
//                    } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
//                        XSSFRow thisrow = (XSSFRow) sheet.getRow(rowNumcopy);
//                        if (thisrow == null) {
//                            thisrow = (XSSFRow) sheet.createRow(rowNumcopy);
//                        }
//                        updateExcel2007RowAndCell(thisrow, county, columnData, lastStartColumnNum, lastEndColumnNum);
//                    }
//                }
//            }
//
//        }
//    }

    /**
     * 添加行（整体往下移动） 重新设置 起始行
     *
     * @param sheet 页
     * @param dataListSize 数据集合
     * @param tempRow 当前行下标
     * @return int
     */
    private int addExcelRowNumAndSetRowNum(Sheet sheet, int dataListSize, String tempRow) {
        //如果数据量(A)大于输入行(B)则插入(N = A-B)行 否则 不用
        int startRow = 0;
        if (tempRow != null && tempRow.contains(SYMBOL_COMMA)) {
            String[] tempRowArr = tempRow.split(SYMBOL_COMMA);
            int startRowNum = Integer.parseInt(tempRowArr[0]);
            if (startRowNum > 0) {
                startRowNum = startRowNum - 1;
            }
            int endRowNum = Integer.parseInt(tempRowArr[1]);
            if (endRowNum > 0) {
                endRowNum = endRowNum - 1;
            }
            int centreRow = startRowNum;
            if (startRowNum > endRowNum) {
                startRowNum = endRowNum;
                endRowNum = centreRow;
            }
            int exclTempRowNum = endRowNum - startRowNum + 1;
            if (dataListSize > exclTempRowNum) {
                int addTempRowNum = dataListSize - exclTempRowNum;
                int oldSheetShiftIndex = getNowSheetShiftIndex();
                if (oldSheetShiftIndex != 0) {
                    endRowNum += oldSheetShiftIndex;
                }
                int lastRow = endRowNum;
                sheet.shiftRows(lastRow, sheet.getLastRowNum(), addTempRowNum, true, false);
                int nowSheetShiftIndex = oldSheetShiftIndex + addTempRowNum;
                setNowSheetShiftIndex(nowSheetShiftIndex);

                if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
                    HSSFSheet tempSheet = (HSSFSheet) sheet;
                    HSSFRow rowSource = tempSheet.getRow(startRowNum);
                    CellStyle rowStyle = rowSource.getRowStyle();
                    for (int tempRowIndex = lastRow; tempRowIndex <= endRowNum + addTempRowNum; tempRowIndex++) {
                        HSSFRow rowInsert = (HSSFRow) sheet.createRow(tempRowIndex);
                        if (rowStyle != null) {
                            rowInsert.setRowStyle(rowStyle);
                        }
                        rowInsert.setHeight(rowSource.getHeight());
                        for (int col = 0; col < rowSource.getLastCellNum(); col++) {
                            HSSFCell cellsource = rowSource.getCell(col);
                            HSSFCell cellInsert = rowInsert.createCell(col);
                            if (cellsource != null) {
                                HSSFCellStyle cellStyle = cellsource.getCellStyle();
                                if (cellStyle != null) {
                                    cellInsert.setCellStyle(cellsource.getCellStyle());
                                }
                            }
                        }
                    }
                } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
                    XSSFSheet tempSheet = (XSSFSheet) sheet;
                    XSSFRow rowSource = tempSheet.getRow(endRowNum);
                    XSSFCellStyle rowStyle = rowSource.getRowStyle();
                    for (int tempRowIndex = lastRow; tempRowIndex <= endRowNum + addTempRowNum; tempRowIndex++) {
                        XSSFRow rowInsert = (XSSFRow) sheet.createRow(tempRowIndex);
                        if (rowStyle != null) {
                            rowInsert.setRowStyle(rowStyle);
                        }
                        rowInsert.setHeight(rowSource.getHeight());
                        for (int col = 0; col < rowSource.getLastCellNum(); col++) {
                            XSSFCell cellsource = rowSource.getCell(col);
                            XSSFCell cellInsert = rowInsert.createCell(col);
                            if (cellsource != null) {
                                XSSFCellStyle cellStyle = cellsource.getCellStyle();
                                if (cellStyle != null) {
                                    cellInsert.setCellStyle(cellsource.getCellStyle());
                                }
                            }
                        }
                    }
                }
            }
            startRow = startRowNum;
        } else {
            startRow = Integer.parseInt(tempRow);
            if (startRow > 0) {
                startRow = startRow - 1;
            }
        }
        return startRow;
    }

    /**
     * 更新excel2003的行列数据
     *
     * @param thisrow 当前行
     * @param county 计数
     * @param columnData 列数据
     * @param lastStartColumnNum 最后开始列
     * @param lastEndColumnNum 最后结束列
     */
    private void updateExcel2003RowAndCell(HSSFRow thisrow, int county, Object[] columnData, int lastStartColumnNum, int lastEndColumnNum) {
        for (int cellNumcopy = lastStartColumnNum; cellNumcopy < lastEndColumnNum; cellNumcopy++) {
            HSSFCell cell = thisrow.getCell(cellNumcopy);
            if (cell == null) {
                cell = thisrow.createCell(cellNumcopy);
            }
            if (county >= columnData.length) {
                continue;
            }
            String columnval = Objects.isNull(columnData[county]) ? "" : String.valueOf(columnData[county]) ;

            county++;
            if (CellType.FORMULA.equals(cell.getCellType())) {
                cell.setCellFormula(cell.getCellFormula());
            } else {
                if (NumberUtils.isDigits(columnval)) {
                    cell.setCellValue(Double.parseDouble(columnval));
                } else {
                    cell.setCellValue(columnval);
                }
            }
        }
    }

    /**
     * 更新excl2007 的行列数据
     *
     * @param thisrow 当前行
     * @param county  计数
     * @param columnData 列数据
     * @param lastStartColumnNum 最后开始列
     * @param lastEndColumnNum 最后结束列
     */
    private void updateExcel2007RowAndCell(XSSFRow thisrow, int county, Object[] columnData, int lastStartColumnNum, int lastEndColumnNum) {
        for (int cellNumcopy = lastStartColumnNum; cellNumcopy < lastEndColumnNum; cellNumcopy++) {
            XSSFCell cell = thisrow.getCell(cellNumcopy);
            if (cell == null) {
                cell = thisrow.createCell(cellNumcopy);
            }
            if (county >= columnData.length) {
                continue;
            }
            String columnval =  Objects.isNull(columnData[county]) ? "" : String.valueOf(columnData[county]) ;;

            county++;
            //cell.setCellStyle(cell.getCellStyle());
            if (CellType.FORMULA.equals(cell.getCellType())) {
                cell.setCellFormula(cell.getCellFormula());
            } else {
                if (NumberUtils.isNumber(columnval)) {
                    cell.setCellValue(Double.parseDouble(columnval));
                } else {
                    cell.setCellValue(columnval);
                }
            }
        }
    }

    /**
     * 获取工作簿
     *
     * @param inputStream 文件流
     * @return Workbook
     * @throws IOException
     */
    private Workbook getWorkBook(InputStream inputStream) throws IOException {
        Workbook workbook = null;
        try {
            if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            logger.error("获取excel workbook异常!", e);
            throw new IOException(e);
        }
        return workbook;
    }

    public static void main(String[] args) throws Exception{
        ExcelRptModureUnit excelRptModureUnit = new ExcelRptModureUnit("tmpfile.xls",true);
        excelRptModureUnit.writeToExcel(new File("C:\\Users\\yanhuan\\Desktop\\tmpfile.xls"), new HashMap<>(), new HashMap<>());
//        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\yanhuan\\Desktop\\tmpfile.xls");
//        excelRptModureUnit.updateExcl(fileOutputStream, excelRptModureUnit.fileToMulti(new File("C:\\Users\\yanhuan\\Desktop\\test2222.xls")) , new HashMap<>(), new HashMap<>());
//        ExcelCreateMarkUtil.BaseCreateWaterMarkImgImpl baseCreateWaterMarkImg = excelRptModureUnit.excelCreateMarkUtil.defaultBaseCreateWaterMark();
//        BufferedImage bufferedImage = baseCreateWaterMarkImg.getBufferedImage();
//        ImageIO.write(bufferedImage,"png",new FileOutputStream("C:\\Users\\yanhuan\\Desktop\\test1.png"));
    }
}
