package com.yanhuanxy.multifunexport.tools.exception.origin;


import com.yanhuanxy.multifunexport.tools.constant.origin.Constants;
import com.yanhuanxy.multifunexport.tools.constant.origin.DBUtilErrorCode;
import com.yanhuanxy.multifunexport.tools.constant.origin.ErrorCode;
import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;

/**
 * RdbmsException
 *
 * @author yym
 * @date 2020/08/27
 */
public class RdbmsException extends DbException {

    private static final String EXECUTE_SQLMESSAGE = " 执行的SQL为: ";

    private static final String EXECUTE_ERRORMESSAGE = " 具体错误信息为：";

    private static final String EXECUTE_USERNAME = " 用户名为: ";

    private static final String EXECUTE_JDBCURL = " jdbcURL为：";

    public RdbmsException(ErrorCode errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public static DbException asConnException(String dataBaseType, Exception e, String userName, String dbName){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            DBUtilErrorCode dbUtilErrorCode = mySqlConnectionErrorAna(e.getMessage());
            if (dbUtilErrorCode == DBUtilErrorCode.MYSQL_CONN_DB_ERROR && dbName !=null ){
                return DbException.asDbException(dbUtilErrorCode,"该数据库名称为："+dbName+EXECUTE_ERRORMESSAGE+e);
            }
            if (dbUtilErrorCode == DBUtilErrorCode.MYSQL_CONN_USERPWD_ERROR ){
                return DbException.asDbException(dbUtilErrorCode,"该数据库用户名为："+userName+EXECUTE_ERRORMESSAGE+e);
            }
            return DbException.asDbException(dbUtilErrorCode,EXECUTE_ERRORMESSAGE+e);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            DBUtilErrorCode dbUtilErrorCode = oracleConnectionErrorAna(e.getMessage());
            if (dbUtilErrorCode == DBUtilErrorCode.ORACLE_CONN_DB_ERROR && dbName != null){
                return DbException.asDbException(dbUtilErrorCode,"该数据库名称为："+dbName+EXECUTE_ERRORMESSAGE+e);
            }
            if (dbUtilErrorCode == DBUtilErrorCode.ORACLE_CONN_USERPWD_ERROR ){
                return DbException.asDbException(dbUtilErrorCode,"该数据库用户名为："+userName+EXECUTE_ERRORMESSAGE+e);
            }
            return DbException.asDbException(dbUtilErrorCode,EXECUTE_ERRORMESSAGE+e);
        }
        return DbException.asDbException(DBUtilErrorCode.CONN_DB_ERROR,EXECUTE_ERRORMESSAGE+e);
    }

    public static DBUtilErrorCode mySqlConnectionErrorAna(String e){
        if (e.contains(Constants.MYSQL_DATABASE)){
            return DBUtilErrorCode.MYSQL_CONN_DB_ERROR;
        }

        if (e.contains(Constants.MYSQL_CONNEXP)){
            return DBUtilErrorCode.MYSQL_CONN_IPPORT_ERROR;
        }

        if (e.contains(Constants.MYSQL_ACCDENIED)){
            return DBUtilErrorCode.MYSQL_CONN_USERPWD_ERROR;
        }

        return DBUtilErrorCode.CONN_DB_ERROR;
    }

    public static DBUtilErrorCode oracleConnectionErrorAna(String e){
        if (e.contains(Constants.ORACLE_DATABASE)){
            return DBUtilErrorCode.ORACLE_CONN_DB_ERROR;
        }

        if (e.contains(Constants.ORACLE_CONNEXP)){
            return DBUtilErrorCode.ORACLE_CONN_IPPORT_ERROR;
        }

        if (e.contains(Constants.ORACLE_ACCDENIED)){
            return DBUtilErrorCode.ORACLE_CONN_USERPWD_ERROR;
        }

        return DBUtilErrorCode.CONN_DB_ERROR;
    }

    public static DbException asQueryException(String dataBaseType, Exception e, String querySql, String table, String userName){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            DBUtilErrorCode dbUtilErrorCode = mySqlQueryErrorAna(e.getMessage());
            if (dbUtilErrorCode == DBUtilErrorCode.MYSQL_QUERY_TABLE_NAME_ERROR && table != null){
                return DbException.asDbException(dbUtilErrorCode,"表名为："+table+ EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE+e);
            }
            if (dbUtilErrorCode == DBUtilErrorCode.MYSQL_QUERY_SELECT_PRI_ERROR && userName != null){
                return DbException.asDbException(dbUtilErrorCode,EXECUTE_USERNAME +userName+EXECUTE_ERRORMESSAGE+e);
            }

            return DbException.asDbException(dbUtilErrorCode,EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE+e);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            DBUtilErrorCode dbUtilErrorCode = oracleQueryErrorAna(e.getMessage());
            if (dbUtilErrorCode == DBUtilErrorCode.ORACLE_QUERY_TABLE_NAME_ERROR && table != null){
                return DbException.asDbException(dbUtilErrorCode,"表名为："+table+ EXECUTE_SQLMESSAGE +querySql+EXECUTE_ERRORMESSAGE+e);
            }
            if (dbUtilErrorCode == DBUtilErrorCode.ORACLE_QUERY_SELECT_PRI_ERROR){
                return DbException.asDbException(dbUtilErrorCode,EXECUTE_USERNAME +userName+EXECUTE_ERRORMESSAGE+e);
            }

            return DbException.asDbException(dbUtilErrorCode,EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE+e);

        }

