package com.yanhuanxy.multifunexport.tools.origin.base.meta;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.domain.origin.vo.ColumnInfoVo;
import com.yanhuanxy.multifunexport.tools.domain.origin.vo.TableInfoVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 构建创表语句
 * @author yym
 * @date 2021/11/2
 */
public class ParserCreateTableSql {

    /**
     * 构建创建表的sql 语句
     * @return List
     */
    public List<String> convertTableInfoFieldToSql(String dataSourceType, String tableName, String tableComment, TableInfoVo tableInfoVo){
        List<ColumnInfoVo> tableColumns = tableInfoVo.getColumns();
        if(ObjectUtils.isEmpty(tableColumns)){
            throw new RuntimeException("表字段为空，无法构建！");
        }
        List<String> sqlddls;
        switch (dataSourceType){
            case JdbcConstants.MYSQL:
                sqlddls = new ArrayList<>();
                sqlddls.add(mySqlConvertTableInfoField(tableColumns, tableName, tableComment));
                break;
            case JdbcConstants.ORACLE:
                sqlddls = oracleConvertTableInfoField(tableColumns, tableName, tableComment);
                break;
            case JdbcConstants.SQL_SERVER:
                sqlddls = sqlServerConvertTableInfoField(tableColumns, tableName, tableComment);
                break;
            default:
                sqlddls = null;
                break;
        }
        return sqlddls;
    }

    /**
     *构建mysql 的建表语句
     * @return String
     */
    private String mySqlConvertTableInfoField(List<ColumnInfoVo> tableColumns, String tableName, String tableComment){
        List<String> columnFieldSql = new ArrayList<>();
        List<String> priColumnFieldSql = new ArrayList<>();
        tableColumns.forEach(item->{
            int isnull = item.getIsnull();
            String columnname = item.getName();
            String columncomment = item.getComment();
            String columntype = item.getType();
            Boolean isPrimaryKey = item.getIsPrimaryKey();
            StringBuilder tmpFieldSqlbuf = new StringBuilder();
            tmpFieldSqlbuf.append(columnname).append(" ").append(columntype);
            if(isnull == 0){
                tmpFieldSqlbuf.append(" NOT NULL ");
            }else{
                tmpFieldSqlbuf.append(" DEFAULT NULL ");
            }
            if(ObjectUtils.isNotEmpty(columncomment)){
                tmpFieldSqlbuf.append(" COMMENT '").append(columncomment).append("' ");
            }
            if(isPrimaryKey){
                priColumnFieldSql.add(columnname);
            }
            columnFieldSql.add(tmpFieldSqlbuf.toString());
        });

        if(ObjectUtils.isEmpty(columnFieldSql)){
            return null;
        }
        StringBuilder sqlbuffer = new StringBuilder("create table "+ tableName +"(");
        sqlbuffer.append(StringUtils.join(columnFieldSql.toArray(),","));
        if(ObjectUtils.isNotEmpty(priColumnFieldSql)){
            sqlbuffer.append(" ,PRIMARY KEY (").append(StringUtils.join(priColumnFieldSql.toArray(), ",")).append(") USING BTREE ");
        }
        sqlbuffer.append(")");
        sqlbuffer.append("COMMENT = '").append(tableComment).append("'");
        return sqlbuffer.toString();
    }

