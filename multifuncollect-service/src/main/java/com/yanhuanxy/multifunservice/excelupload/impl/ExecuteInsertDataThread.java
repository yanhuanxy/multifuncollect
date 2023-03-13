package com.yanhuanxy.multifunservice.excelupload.impl;

import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifundomain.desfrom.dto.DesDataUploadBatchDTO;
import com.yanhuanxy.multifundomain.desfrom.dto.DesTableColumnDTO;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadDataExcelDTO;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadMoreDataConfDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ExecuteInsertDataThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteInsertDataThread.class);

    private static final int MAX_COMMIT_SIZE = 800;

    private static final String TMP_TABLE_NAME_PREFIX = "UP_TMP_";

    private static final Integer COLUMN_NUMBER_10 = 10;
    private static final Integer COLUMN_NUMBER_20 = 20;
    private static final Integer COLUMN_NUMBER_30 = 30;
    private static final Integer COLUMN_NUMBER_40 = 40;
    private static final Integer COLUMN_NUMBER_50 = 40;

    private final SqlSessionFactory sqlSessionFactory;

    private final UploadDataExcelDTO beforeUploadDataExcelConf;

    private final String[] uploadTableName;

    public ExecuteInsertDataThread(SqlSessionFactory sqlSessionFactory, UploadDataExcelDTO beforeUploadDataExcelConf, String[] uploadTableName) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.beforeUploadDataExcelConf = beforeUploadDataExcelConf;
        if(uploadTableName == null){
            this.uploadTableName = new String[5];
        }else{
            this.uploadTableName = uploadTableName;
        }
    }
    private String errorMessage;


    public String getErrorMessage() {
        return errorMessage;
    }

    public String[] getUploadTableName() {
        return uploadTableName;
    }

    @Override
    public void run() {
        // 插入临时表
        UploadMoreDataConfDTO uploadMoreDataConf = beforeUploadDataExcelConf.getUploadMoreDataConf();
        String targetTableName, tmpDatasource;
        if(Objects.isNull(uploadTableName[0])){
            targetTableName = uploadMoreDataConf.getConfTargetDatasource();
            uploadTableName[0] = targetTableName;
//            String targetLibName = "";
//            if(targetTableName.contains(".")){
//                String[] targetTableNameSplit = targetTableName.split("\\.");
//                targetLibName = targetTableNameSplit[0] + ".";
//            }
            String onlyKey = UUID.randomUUID().toString();
            tmpDatasource = String.format("%s%s", TMP_TABLE_NAME_PREFIX, onlyKey.replaceAll("-","").substring(0, 20).toUpperCase());
            uploadTableName[1] = tmpDatasource;
            uploadTableName[2] = uploadMoreDataConf.getConfSheetIsCheck().toString();
            uploadTableName[3] = beforeUploadDataExcelConf.getTableDateFieldFormatVal();
            uploadTableName[4] = onlyKey;
            beforeUploadDataExcelConf.setOnlyKey(onlyKey);
        }else{
            targetTableName = uploadTableName[0];
            tmpDatasource = uploadTableName[1];
        }

        List<Map<String, Object>> dataData = beforeUploadDataExcelConf.getDataData();

        SqlSession session = null;
        try {
            session = sqlSessionFactory.openSession(ExecutorType.BATCH);
            DesFormDataBatchUploadMapper batchUploadMapper = session.getMapper(DesFormDataBatchUploadMapper.class);
            if(batchUploadMapper.findTable(tmpDatasource).equals(0)){
                batchUploadMapper.createTempTable(tmpDatasource, targetTableName);
                session.commit();
            }
            List<DesTableColumnDTO> columnDTOs = beforeUploadDataExcelConf.getTableColumns();
            int batchSize = getBatchSize(columnDTOs.size());

            List<DesDataUploadBatchDTO> uploadBatchDTOs = new ArrayList<>();
            for (int num = 0; num < dataData.size(); num++) {
                DesDataUploadBatchDTO dto = new DesDataUploadBatchDTO();
                List<String> columns = new ArrayList<>(dataData.get(num).keySet());
                List<Object> values = new ArrayList<>();
                for (String key : columns) {
                    values.add(typeConversion(columnDTOs, key, dataData.get(num).get(key)));
                }
                dto.setColumns(columns);
                dto.setValues(values);
                uploadBatchDTOs.add(dto);
                if(num != 0 || dataData.size() == 1){
                    if (num % batchSize == 0 || num == (dataData.size() - 1)) {
                        try {
                            batchUploadMapper.insertDataBatch(uploadBatchDTOs, tmpDatasource);
                        } catch (Exception e) {
                            logger.error("批量保存数据错误：错误信息{}数据{}", e.getMessage(), uploadBatchDTOs.size());
                            throw new RuntimeException(String.format("批量保存数据错误：错误信息%s",  e.getMessage()));
                        }
                        //重置
                        uploadBatchDTOs = new ArrayList<>();
                    }
                    if(num % MAX_COMMIT_SIZE == 0 || num == (dataData.size() - 1)){
                        session.commit();
                    }
                }
            }
        }catch (RuntimeException e){
            logger.error("上传数据写入临时表失败，错误信息{}", e.getMessage());
            errorMessage = ObjectUtils.isNotEmpty(e.getMessage())? e.getMessage() : "";
            if(session != null){
                session.rollback();
            }
        }catch (Exception e){
            logger.error("上传数据写入失败，错误信息{}", e.getMessage());
            errorMessage = ObjectUtils.isNotEmpty(e.getMessage())? e.getMessage() : "";
        }finally {
            if (session!=null) {
                session.close();
            }
        }
    }


    /**
     * 获得批量数量
     * @param columnSize 列
     */
    private static int getBatchSize(Integer columnSize) {
        if (columnSize > 0 && columnSize <= COLUMN_NUMBER_10) {
            return 200;
        } else if (columnSize > COLUMN_NUMBER_10 && columnSize <= COLUMN_NUMBER_20) {
            return 150;
        } else if (columnSize > COLUMN_NUMBER_20 && columnSize <= COLUMN_NUMBER_30) {
            return 100;
        } else if (columnSize > COLUMN_NUMBER_30 && columnSize <= COLUMN_NUMBER_40) {
            return 80;
        } else if (columnSize > COLUMN_NUMBER_40 && columnSize <= COLUMN_NUMBER_50) {
            return 60;
        } else {
            return 40;
        }
    }

    /**
     * 转换数据类型
     * @param columnDTOS
     * @param key
     * @param value
     */
    private Object typeConversion(List<DesTableColumnDTO> columnDTOS,String key,Object value) throws Exception{
        List<DesTableColumnDTO> columnDTO = columnDTOS.stream().filter(item -> item.getColumnName().equals(key)).collect(Collectors.toList());
        if(columnDTO.size()>0){
            DesTableColumnDTO dto = columnDTO.get(0);
            switch(dto.getDataType()){
                case "NUMBER":
                    /*return value;*/
                    return ObjectUtils.isNotEmpty(value)? Double.valueOf(value.toString()): "";
                case "DATE":
                    Date date = null;
                    try {
                        if(ObjectUtils.isNotEmpty(value)){
                            SimpleDateFormat simpleFormat = null;
                            if(Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}").matcher(String.valueOf(value)).matches()){
                                simpleFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            }else if(Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}").matcher(String.valueOf(value)).matches()){
                                simpleFormat =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            }else if(Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}").matcher(String.valueOf(value)).matches()){
                                simpleFormat =  new SimpleDateFormat("yyyy-MM-dd");
                            }else if(Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}").matcher(String.valueOf(value)).matches()){
                                simpleFormat =  new SimpleDateFormat("yyyy/MM/dd");
                            }else if(Pattern.compile("\\d{4}\\u5e74\\d{1,2}\\u6708\\d{1,2}\\u65e5?").matcher(String.valueOf(value)).matches()){
                                simpleFormat =  new SimpleDateFormat("yyyy年MM月dd日");
                            }else {
                                simpleFormat =  new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                            }
                            date = simpleFormat.parse(String.valueOf(value));
                        }else{
                            return "";
                        }
                    } catch (ParseException e) {
                        logger.error("时间转换失败，请按照格式填写时间数据（yyyy-MM-dd HH:mm:ss）");
                        throw new Exception("时间转换失败，请按照格式填写时间数据（yyyy-MM-dd HH:mm:ss）");
                    }
                    return date;
                default:
                    return String.valueOf(value);
            }
        }else{
            return String.valueOf(value);
        }
    }
}