        return DbException.asDbException(DBUtilErrorCode.SQL_EXECUTE_FAIL, EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE+e);
    }

    public static DBUtilErrorCode mySqlQueryErrorAna(String e){
        if (e.contains(Constants.MYSQL_TABLE_NAME_ERR1) && e.contains(Constants.MYSQL_TABLE_NAME_ERR2)){
            return DBUtilErrorCode.MYSQL_QUERY_TABLE_NAME_ERROR;
        }else if (e.contains(Constants.MYSQL_SELECT_PRI)){
            return DBUtilErrorCode.MYSQL_QUERY_SELECT_PRI_ERROR;
        }else if (e.contains(Constants.MYSQL_COLUMN1) && e.contains(Constants.MYSQL_COLUMN2)){
            return DBUtilErrorCode.MYSQL_QUERY_COLUMN_ERROR;
        }else if (e.contains(Constants.MYSQL_WHERE)){
            return DBUtilErrorCode.MYSQL_QUERY_SQL_ERROR;
        }
        return DBUtilErrorCode.READ_RECORD_FAIL;
    }

    public static DBUtilErrorCode oracleQueryErrorAna(String e){
        if (e.contains(Constants.ORACLE_TABLE_NAME)){
            return DBUtilErrorCode.ORACLE_QUERY_TABLE_NAME_ERROR;
        }else if (e.contains(Constants.ORACLE_SQL)){
            return DBUtilErrorCode.ORACLE_QUERY_SQL_ERROR;
        }else if (e.contains(Constants.ORACLE_SELECT_PRI)){
            return DBUtilErrorCode.ORACLE_QUERY_SELECT_PRI_ERROR;
        }
        return DBUtilErrorCode.READ_RECORD_FAIL;
    }

    public static DbException asSqlParserException(String dataBaseType, Exception e, String querySql){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            throw DbException.asDbException(DBUtilErrorCode.MYSQL_QUERY_SQL_PARSER_ERROR, EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE + e);
        }
        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            throw DbException.asDbException(DBUtilErrorCode.ORACLE_QUERY_SQL_PARSER_ERROR,EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE +e);
        }
        throw DbException.asDbException(DBUtilErrorCode.READ_RECORD_FAIL,EXECUTE_SQLMESSAGE + querySql+EXECUTE_ERRORMESSAGE+e);
    }

    public static DbException asPreSqlParserException(String dataBaseType, Exception e, String querySql){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            throw DbException.asDbException(DBUtilErrorCode.MYSQL_PRE_SQL_ERROR, EXECUTE_SQLMESSAGE+querySql+EXECUTE_ERRORMESSAGE + e);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            throw DbException.asDbException(DBUtilErrorCode.ORACLE_PRE_SQL_ERROR,EXECUTE_SQLMESSAGE+querySql+EXECUTE_ERRORMESSAGE +e);
        }
        throw DbException.asDbException(DBUtilErrorCode.READ_RECORD_FAIL,EXECUTE_SQLMESSAGE+querySql+EXECUTE_ERRORMESSAGE+e);
    }

    public static DbException asPostSqlParserException(String dataBaseType, Exception e, String querySql){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            throw DbException.asDbException(DBUtilErrorCode.MYSQL_POST_SQL_ERROR, EXECUTE_SQLMESSAGE+querySql+EXECUTE_ERRORMESSAGE + e);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            throw DbException.asDbException(DBUtilErrorCode.ORACLE_POST_SQL_ERROR,EXECUTE_SQLMESSAGE+querySql+EXECUTE_ERRORMESSAGE +e);
        }
        throw DbException.asDbException(DBUtilErrorCode.READ_RECORD_FAIL,EXECUTE_SQLMESSAGE+querySql+EXECUTE_ERRORMESSAGE+e);
    }

    public static DbException asInsertPriException(String dataBaseType, String userName, String jdbcUrl){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            throw DbException.asDbException(DBUtilErrorCode.MYSQL_INSERT_ERROR, EXECUTE_USERNAME+ userName + EXECUTE_JDBCURL+jdbcUrl);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            throw DbException.asDbException(DBUtilErrorCode.ORACLE_INSERT_ERROR,EXECUTE_USERNAME+userName+EXECUTE_JDBCURL+jdbcUrl);
        }
        throw DbException.asDbException(DBUtilErrorCode.NO_INSERT_PRIVILEGE,EXECUTE_USERNAME+userName+EXECUTE_JDBCURL+jdbcUrl);
    }

    public static DbException asDeletePriException(String dataBaseType, String userName, String jdbcUrl){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){
            throw DbException.asDbException(DBUtilErrorCode.MYSQL_DELETE_ERROR, EXECUTE_USERNAME+userName+EXECUTE_JDBCURL+jdbcUrl);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            throw DbException.asDbException(DBUtilErrorCode.ORACLE_DELETE_ERROR,EXECUTE_USERNAME+userName+EXECUTE_JDBCURL+jdbcUrl);
        }
        throw DbException.asDbException(DBUtilErrorCode.NO_DELETE_PRIVILEGE,EXECUTE_USERNAME+userName+EXECUTE_JDBCURL+jdbcUrl);
    }

    public static DbException asSplitPkException(String dataBaseType, Exception e, String splitSql, String splitPkId){
        if (dataBaseType.equals(JdbcConstants.MYSQL)){

            return DbException.asDbException(DBUtilErrorCode.MYSQL_SPLIT_PK_ERROR,"配置的SplitPK为: "+splitPkId+", 执行的SQL为: "+splitSql+EXECUTE_ERRORMESSAGE+e);
        }

        if (dataBaseType.equals(JdbcConstants.ORACLE)){
            return DbException.asDbException(DBUtilErrorCode.ORACLE_SPLIT_PK_ERROR,"配置的SplitPK为: "+splitPkId+", 执行的SQL为: "+splitSql+EXECUTE_ERRORMESSAGE+e);
        }

        return DbException.asDbException(DBUtilErrorCode.READ_RECORD_FAIL,splitSql+e);
    }
}
