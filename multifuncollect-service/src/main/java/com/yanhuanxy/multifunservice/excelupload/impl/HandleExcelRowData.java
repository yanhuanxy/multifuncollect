package com.yanhuanxy.multifunservice.excelupload.impl;

import com.yanhuanxy.multifuncommon.enums.ReadEventTypeEnum;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadDataExcelDTO;
import com.yanhuanxy.multifunexport.tools.excel.read.IExcelRowReader;
import com.yanhuanxy.multifunservice.excelupload.listen.ReaderDataEvent;
import com.yanhuanxy.multifunservice.excelupload.listen.ReaderListener;
import com.yanhuanxy.multifunservice.excelupload.listen.ReaderListenerAble;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HandleExcelRowData implements IExcelRowReader, ReaderListenerAble {

    /**
     * 配置
     */
    private final List<UploadDataExcelDTO> uploadDataExcels;


    private static final String REPLACE_FIELD_VAL = "\"-\"";

    /**
     * 所有数据标记
     */
    private static final int ALL_ROW_FLAG = -1;

    /**
     * 读取事件监听
     */
    private ReaderListener readerDataListener;

    /**
     * 注册监听
     * @param readerDataListener 监听器
     */
    @Override
    public void registerListener(ReaderListener readerDataListener) {
        this.readerDataListener = readerDataListener;
    }

    /**
     * 触发监听事件
     * @param readerDataEvent 监听事件
     */
    @Override
    public void triggerReadListener(ReaderDataEvent readerDataEvent) {
        if(readerDataListener == null){
            return;
        }
        this.readerDataListener.insertData(readerDataEvent);
    }

    /**
     * 容器最大容量
     */
    private static final int MAX_CONTAINER_SIZE = 18000;

    private Integer beforeSheetIndex;

    private Integer beforeUploadConfId;

    private UploadDataExcelDTO beforeUploadDataExcelConf;

    public HandleExcelRowData(List<UploadDataExcelDTO> uploadDataExcels) {
        this.uploadDataExcels = uploadDataExcels;
    }

    @Override
    public void getRows(int sheetIndex, int curRow, List<String> rowList) {

        uploadDataExcels.stream().filter(confItem->{
            Integer confSheetYoffsetIndex = confItem.getConfSheetYoffsetIndex();
            Integer confSheetYendOffsetIndex = confItem.getConfSheetYendOffsetIndex();
            return Objects.equals(sheetIndex, confItem.getConfSheetIndex()) && curRow >= confSheetYoffsetIndex && ( curRow <= confSheetYendOffsetIndex || confSheetYendOffsetIndex == -1);
        }).forEach(confItem->{
            Integer confSheetYendOffsetIndex = confItem.getConfSheetYendOffsetIndex();
            int curDataSize = confItem.getDataData().size();
            if(Objects.isNull(beforeUploadDataExcelConf)){
                beforeSheetIndex = sheetIndex;
                beforeUploadConfId = confItem.getUploadMoreDataConf().getConfId();
                beforeUploadDataExcelConf = confItem;
            }
            // 填入数据
            Integer startXindex = confItem.getConfSheetXoffsetIndex();
            Integer endXindex = confItem.getConfSheetXendOffsetIndex();
            int rowlen = rowList.size();
            if(rowlen > startXindex){
                List<String> titleData = confItem.getTitleData();
                Map<String, Object> resultData = new LinkedHashMap<>(16);
                if(rowlen < endXindex){
                    endXindex = rowList.size();
                }
                List<String> tmpdatalist = rowList.subList(startXindex, endXindex);
                int tmplen = tmpdatalist.size();
                for(int i=0; i < titleData.size(); i++){
                    resultData.put(titleData.get(i), i < tmplen ? REPLACE_FIELD_VAL.equals(tmpdatalist.get(i)) ? "-" : tmpdatalist.get(i) : "");
                }
                if(confItem.getTableUploadDateField() != null){
                    resultData.put(confItem.getTableUploadDateField(), confItem.getTableDateFieldFormatVal());
                }
                List<Map<String, Object>> dataData = confItem.getDataData();
                if(dataData.size() == MAX_CONTAINER_SIZE){
                    //数据量 大于 MAX_CONTAINER_SIZE 先写入
                    insertToExcelRowData();
                }
                confItem.getDataData().add(resultData);
            }

            if(curDataSize == MAX_CONTAINER_SIZE ){
                // dataRow = 内存 maxRow -> 写入数据
                insertToExcelRowData(confItem, ReadEventTypeEnum.MAX_MAX_ROW);
            }else if(confSheetYendOffsetIndex != ALL_ROW_FLAG && curRow == confSheetYendOffsetIndex){
                // curRow = 配置 maxRow -> 写入数据
                insertToExcelRowData(confItem, ReadEventTypeEnum.CUR_MAX_ROW);
            }else if(!Objects.equals(beforeSheetIndex, sheetIndex) || !Objects.equals(beforeUploadConfId, confItem.getUploadMoreDataConf().getConfId())){
                // sheet 更改 -> 数据写入
                // 最后一次结束？ 手动触发 根据数据触发
                // 1、sheetIndex 变更
                // 2、配置变更
                insertToExcelRowData(beforeUploadDataExcelConf, ReadEventTypeEnum.CHANGE_CONF);

                beforeSheetIndex = sheetIndex;
                beforeUploadConfId = confItem.getUploadMoreDataConf().getConfId();
                beforeUploadDataExcelConf = confItem;
            }
        });
    }

    /**
     * 手动触发执行插入操作监听
     */
    public void insertToExcelRowData(){
        uploadDataExcels.stream().filter(item-> !item.getDataData().isEmpty()).forEach(item->{
            insertToExcelRowData(item, ReadEventTypeEnum.LAST_HANDLE);
        });
    }

    /**
     * 触发执行插入操作监听
     * @param uploadDataExcelDTO 配置
     * @param eventTypeEnum 事件类型
     */
    public void insertToExcelRowData(UploadDataExcelDTO uploadDataExcelDTO, ReadEventTypeEnum eventTypeEnum){
        ReaderDataEvent readerDataEvent = new ReaderDataEvent();
        readerDataEvent.setBeforeUploadDataExcelConf(uploadDataExcelDTO);
        readerDataEvent.setEventTypeEnum(eventTypeEnum);
        this.triggerReadListener(readerDataEvent);
        // 执行完毕清除数据
        uploadDataExcelDTO.getDataData().clear();
    }
}
