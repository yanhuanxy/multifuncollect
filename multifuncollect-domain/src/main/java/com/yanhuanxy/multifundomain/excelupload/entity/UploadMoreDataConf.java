package com.yanhuanxy.multifundomain.excelupload.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 多表多数据上传配置明细表
 * @date 2021/1/19
 */
public class UploadMoreDataConf implements Serializable {
    private static final long serialVersionUID = 3334143139722053033L;

    /**
     * 编号
     */
    private Long confId;

    /**
     * 主配置编号
     */
    private Long mainId;

    /**
     * 明细名称
     */
    private String confMingxiName;

    /**
     * 文件的名称
     */
    private String confFileName;

    /**
     * 目标表，表示该sheet页上传数据到哪个数据源中
     */
    private String confTargetDatasource;

    /**
     * 目标表，sheet索引
     */
    private Integer confSheetIndex;

    /**
     * 开始单元格X
     */
    private String confSheetXoffset;

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
    private Integer confSheetYoffset;

    /**
     * 开始单元格Y
     */
    private Integer confSheetYoffsetIndex;

    /**
     * sheet页的列的数量
     */
    private Integer confSheetFieldNumber;

    /**
     * 1固定行数 2动态行数 3全量
     */
    private String confSheetRowType;

    /**
     * 读取行数
     */
    private Integer confSheetRowNumber;

    /**
     *  y轴结束单元格下标
     */
    private Integer confSheetYendOffsetIndex;

    /**
     * 1不校验数据 2存量更新、增量插入 3存量不更新、增量插入 4存量更新、增量不插入
     */
    private Integer confSheetIsCheck;

    /**
     * 校验字段，多个以;分割
     */
    private String coofDatasourceField;

    /**
     * 存储数据导入时间的字段
     */
    private String tableUploadDateField;

    /**
     * 存储数据导入时间格式
     */
    private String tableDateFieldFormat;

    /**
     * 存储数据导入时间的字段
     */
    private String tableUploadFields;

    /**
     * 数据按照表字段顺序写入
     */
    private List<String> tableFields;

    public UploadMoreDataConf() {
        super();
    }

    public static class Builder{
        /**
         * 编号
         */
        private Long confId;

        /**
         * 主配置编号
         */
        private Long mainId;

        /**
         * 明细名称
         */
        private String confMingxiName;

        /**
         * 文件的名称
         */
        private String confFileName;

        /**
         * 目标表，表示该sheet页上传数据到哪个数据源中
         */
        private String confTargetDatasource;

        /**
         * 目标表，sheet索引
         */
        private Integer confSheetIndex;

        /**
         * 开始单元格X
         */
        private String confSheetXoffset;

        /**
         * 开始单元格Y
         */
        private Integer confSheetYoffset;

        /**
         * sheet页的列的数量
         */
        private Integer confSheetFieldNumber;

        /**
         * 1固定行数 2动态行数 3全量
         */
        private String confSheetRowType;

        /**
         * 读取行数
         */
        private Integer confSheetRowNumber;

        /**
         * 1不校验数据 2存量更新、增量插入 3存量不更新、增量插入 4存量更新、增量不插入
         */
        private Integer confSheetIsCheck;

        /**
         * 校验字段，多个以;分割
         */
        private String coofDatasourceField;

        public Builder confId(Long confId) {
            this.confId = confId;
            return this;
        }

        public Builder mainId(Long mainId) {
            this.mainId = mainId;
            return this;
        }

        public Builder confMingxiName(String confMingxiName) {
            this.confMingxiName = confMingxiName;
            return this;
        }

        public Builder setConfFileName(String confFileName) {
            this.confFileName = confFileName;
            return this;
        }

        public Builder confTargetDatasource(String confTargetDatasource) {
            this.confTargetDatasource = confTargetDatasource;
            return this;
        }

        public Builder confSheetIndex(Integer confSheetIndex) {
            this.confSheetIndex = confSheetIndex;
            return this;
        }

        public Builder confSheetXoffset(String confSheetXoffset) {
            this.confSheetXoffset = confSheetXoffset;
            return this;
        }

        public Builder confSheetYoffset(Integer confSheetYoffset) {
            this.confSheetYoffset = confSheetYoffset;
            return this;
        }

        public Builder confSheetFieldNumber(Integer confSheetFieldNumber) {
            this.confSheetFieldNumber = confSheetFieldNumber;
            return this;
        }

        public Builder confSheetRowType(String confSheetRowType) {
            this.confSheetRowType = confSheetRowType;
            return this;
        }