    /**
     * 构建oracle 的建表语句
     * @return List
     */
    private List<String> oracleConvertTableInfoField(List<ColumnInfoVo> tableColumns, String tableName, String tableComment){
        List<String> columnFieldSql = new ArrayList<>();
        List<String> priColumnFieldSql = new ArrayList<>();
        List<String> columnCommentFieldSql = new ArrayList<>();
        tableColumns.forEach(item->{
            int isnull = item.getIsnull();
            String columnname = item.getName();
            String columncomment = item.getComment();
            String columntype = item.getType();
            Boolean isPrimaryKey = item.getIsPrimaryKey();
            StringBuilder tmpFieldSqlbuf = new StringBuilder();
            tmpFieldSqlbuf.append(columnname).append(" ").append(columntype);
            if(isnull == 0){
                tmpFieldSqlbuf.append(" NOT NULL ");
            }
            columnFieldSql.add(tmpFieldSqlbuf.toString());
            if(ObjectUtils.isNotEmpty(columncomment)){
                columnCommentFieldSql.add("COMMENT ON COLUMN  "+ tableName +"."+ columnname +" IS '"+columncomment+"'");
            }
            if(isPrimaryKey){
                priColumnFieldSql.add(columnname);
            }
        });

        if(ObjectUtils.isEmpty(columnFieldSql)){
            return null;
        }
        StringBuilder sqlbuffer = new StringBuilder("create table "+ tableName +"(");
        sqlbuffer.append(StringUtils.join(columnFieldSql.toArray(),","));
        if(ObjectUtils.isNotEmpty(priColumnFieldSql)){
            sqlbuffer.append(" ,CONSTRAINT ").append(tableName).append(" PRIMARY KEY (").append(StringUtils.join(priColumnFieldSql.toArray(), ",")).append(") ");
        }
        sqlbuffer.append(")");
        List<String> result = new ArrayList<>();
        result.add(sqlbuffer.toString());
        if(ObjectUtils.isNotEmpty(tableComment)){
            sqlbuffer.setLength(0);
            sqlbuffer.append("COMMENT ON TABLE ").append(tableName).append(" IS '").append(tableComment).append("'");
            result.add(sqlbuffer.toString());
        }
        if(ObjectUtils.isNotEmpty(columnCommentFieldSql)){
            result.addAll(columnCommentFieldSql);
        }
        return result;
    }

    /**
     * 构建 sqlserver 的建表语句
     * @return List
     */
    private List<String> sqlServerConvertTableInfoField(List<ColumnInfoVo> tableColumns, String tableName, String tableComment){
        List<String> columnFieldSql = new ArrayList<>();
        List<String> priColumnFieldSql = new ArrayList<>();
        List<String> columnCommentFieldSql = new ArrayList<>();
        tableColumns.forEach(item->{
            int isnull = item.getIsnull();
            String columnname = item.getName();
            String columncomment = item.getComment();
            String columntype = item.getType();
            Boolean isPrimaryKey = item.getIsPrimaryKey();
            StringBuilder tmpFieldSqlbuf = new StringBuilder();
            tmpFieldSqlbuf.append(columnname).append(" ").append(columntype);
            if(isnull == 0){
                tmpFieldSqlbuf.append(" NOT NULL ");
            }
            columnFieldSql.add(tmpFieldSqlbuf.toString());
            if(ObjectUtils.isNotEmpty(columncomment)){
                columnCommentFieldSql.add("EXEC sp_addextendedproperty N'MS_Description', N'"+ columncomment +"',N'SCHEMA', N'dbo',N'TABLE', N'sys_user', N'COLUMN', N'"+ columnname +"'");
            }
            if(isPrimaryKey){
                priColumnFieldSql.add(columnname);
            }
        });

        if(ObjectUtils.isEmpty(columnFieldSql)){
            return null;
        }
        StringBuilder sqlbuffer = new StringBuilder("create table "+ tableName +"(");
        sqlbuffer.append(StringUtils.join(columnFieldSql.toArray(),","));
        if(ObjectUtils.isNotEmpty(priColumnFieldSql)){
            sqlbuffer.append(" ,CONSTRAINT ").append(tableName).append(" PRIMARY KEY (").append(StringUtils.join(priColumnFieldSql.toArray(), ",")).append(") ");
        }
        sqlbuffer.append(")");

        List<String> result = new ArrayList<>();
        result.add(sqlbuffer.toString());
        if(ObjectUtils.isNotEmpty(tableComment)){
            sqlbuffer.setLength(0);
            sqlbuffer.append("EXEC sp_addextendedproperty N'MS_Description', N'").append(tableComment).append("',N'SCHEMA', N'dbo',N'TABLE', N'sys_user'");
            result.add(sqlbuffer.toString());
        }
        if(ObjectUtils.isNotEmpty(columnCommentFieldSql)){
            result.addAll(columnCommentFieldSql);
        }
        return result;
    }
}
