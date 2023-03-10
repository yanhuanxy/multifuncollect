package com.yanhuanxy.multifunexport.tools.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanhuanxy.multifunexport.tools.domain.emuns.excel.ExcelTypeEnums;
import com.yanhuanxy.multifunexport.tools.excel.watermark.ExcelCreateMarkUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author yym
 */
public class ExcelRptArrearsUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelRptArrearsUtil.class);

    private final String fileName;

    private int hascolIndex;

    private boolean valuehas = false;

    /**
     * jackson 转换类
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * excel 类型
     */
    private ExcelTypeEnums excelType;

    private ExcelCreateMarkUtil excelCreateMarkUtil;

    public ExcelRptArrearsUtil(String fileName){
        super();
        this.fileName = fileName;
    }

    public ExcelRptArrearsUtil(String fileName, boolean isMarkWater){
        this.fileName = fileName;
        if(isMarkWater){
            try {
                this.excelCreateMarkUtil = new ExcelCreateMarkUtil(fileName);
            }catch (Exception e){
                logger.error("水印工具注入失败！！",e);
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void createNewExcl(File exclFile,String dataTime,Map<String,Object> targetmap,
                                     Map<String,Object> datamap)throws IOException{
        FileOutputStream fileOutputStream = null;
        try {
            // 检查文件是否存在 校验excel 类型
            excelType = ExcelTypeEnums.checkFile(exclFile, fileName);
            //获得Workbook工作薄对象
            Workbook workbook = getWorkBook(exclFile);
            if(workbook != null){
                boolean isExcelCreateMark = ObjectUtils.isNotEmpty(excelCreateMarkUtil);
                for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++){
                    //获得当前sheet工作表
                    Sheet sheet = workbook.getSheetAt(sheetNum);
                    if(sheet == null){
                        continue;
                    }
                    //当前sheet 页是否存在需要修改的值
                    if (targetmap.containsKey((sheetNum + 1) + "") && datamap.containsKey((sheetNum + 1) + "")) {
//                        JSONArray thistargetmap = JSONArray.parseArray(targetmap.get((sheetNum + 1) + "").toString());
                        List<?> thistargetmap = objectMapper.readValue(targetmap.get((sheetNum + 1) + "").toString(), List.class);
                        List thisdatalist = (List) datamap.get((sheetNum + 1) + "");
                        //将需要修改的填充到相对应的位置
                        updateSheet(sheet,thistargetmap,thisdatalist,dataTime);
                    }
                    sheet.setForceFormulaRecalculation(true);
                    //添加水印
                    if(isExcelCreateMark){
                        excelCreateMarkUtil.setWaterMarkToExcel(excelType,workbook,sheet);
                    }
                }
                if(isExcelCreateMark){
                    excelCreateMarkUtil.deleteOldWaterMark();
                }
                //修改模板内容导出新模板
                workbook.setForceFormulaRecalculation(true);
                fileOutputStream = new FileOutputStream(exclFile);
                workbook.write(fileOutputStream);
                workbook.close();
            }
        } catch (FileNotFoundException e) {
            logger.error("文件未找到！！",e);
        } catch (Exception e) {
            logger.error("数据写入异常！！",e);
        } finally {
            if (null != fileOutputStream){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }

    /**
     *  判断之前当前时间是否已经导出过
     *  选择的第一行 跨2行
     * @param sheet sheet
     * @param thistargetmap  变更地址
     * @param thisdatalist 数据
     * @param dataTime 时间
     */
    private void updateSheet(Sheet sheet, List thistargetmap, List thisdatalist, String dataTime){

    }
//     private void updateSheet(Sheet sheet, JSONArray thistargetmap, List thisdatalist, String dataTime){
//         for(int i=0;i<thistargetmap.size();i++){
//             objectMapper.readValue();
//             JSONObject tmpobj= JSONObject.parseObject(thistargetmap.get(i).toString());
//             int startRow = Integer.parseInt((String) tmpobj.get("row")) - 1;
//
//             int countx = 0, county = 0;
//             List<Object[]> dataList = (List<Object[]>) thisdatalist.get(i);
//            if(dataList.size()>0){
//                int lastCellNum = startRowLastCellNum(sheet,startRow,dataTime);
//
//                for (int rowNumcopy = startRow; rowNumcopy <= (startRow + 1 + dataList.size()); rowNumcopy++) {
//                    county = 0;
//                    if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
//                        //获得当前行rowNumcopy
//                        HSSFRow thisrow = (HSSFRow) sheet.getRow(rowNumcopy);
//                        if (thisrow == null) {
//                            thisrow = (HSSFRow) sheet.createRow(rowNumcopy);
//                        }
//                        //修改excel 2003
//                        if(countx<2){
//                            updateExcel2003RowAndCell(thisrow, lastCellNum, county,dataList.get(0),dataTime,true);
//                        }else {
//                            //当前行数据
//                            Object[] columnData = dataList.get(countx-2);
//                            updateExcel2003RowAndCell(thisrow, lastCellNum, county,columnData,null,false);
//                        }
//                    } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
//                        XSSFRow thisrow = (XSSFRow) sheet.getRow(rowNumcopy);
//                        if (thisrow == null) {
//                            thisrow = (XSSFRow) sheet.createRow(rowNumcopy);
//                        }
//                        //修改excel 2007
//                        if(countx<2){
//                            updateExcel2007RowAndCell(thisrow, lastCellNum, county, dataList.get(0), dataTime,true);
//                        }else {
//                            Object[] columnData = dataList.get(countx-2);
//                            updateExcel2007RowAndCell(thisrow, lastCellNum, county, columnData,null,false);
//                        }
//                    }
//                    countx++;
//                }
//                System.out.println("----nextCellNum:"+lastCellNum);
//                if(!valuehas){
//                    sheet.addMergedRegion(new CellRangeAddress(startRow,startRow+1,hascolIndex,hascolIndex)); // 合并行
//                }
//            }
//         }
//     }

    private void updateExcel2003RowAndCell(HSSFRow thisrow,int lastCellNum,int county,Object[] columnData,String dataTime,boolean flag){
        //在之前行位置增加数据
        HSSFCell aftercell = thisrow.getCell(lastCellNum!=0?lastCellNum-1:lastCellNum);
        if (aftercell == null) {
            aftercell = thisrow.createCell(lastCellNum!=0?lastCellNum-1:lastCellNum);
        }
        if(flag){
            if(!valuehas){
                hascolIndex = lastCellNum;
                HSSFCell cell = thisrow.getCell(lastCellNum);
                if (cell == null) {
                    cell = thisrow.createCell(lastCellNum);
                }
                cell.setCellStyle(aftercell.getCellStyle());
                //dataTime = dataTime.replace("-",".");
                if(cell.getCellType() == CellType.FORMULA){
                    cell.setCellFormula(cell.getCellFormula());
                }else{
                    if (NumberUtils.isNumber(dataTime)) {
                        cell.setCellValue(Double.valueOf(dataTime).intValue());
                    } else {
                        cell.setCellValue(dataTime);
                    }
                }
            }
        }else {
            //从本行最后一列开始
            if(valuehas){
                lastCellNum = lastCellNum - columnData.length;
            }
            for (int cellNumcopy = lastCellNum; cellNumcopy < (lastCellNum + (columnData.length)); cellNumcopy++) {
                HSSFCell cell = thisrow.getCell(cellNumcopy);
                if (cell == null) {
                    cell = thisrow.createCell(cellNumcopy);
                }
                //当前列的数据
                String columnval = columnData[county] == null ? "" : columnData[county].toString();
                county++;
                //赋值
                cell.setCellStyle(aftercell.getCellStyle());
                if(cell.getCellType() == CellType.FORMULA){
                    cell.setCellFormula(cell.getCellFormula());
                }else{
                    if (NumberUtils.isNumber(columnval)) {
                        cell.setCellValue(columnval+"");
                    } else {
                        cell.setCellValue(columnval.replace("date",""));
                    }
                }
            }
        }
    }

    private void updateExcel2007RowAndCell(XSSFRow thisrow,int nextCellNum,int county,Object[] columnData,String dataTime,boolean flag){
        //在之前行位置增加数据
        XSSFCell aftercell = thisrow.getCell(nextCellNum!=0?nextCellNum-1:nextCellNum);
        if (aftercell == null) {
            aftercell = thisrow.createCell(nextCellNum!=0?nextCellNum-1:nextCellNum);
        }
        if(flag){
            if(!valuehas){
                hascolIndex = nextCellNum;
                XSSFCell cell = thisrow.getCell(nextCellNum);
                if (cell == null) {
                    cell = thisrow.createCell(nextCellNum);
                }
                cell.setCellStyle(aftercell.getCellStyle());
                //dataTime = dataTime.replace("-",".");
                if(cell.getCellType() == CellType.FORMULA){
                    cell.setCellFormula(cell.getCellFormula());
                }else{
                    if (NumberUtils.isNumber(dataTime)) {
                        cell.setCellValue(dataTime+"");
                    } else {
                        cell.setCellValue(dataTime.replace("date",""));
                    }
                }
            }
        }else{
            if(valuehas){
                nextCellNum = nextCellNum - columnData.length;
            }
            for (int cellNumcopy = nextCellNum; cellNumcopy < (nextCellNum + (columnData.length)); cellNumcopy++) {
                XSSFCell cell = thisrow.getCell(cellNumcopy);
                if (cell == null) {
                    cell = thisrow.createCell(cellNumcopy);
                }
                //当前列的数据
                String columnval = columnData[county] == null ? "" : columnData[county].toString();
                county++;
                //赋值
                cell.setCellStyle(aftercell.getCellStyle());
                if(cell.getCellType() == CellType.FORMULA){
                    cell.setCellFormula(cell.getCellFormula());
                }else{
                    if (NumberUtils.isDigits(columnval)) {
                        cell.setCellValue(Double.valueOf(columnval));
                    } else {
                        cell.setCellValue(columnval);
                    }
                }
            }
        }
    }

    private Workbook getWorkBook(File file) throws IOException {
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        InputStream is =null;
        try {
            //获取excel文件的io流
            is= new FileInputStream(file);
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(ExcelTypeEnums.EXCEL_2003L.equals(excelType)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(ExcelTypeEnums.EXCEL_2007U.equals(excelType)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            assert is != null;
            is.close();
        }
        return workbook;
    }

    private int startRowLastCellNum(Sheet sheet, int startRow,String dataTime){
        String aftervalue ="";
        int lastCellNum =0;
        if(ExcelTypeEnums.EXCEL_2003L.equals(excelType)){
            HSSFRow startrow = (HSSFRow) sheet.getRow(startRow);
            if (startrow == null) {
                startrow = (HSSFRow)sheet.createRow(startRow);
            }else{
                lastCellNum = startrow.getLastCellNum()-1;
            }

            HSSFCell aftercell = startrow.getCell(lastCellNum);
            if (aftercell == null) {
                aftercell = startrow.createCell(lastCellNum);
            }
            aftervalue = getCellValue(aftercell);
        }else if(ExcelTypeEnums.EXCEL_2007U.equals(excelType)){
            XSSFRow startrow = (XSSFRow) sheet.getRow(startRow);
            if (startrow == null) {
                startrow = (XSSFRow) sheet.createRow(startRow);
            }else{
                lastCellNum = startrow.getLastCellNum()-1;//取的是数量需变成 下标
            }
            XSSFCell aftercell = startrow.getCell(lastCellNum);//用的是下标取值
            if (aftercell == null) {
                aftercell = startrow.createCell(lastCellNum);
            }
            XSSFCell fristcell = startrow.getCell(0);
            System.out.println("-----fristCellNum"+getCellValue(fristcell));
            aftervalue = getCellValue(aftercell);
        }
        valuehas = false;
        hascolIndex = 0;
        System.out.println("----lastCellNum:"+lastCellNum+"---lastvalue:"+aftervalue);
        //dataTime = dataTime.replace("-",".");
        if(aftervalue.equals(dataTime)){
            valuehas = true;
        }
        return lastCellNum+1;
    }

    /**
     * 获取excl 不同单元格类型的值
     * @param cell 单元格
     * @return String
     */
    private String getCellValue(Cell cell) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        CellType cellType = cell.getCellType();
        // 把数字当成String来读，避免出现1读成1.0的情况
        if (cellType == CellType.NUMERIC) {
            cell.setCellType(CellType.STRING);
        }

        // 判断数据的类型
        switch (cellType) {
            case NUMERIC: // 数字、日期
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    cellValue = fmt.format(cell.getDateCellValue()); // 日期型
//                } else {
                    cellValue = String.valueOf(Double.valueOf(cell.getStringCellValue()).intValue());
//                cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
                    if (cellValue.contains("E")) {
                        cellValue = String.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue()); // 数字
                    }
//                }
                break;
            case STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                cellValue = cell.getStringCellValue();
                break;
            case ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static boolean IsFileInUse(File file){
        boolean inUse = true;
        try{
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.close();
            inUse = false;
        }catch (Exception e){
            logger.error("文件不存在！！",e);
        }
        return inUse;
    }

    public static void downloadExcel(HttpServletResponse response, String name, File file) throws IOException{

        //1、设置响应的头文件，会自动识别文件内容
        response.setContentType("application/vnd.ms-excel;charset=utf-8");

        //2、设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes(), StandardCharsets.ISO_8859_1));
        //3、输出流
        //4、获取服务端生成的excel文件
        try (OutputStream out = response.getOutputStream(); InputStream in = new FileInputStream(file)) {
            //5、输出文件
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
        } catch (IOException e) {
            logger.error("文件下载异常！！", e);
            throw new IOException(e);
        }
    }
}

