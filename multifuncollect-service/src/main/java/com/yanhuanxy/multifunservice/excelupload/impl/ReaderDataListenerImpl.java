package com.yanhuanxy.multifunservice.excelupload.impl;

import com.yanhuanxy.multifuncommon.enums.ReadEventTypeEnum;
import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadDataExcelDTO;
import com.yanhuanxy.multifunexport.tools.util.ThreadPoolUtils;
import com.yanhuanxy.multifunservice.excelupload.listen.ReaderDataEvent;
import com.yanhuanxy.multifunservice.excelupload.listen.ReaderListener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

public class ReaderDataListenerImpl implements ReaderListener {
    private static final Logger logger = LoggerFactory.getLogger(ReaderDataListenerImpl.class);

    /**
     * 写入服务
     */
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 操作表
     */
    private final DesFormDataBatchUploadMapper desFormDataBatchUploadMapper;

    private final List<String[]> storageStrategy = new ArrayList<>();

    public List<String[]> getStorageStrategy() {
        return this.storageStrategy;
    }

    public ReaderDataListenerImpl(SqlSessionFactory sqlSessionFactory, DesFormDataBatchUploadMapper desFormDataBatchUploadMapper) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.desFormDataBatchUploadMapper = desFormDataBatchUploadMapper;
    }

    /**
     * 写入数据
     */
    public void insertData(ReaderDataEvent readerDataEvent){
        ReadEventTypeEnum eventTypeEnum = readerDataEvent.getEventTypeEnum();
        logger.info("触发数据写入事件类型-> {}: {}", eventTypeEnum.toString(), eventTypeEnum.getMessage());
        insertToExcelRowData(readerDataEvent.getBeforeUploadDataExcelConf());
    }

    /**
     * TODO 考虑多线程
     *
     *  *     // 环形算法 满了则通知线程等待
     *  *     // 当线程都完成后继续读取
     *  *     //  XXX  线程1  当X段满 则通知线程1写入 完成 -> 重复流程
     *  *     //  BBB  线程2  当B段满 则通知线程2写入 完成 -> 重复流程
     *  *     //  YYY  线程3  当Y段满 则通知线程3写入 完成 -> 重复流程
     * 将数据写入数据库
     */
    private void insertToExcelRowData(UploadDataExcelDTO beforeUploadDataExcelConf){
        String onlyKey = beforeUploadDataExcelConf.getOnlyKey();
        String[] tmpStorageStrategy = null;
        boolean hasOnlyKey = Objects.nonNull(onlyKey);
        if(hasOnlyKey){
            tmpStorageStrategy = storageStrategy.stream().filter(item-> Objects.equals(item[4], onlyKey)).findAny().orElse(new String[5]);
        }

        ExecuteInsertDataThread executeInsertDataThread = new ExecuteInsertDataThread(sqlSessionFactory, beforeUploadDataExcelConf, tmpStorageStrategy);
        ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();
        ThreadFactory threadFactory = threadPool.getThreadFactory();
        Thread insertDataThread = threadFactory.newThread(executeInsertDataThread);
        insertDataThread.start();
        try {
            insertDataThread.join();
        } catch (InterruptedException e) {
            logger.error("数据写入线程异常！", e);
        }
        // 处理信息
        String errorMessage = executeInsertDataThread.getErrorMessage();
        if(Objects.nonNull(errorMessage)){
            storageStrategy.stream().map(item-> item[1]).forEach(desFormDataBatchUploadMapper::dropTable);
            throw new BaseRuntimeException("数据写入失败！" + errorMessage);
        }
        if(!hasOnlyKey){
            storageStrategy.add(executeInsertDataThread.getUploadTableName());
        }
    }
}
