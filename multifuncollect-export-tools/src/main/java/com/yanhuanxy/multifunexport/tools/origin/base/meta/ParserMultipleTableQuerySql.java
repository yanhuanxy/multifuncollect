package com.yanhuanxy.multifunexport.tools.origin.base.meta;

import com.yanhuanxy.multifunexport.tools.domain.origin.dto.QueryParamDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.TableAliasDto;
import com.yanhuanxy.multifunexport.tools.domain.emuns.origin.BiOperateTypeEnum;
import com.yanhuanxy.multifunexport.tools.domain.emuns.origin.JoinTypeEnum;
import com.yanhuanxy.multifunexport.tools.domain.origin.vo.ColumnInfoVo;
import com.yanhuanxy.multifunexport.tools.domain.origin.vo.QueryParameterVo;
import com.yanhuanxy.multifunexport.tools.domain.origin.vo.QueryTableMultDataVo;
import com.yanhuanxy.multifunexport.tools.domain.origin.vo.QueryTableMultSubDataVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 组装多表联查的sql语句
 * @author yym
 * @date 2021/11/2
 */
public class ParserMultipleTableQuerySql {
    /**
     * 显示字段
     */
    private List<String> fields = new ArrayList<>();
    /**
     * 筛选条件
     */
    private List<String> filters = new ArrayList<>();

    /**
     * 排序字段
     */
    private List<String> orders = new ArrayList<>();

    /**
     * 筛选条件的值
     */
    private List<Object> whereparams = new ArrayList<>();

    /**
     * 表别名 对照 表名 以及含有的字段信息
     */
    private List<TableAliasDto> tableAliasDtos = new ArrayList<>();

    /**
     * sql 源表信息
     */
    private List<String> tablesource = new ArrayList<>();

    public List<String> getFilters() {
        return filters;
    }

    public List<Object> getWhereparams() {
        return whereparams;
    }

    public List<TableAliasDto> getTableAliasDtos() {
        return tableAliasDtos;
    }

    public void init(QueryTableMultDataVo multDataVo) throws Exception{
        int defaultalias = 65;
        char defaultchar = (char) defaultalias;
        /*
         * 1、处理主表
         */
        String tableName = multDataVo.getTableName();
        List<ColumnInfoVo> fieldInfos = multDataVo.getFields();
        String firstalias = String.valueOf(defaultchar);
        /*
         * 主表拥有的字段
         **/
        List<String> tablefields = new ArrayList<>();
        convertFieldByColumnInfoVo(fieldInfos, tablefields, firstalias);

        List<QueryParameterVo> parameters = multDataVo.getParameters();
        convertWhereFieldByQueryParameterVo(parameters,firstalias);
        convertWhereFieldByQueryParameterVo(multDataVo.getParameterStrs());
        tableAliasDtos.add(new TableAliasDto(tableName,firstalias,defaultalias,tablefields));
        String firstTableSource = tableName + " "+ firstalias;
        tablesource.add(firstTableSource);
        /*
         * 2、处理子表
         **/
        List<QueryTableMultSubDataVo> childrenTable = multDataVo.getChildrenTable();
        initChildrenAggregate(childrenTable, defaultalias);
    }

    /**
     * 解析子表的信息
     *
     */
    private void initChildrenAggregate(List<QueryTableMultSubDataVo> childrenTable,int defaultalias){
        if(ObjectUtils.isEmpty(childrenTable)){
            return;
        }
        /*
         * 预先生成子表的别名以及下标
         */
        for(int i=0,len = childrenTable.size(); i<len; i++){
            int tmpalias = defaultalias + i + 1;
            QueryTableMultSubDataVo queryTableMultSubDataVo = childrenTable.get(i);
            char defaultchar = (char) tmpalias;
            tableAliasDtos.add(new TableAliasDto(queryTableMultSubDataVo.getTableName(),String.valueOf(defaultchar),tmpalias));
        }
        /*
         * 处理子表
         */
        for(QueryTableMultSubDataVo tmpsubdata : childrenTable){
            defaultalias++;
            TableAliasDto tableAliasDto = getTableAliasDtoByIndex(defaultalias);
            if(tableAliasDto == null || ObjectUtils.isEmpty(tableAliasDto)){
                continue;
            }
            // 获取当前表的别名
            String tmptablealias = tableAliasDto.getTableAlias();
            String tmpTableSource = getTableSorceByMultSubData(tmpsubdata, tmptablealias);
            tablesource.add(tmpTableSource);

            List<String> tablefields = new ArrayList<>();
            List<ColumnInfoVo> childrenFieldInfo = tmpsubdata.getFields();
            convertFieldByColumnInfoVo(childrenFieldInfo, tablefields, tmptablealias);
            List<QueryParameterVo> childrenparamets = tmpsubdata.getParameters();
            convertWhereFieldByQueryParameterVo(childrenparamets,tmptablealias);
            tableAliasDto.setFields(tablefields);
        }
    }

