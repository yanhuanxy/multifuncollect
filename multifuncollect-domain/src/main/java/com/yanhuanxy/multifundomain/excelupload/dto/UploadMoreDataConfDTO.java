package com.yanhuanxy.multifundomain.excelupload.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 多表多数据上传配置明细表
 * @date 2021/1/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
public class UploadMoreDataConfDTO {

    /**
     * 编号
     */
    private Integer confId;

    /**
     * 主配置编号
     */
    private Integer mainId;

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
    private Integer confSheetRowType;

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

    /**
     * 存储数据导入时间的字段
     */
    private String tableUploadDateField;

    /**
     * 存储数据导入时间的字段
     */
    private String tableDateFieldFormat;

    /**
     * 数据按照表字段顺序写入
     */
    private List<String> tableFields;



    public UploadMoreDataConfDTO() {
        super();
    }

    public static class Builder{
        /**
         * 编号
         */
        private Integer confId;

        /**
         * 主配置编号
         */
        private Integer mainId;

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
        private Integer confSheetRowType;

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

        public Builder confId(Integer confId) {
            this.confId = confId;
            return this;
        }

        public Builder mainId(Integer mainId) {
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

        public Builder confSheetRowType(Integer confSheetRowType) {
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

        public UploadMoreDataConfDTO build(){
            return new UploadMoreDataConfDTO(this);
        }
    }

    public UploadMoreDataConfDTO(Builder builder) {
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

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }

    public Integer getMainId() {
        return mainId;
    }

    public void setMainId(Integer mainId) {
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

    public Integer getConfSheetRowType() {
        return confSheetRowType;
    }

    public void setConfSheetRowType(Integer confSheetRowType) {
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

    public List<String> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<String> tableFields) {
        this.tableFields = tableFields;
    }

    public String getTableUploadDateField() {
        return tableUploadDateField;
    }

    public void setTableUploadDateField(String tableUploadDateField) {
        this.tableUploadDateField = tableUploadDateField;
    }

    public String getTableDateFieldFormat() {
        return tableDateFieldFormat;
    }

    public void setTableDateFieldFormat(String tableDateFieldFormat) {
        this.tableDateFieldFormat = tableDateFieldFormat;
    }
}
