package com.yanhuanxy.multifundomain.excelupload.dto;

import com.yanhuanxy.multifundomain.desfrom.dto.DesTableColumnDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UploadDataExcelDTO implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UploadDataExcelDTO.class);

    /**
     * 配置唯一键
     */
    private String onlyKey;

    /**
     * sheet页下标
     */
    private Integer confSheetIndex;

    /**
     * x轴开始单元格下标
     */
    private Integer confSheetXoffsetIndex;

    /**
     *  x轴结束单元格下标
     */
    private Integer confSheetXendOffsetIndex;

    /**
     * 开始单元格Y
     */
    private Integer confSheetYoffsetIndex;

    /**
     *  y轴结束单元格下标
     */
    private Integer confSheetYendOffsetIndex;

    /**
     * 存储数据导入时间的字段
     */
    private String tableUploadDateField;

    /**
     * 存储数据导入时间格式的值
     */
    private String tableDateFieldFormatVal;

    /**
     * 表头
     */
    private List<String> titleData = new ArrayList<>();

    /**
     * 表字段信息
     */
    private List<DesTableColumnDTO> tableColumns = new ArrayList<>();

    /**
     * 数据
     */
    private List<Map<String,Object>> dataData = new LinkedList<>();

    /**
     * 配置
     */
    private UploadMoreDataConfDTO uploadMoreDataConf;



    /**
     * 表格是否存在
     */
    private boolean tableIsExist = true;


    public Integer getConfSheetIndex() {
        return confSheetIndex;
    }

    public void setConfSheetIndex(Integer confSheetIndex) {
        this.confSheetIndex = confSheetIndex;
    }

    public Integer getConfSheetXoffsetIndex() {
        return confSheetXoffsetIndex;
    }

    public void setConfSheetXoffsetIndex(Integer confSheetXoffsetIndex) {
        this.confSheetXoffsetIndex = confSheetXoffsetIndex;
    }

    public Integer getConfSheetXendOffsetIndex() {
        return confSheetXendOffsetIndex;
    }

    public void setConfSheetXendOffsetIndex(Integer confSheetXendOffsetIndex) {
        this.confSheetXendOffsetIndex = confSheetXendOffsetIndex;
    }

    public Integer getConfSheetYoffsetIndex() {
        return confSheetYoffsetIndex;
    }

    public void setConfSheetYoffsetIndex(Integer confSheetYoffsetIndex) {
        this.confSheetYoffsetIndex = confSheetYoffsetIndex;
    }

    public Integer getConfSheetYendOffsetIndex() {
        return confSheetYendOffsetIndex;
    }

    public void setConfSheetYendOffsetIndex(Integer confSheetYendOffsetIndex) {
        this.confSheetYendOffsetIndex = confSheetYendOffsetIndex;
    }

    public List<String> getTitleData() {
        return titleData;
    }

    public void setTitleData(List<String> titleData) {
        this.titleData = titleData;
    }

    public List<DesTableColumnDTO> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<DesTableColumnDTO> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public List<Map<String,Object>> getDataData() {
        return dataData;
    }

    public void setDataData(List<Map<String,Object>> dataData) {
        this.dataData = dataData;
    }

    public UploadMoreDataConfDTO getUploadMoreDataConf() {
        return uploadMoreDataConf;
    }

    public void setUploadMoreDataConf(UploadMoreDataConfDTO uploadMoreDataConf) {
        this.uploadMoreDataConf = uploadMoreDataConf;
    }

    public boolean isTableIsExist() {
        return tableIsExist;
    }

    public void setTableIsExist(boolean tableIsExist) {
        this.tableIsExist = tableIsExist;
    }

    public String getTableUploadDateField() {
        return tableUploadDateField;
    }

    public void setTableUploadDateField(String tableUploadDateField) {
        this.tableUploadDateField = tableUploadDateField;
    }

    public String getTableDateFieldFormatVal() {
        return tableDateFieldFormatVal;
    }

    public void setTableDateFieldFormatVal(String tableDateFieldFormatVal) {
        this.tableDateFieldFormatVal = tableDateFieldFormatVal;
    }

    public String getOnlyKey() {
        return onlyKey;
    }

    public void setOnlyKey(String onlyKey) {
        this.onlyKey = onlyKey;
    }

    /**
     * 深拷贝 UploadDataExcelDTO
     */
    public UploadDataExcelDTO cloneUploadDataExcel() {
        try{
            ByteArrayOutputStream byteArrayOs = new ByteArrayOutputStream();
            ObjectOutputStream objectOs = new ObjectOutputStream(byteArrayOs);
            objectOs.writeObject(this);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(byteArrayOs.toByteArray());
            ObjectInputStream objectIs = new ObjectInputStream(byteArrayIs);
            return (UploadDataExcelDTO) objectIs.readObject();
        }catch (IOException | ClassNotFoundException e){
            logger.error(" 深拷贝 UploadDataExcelDTO 对象异常");
        }
        return null;
    }
}
