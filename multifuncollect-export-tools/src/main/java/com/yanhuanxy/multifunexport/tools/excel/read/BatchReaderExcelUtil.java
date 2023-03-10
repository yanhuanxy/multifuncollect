package com.yanhuanxy.multifunexport.tools.excel.read;

import com.yanhuanxy.multifunexport.tools.domain.emuns.excel.ExcelTypeEnums;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *  * poi事件模式读取文件 工具
 *  * @author yym
 *  * @date 20190916 18:07
 */
public class BatchReaderExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(BatchReaderExcelUtil.class);

    public BatchReaderExcelUtil(){ }

    /**
     * 按类型读取
     * @param fileName
     * @throws Exception
     */
    public void readExcel(String fileName, IExcelRowReader rowReader) throws Exception{
        this.readExcel(new File(fileName), rowReader);
    }

    /**
     * 按类型读取
     * @param file
     * @throws Exception
     */
    public void readExcel(File file, IExcelRowReader rowReader) throws Exception{
        String fileName = file.getName();
        ExcelTypeEnums excelType = ExcelTypeEnums.getExcelTypeEnum(fileName);
        IBatchReaderExcel excel = null;
        // 处理excel2003文件
        if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
            excel = new BatchReaderExcelxls();
            // 处理excel2007文件
        } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
            excel = new BatchReaderExcelxlsx();
        } else if(ExcelTypeEnums.CSV.equals(excelType)){
            excel = new BatchReaderExcelcsv(file);
        } else{
            throw new Exception("文件格式错误，fileName的扩展名只能是.xls,.xlsx,.csv");
        }
        excel.setRowReader(rowReader);
        excel.process(file);
    }

    /**
     * 按类型读取
     * @param inputstream
     * @throws Exception
     */
    public void readExcel(InputStream inputstream, String fileName, IExcelRowReader rowReader) throws Exception{
        ExcelTypeEnums excelType = ExcelTypeEnums.getExcelTypeEnum(fileName);
        IBatchReaderExcel excel = null;
        // 处理excel2003文件
        if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
            excel = new BatchReaderExcelxls();
            excel.setRowReader(rowReader);
            excel.process(inputstream);
            // 处理excel2007文件
        } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
            excel = new BatchReaderExcelxlsx();
            excel.setRowReader(rowReader);
            excel.process(inputstream);
        } else if(ExcelTypeEnums.CSV.equals(excelType)){
            ByteArrayOutputStream outputStream = convertToByteArrayOutputStream(inputstream);
            excel = new BatchReaderExcelcsv(new ByteArrayInputStream(outputStream.toByteArray()));
            excel.setRowReader(rowReader);
            excel.process(inputstream);
        } else{
            throw new Exception("文件格式错误，fileName的扩展名只能是.xls,.xlsx,.csv");
        }
    }

    /**
     * 转换为数据
     * @param inputStream
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    private ByteArrayOutputStream convertToByteArrayOutputStream(InputStream inputStream) throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, len);
        }
        outputStream.flush();
        return outputStream;
    }

    /**
     * 按类型读取
     * @param multipartFile
     * @throws Exception
     */
    @SuppressWarnings("all")
    public void readExcel(MultipartFile multipartFile, IExcelRowReader rowReader) throws Exception{
        String fileName = multipartFile.getOriginalFilename();
        ExcelTypeEnums excelType = ExcelTypeEnums.getExcelTypeEnum(fileName);
        try (InputStream inputStream = multipartFile.getInputStream()){
            IBatchReaderExcel excel = null;
            // 处理excel2003文件
            if (ExcelTypeEnums.EXCEL_2003L.equals(excelType)) {
                excel = new BatchReaderExcelxls();
                // 处理excel2007文件
            } else if (ExcelTypeEnums.EXCEL_2007U.equals(excelType)) {
                excel = new BatchReaderExcelxlsx();
            } else if(ExcelTypeEnums.CSV.equals(excelType)){
                excel = new BatchReaderExcelcsv(multipartFile.getInputStream());
            } else{
                throw new Exception("文件格式错误，fileName的扩展名只能是.xls,.xlsx,.csv");
            }
            excel.setRowReader(rowReader);
            excel.process(inputStream);
        }
    }

    /**
     * 拼装 列
     * @param obj
     * @return List
     * @throws Exception
     */
    public static List<String> getColumnNameByObject(Object obj){
        //容器
        List<String> list = new ArrayList<>();
        try {
           Class<?> rowClazz= obj.getClass();
           Field[] fields = FieldUtils.getAllFields(rowClazz);
           if (fields == null || fields.length < 1) {
               return null;
           }
           for (int j = 0; j < fields.length; j++) {
               list.add(fields[j].getName());
           }
       }catch (Exception e){
            logger.error("批量读取文件时，反射获取实体类字段异常！",e);
       }
        return list;
    }


}