        public Builder confSheetRowNumber(Integer confSheetRowNumber) {
            this.confSheetRowNumber = confSheetRowNumber;
            return this;
        }

        public Builder confSheetIsCheck(Integer confSheetIsCheck) {
            this.confSheetIsCheck = confSheetIsCheck;
            return this;
        }

        public Builder coofDatasourceField(String coofDatasourceField) {
            this.coofDatasourceField = coofDatasourceField;
            return this;
        }

        public UploadMoreDataConf build(){
            return new UploadMoreDataConf(this);
        }
    }

    public UploadMoreDataConf(Builder builder) {
        this.confId = builder.confId;
        this.mainId = builder.mainId;
        this.confMingxiName = builder.confMingxiName;
        this.confFileName = builder.confFileName;
        this.confTargetDatasource = builder.confTargetDatasource;
        this.confSheetIndex = builder.confSheetIndex;
        this.confSheetXoffset = builder.confSheetXoffset;
        this.confSheetYoffset = builder.confSheetYoffset;
        this.confSheetFieldNumber = builder.confSheetFieldNumber;
        this.confSheetRowType = builder.confSheetRowType;
        this.confSheetRowNumber = builder.confSheetRowNumber;
        this.confSheetIsCheck = builder.confSheetIsCheck;
        this.coofDatasourceField = builder.coofDatasourceField;
    }

    public Long getConfId() {
        return confId;
    }

    public void setConfId(Long confId) {
        this.confId = confId;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getConfMingxiName() {
        return confMingxiName;
    }

    public void setConfMingxiName(String confMingxiName) {
        this.confMingxiName = confMingxiName;
    }

    public String getConfFileName() {
        return confFileName;
    }

    public void setConfFileName(String confFileName) {
        this.confFileName = confFileName;
    }

    public String getConfTargetDatasource() {
        return confTargetDatasource;
    }

    public void setConfTargetDatasource(String confTargetDatasource) {
        this.confTargetDatasource = confTargetDatasource;
    }

    public Integer getConfSheetIndex() {
        return confSheetIndex;
    }

    public void setConfSheetIndex(Integer confSheetIndex) {
        this.confSheetIndex = confSheetIndex;
    }

    public String getConfSheetXoffset() {
        return confSheetXoffset;
    }

    public void setConfSheetXoffset(String confSheetXoffset) {
        this.confSheetXoffset = confSheetXoffset;
    }

    public Integer getConfSheetYoffset() {
        return confSheetYoffset;
    }

    public void setConfSheetYoffset(Integer confSheetYoffset) {
        this.confSheetYoffset = confSheetYoffset;
    }

    public Integer getConfSheetFieldNumber() {
        return confSheetFieldNumber;
    }

    public void setConfSheetFieldNumber(Integer confSheetFieldNumber) {
        this.confSheetFieldNumber = confSheetFieldNumber;
    }

    public String getConfSheetRowType() {
        return confSheetRowType;
    }

    public void setConfSheetRowType(String confSheetRowType) {
        this.confSheetRowType = confSheetRowType;
    }

    public Integer getConfSheetRowNumber() {
        return confSheetRowNumber;
    }

    public void setConfSheetRowNumber(Integer confSheetRowNumber) {
        this.confSheetRowNumber = confSheetRowNumber;
    }

    public Integer getConfSheetIsCheck() {
        return confSheetIsCheck;
    }

    public void setConfSheetIsCheck(Integer confSheetIsCheck) {
        this.confSheetIsCheck = confSheetIsCheck;
    }

    public String getCoofDatasourceField() {
        return coofDatasourceField;
    }

    public void setCoofDatasourceField(String coofDatasourceField) {
        this.coofDatasourceField = coofDatasourceField;
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

    public String getTableUploadDateField() {
        return tableUploadDateField;
    }

    public void setTableUploadDateField(String tableUploadDateField) {
        this.tableUploadDateField = tableUploadDateField;
    }

    public List<String> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<String> tableFields) {
        this.tableFields = tableFields;
    }

    public String getTableDateFieldFormat() {
        return tableDateFieldFormat;
    }

    public void setTableDateFieldFormat(String tableDateFieldFormat) {
        this.tableDateFieldFormat = tableDateFieldFormat;
    }

    public String getTableUploadFields() {
        return tableUploadFields;
    }

    public void setTableUploadFields(String tableUploadFields) {
        this.tableUploadFields = tableUploadFields;
    }
}