    /**
     * 获取表关联 关键字
     * @param joinTypeEnum 关联类型
     * @return String
     */
    private String getJoinTypeByjoinTypeEnum(JoinTypeEnum joinTypeEnum){
        String result = "LEFT JOIN";
        switch (joinTypeEnum){
            case LEFTJOIN:
                result = "LEFT JOIN";
                break;
            case RIGHTJOIN:
                result = "RIGHT JOIN";
                break;
            case INNERJOIN:
                result = "INNER JOIN";
                break;
            case FULLJOIN:
                result = "FULL JOIN";
                break;
            case CROSSJOIN:
                result = "CROSS JOIN";
                break;
            case OTHER:
                break;
        }
        return result;
    }

    /**
     * 拼接关联关系以及数据源
     * @param tmpsubdata 从表数据集合
     * @param tmptablealias 从表的表别名
     * @return String
     */
    private String getTableSorceByMultSubData(QueryTableMultSubDataVo tmpsubdata, String tmptablealias){
        String tmptablename = tmpsubdata.getTableName();
        //关联表名
        String beforeTableName = tmpsubdata.getBeforeTableName();
        //关联表别名
        String beforetablealias = getTableAliasByTableName(beforeTableName);

        Integer joinType = tmpsubdata.getJoinType();
        JoinTypeEnum joinTypeEnum = JoinTypeEnum.getJoinType(joinType);
        //关联关系关键字
        String joinKeyWord = getJoinTypeByjoinTypeEnum(joinTypeEnum);

        List<String> beforeJoinFields = tmpsubdata.getBeforeJoinFields();
        List<String> afterJoinFields = tmpsubdata.getAfterJoinFields();
        StringBuilder result = new StringBuilder();
        // 拼接关联关系 相当于数据源
        result.append(joinKeyWord).append(" ").append(tmptablename).append(" ").append(tmptablealias).append(" on ");
        int beforesize = beforeJoinFields.size(),aftersize = afterJoinFields.size();
        IntStream.range(0,beforesize).forEach(index->{
            String beforefield = beforeJoinFields.get(index);
            if(aftersize< index + 1){
                return;
            }
            String aftrfield = afterJoinFields.get(index);
            if(index > 0){
                result.append(" and ");
            }
            result.append(beforetablealias).append(".").append(beforefield).append(" = ")
                    .append(tmptablealias).append(".").append(aftrfield);
        });
        return result.toString();
    }

    /**
     * 通过表名获取表别名信息 没有则为空
     * @param nowalias 表名
     * @return String
     */
    private TableAliasDto getTableAliasDtoByIndex(int nowalias){
        TableAliasDto tableAliasDto = tableAliasDtos.stream().filter(item -> Objects.equals(item.getTableIndex(), nowalias) ).findAny().orElse(null);
        if(ObjectUtils.isEmpty(tableAliasDto)){
            return null;
        }
        return tableAliasDto;
    }

    /**
     * 通过表名获取表别名信息 没有则为空
     * @param tableName 表名
     * @return String
     */
    private String getTableAliasByTableName(String tableName){
        TableAliasDto tableAliasDto = tableAliasDtos.stream().filter(item -> Objects.equals(item.getTableName(), tableName)).findAny().orElse(new TableAliasDto());
        if(ObjectUtils.isEmpty(tableAliasDto.getTableName())){
            return "";
        }
        return tableAliasDto.getTableAlias();
    }


    /**
     * 根据字段信息vo转换成sql字段片段
     * @param fieldInfos 字段集合
     * @param tablefields 字段名称
     * @param tablealias 表别名
     */
    private void convertFieldByColumnInfoVo(List<ColumnInfoVo> fieldInfos, List<String> tablefields,String tablealias){
        if(ObjectUtils.isEmpty(fieldInfos)){
            fields.add(tablealias + ".*");
            return;
        }
        String symbol = "_";
        for(ColumnInfoVo columnInfoVo : fieldInfos){
            /*
             * 添加含有字段
             **/
            String tablefield = columnInfoVo.getName();
            tablefields.add(tablefield);
            /*
             *添加显示字段
             **/
            String tmpTableField = tablealias+ "." + tablefield;
            String tmpAliasTableField =  tmpTableField + " as " + tablefield + symbol + tablealias + symbol;
            fields.add(tmpAliasTableField);
            /*
             * 添加排序字段
             **/
            Boolean sort = columnInfoVo.getSort();
            if(ObjectUtils.isNotEmpty(sort)){
                String sortTableField = tmpTableField;
                if(sort){
                    sortTableField+= " ASC ";
                }else{
                    sortTableField+= " DESC ";
                }
                orders.add(sortTableField);
            }
        }
    }

    /**
     * 根据筛选条件vo转换成sqlwhere条件片段
     * @param params 查询条件集合
     * @param tablealias 表别名
     */
    private void convertWhereFieldByQueryParameterVo(List<QueryParameterVo> params, String tablealias){
        if(ObjectUtils.isEmpty(params)){
            return;
        }
        /*
         * 添加筛选条件 且 添加筛选条件的值
         **/
        List<QueryParamDto> queryParamDtos = convertQueryParameterVoToDto(params);
        for(QueryParamDto queryParameterDto : queryParamDtos){
            String fieldName = parserQueryParamDto(queryParameterDto, whereparams);
            if(ObjectUtils.isNotEmpty(fieldName)){
                String tmpFilter = tablealias + "." + fieldName;
                filters.add(tmpFilter);
            }
        }
    }
    /**
     * 直接添加条件字符串
     * @param params 查询条件集合
     */
    private void convertWhereFieldByQueryParameterVo(List<String> params){
        if(ObjectUtils.isEmpty(params)){
            return;
        }
        filters.addAll(params);
    }
    /**
     * Vo 转换成 dto
     * @param whereparameters vo
     * @return list
     */
    public static List<QueryParamDto> convertQueryParameterVoToDto(List<QueryParameterVo> whereparameters){
        if(ObjectUtils.isEmpty(whereparameters)){
            return new ArrayList<>();
        }
        return whereparameters.stream().filter(item-> !BiOperateTypeEnum.OTHER.equals(item.getBiOperateTypeEnum())).map(item->{
            QueryParamDto queryParamDto = new QueryParamDto();
            queryParamDto.setFieldName(item.getFieldName());
            BiOperateTypeEnum biOperateTypeEnum = item.getBiOperateTypeEnum();
            queryParamDto.setOperateType(biOperateTypeEnum.getSymbolCode());
            if(ObjectUtils.isEmpty(item.getFieldVal())){
                queryParamDto.setFieldVal("");
            }else{
                queryParamDto.setFieldVal(item.getFieldVal());
            }
            queryParamDto.setFieldValSecond(item.getFieldValSecond());
            return queryParamDto;
        }).collect(Collectors.toList());
    }

    /**
     * 转换查询配置 成 where sql 片段
     * @param queryParamDto 查询配置
     * @param tmpVals 值集合
     * @return String
     */
    public static String parserQueryParamDto(QueryParamDto queryParamDto, List<Object> tmpVals){
        String operateType = queryParamDto.getOperateType();
        String tmpVal = "";
        if(operateType.contains("like")){
            if("likeX%".equals(operateType)){
                tmpVal = queryParamDto.getFieldVal() + "%";
            }else if("like%X".equals(operateType)){
                tmpVal = "%" + queryParamDto.getFieldVal();
            }else{
                tmpVal = "%" + queryParamDto.getFieldVal() + "%";
            }
            tmpVals.add(tmpVal);
            if("not like".equals(operateType)){
                return queryParamDto.getFieldName() + " NOT LIKE  ? ";
            }else{
                return queryParamDto.getFieldName() + " LIKE  ? ";
            }
        }else if(operateType.contains("null")){
            return queryParamDto.getFieldName() + " " + operateType;
        }else if(operateType.contains("Empty")){
            if("isEmpty".equals(operateType)){
                return queryParamDto.getFieldName() + " = ''";
            }
            return queryParamDto.getFieldName() + " <> ''";
        }else if(operateType.contains("between")){
            if(ObjectUtils.isNotEmpty(queryParamDto.getFieldVal()) && ObjectUtils.isNotEmpty(queryParamDto.getFieldValSecond())){
                tmpVals.add(queryParamDto.getFieldVal());
                tmpVals.add(queryParamDto.getFieldValSecond());
                return queryParamDto.getFieldName() + " " + queryParamDto.getOperateType() + " ? AND ?";
            }
        }else if(operateType.contains("in")){
            if(ObjectUtils.isNotEmpty(queryParamDto.getFieldVal())){
                String fieldVal = String.valueOf(queryParamDto.getFieldVal());
                String[] tmpfieldVals = fieldVal.split(",");
                String tmpfieldVal = StringUtils.join(tmpfieldVals, "','");
                return queryParamDto.getFieldName() + " " + queryParamDto.getOperateType() + " ('"+ tmpfieldVal +"') ";
            }
        }else{
            tmpVals.add(queryParamDto.getFieldVal());
            return queryParamDto.getFieldName() + " " + queryParamDto.getOperateType() + " ? ";
        }
        return tmpVal;
    }

    /**
     * 根据所有的条件组装sql
     * @return StringBuilder
     */
    public StringBuilder getQueryMultipleTableDataSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append(StringUtils.join(fields.toArray(),","))
                .append(" from ").append(StringUtils.join(tablesource.toArray()," "));
        if(ObjectUtils.isNotEmpty(filters)){
            sql.append(" where ").append(StringUtils.join(filters.toArray()," and "));
        }
        if(ObjectUtils.isNotEmpty(orders)){
            sql.append(" order by ").append(StringUtils.join(orders.toArray(),", "));
        }
        return sql;
    }
}
