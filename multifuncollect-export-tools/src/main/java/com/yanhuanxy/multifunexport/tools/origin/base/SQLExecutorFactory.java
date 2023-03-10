package com.yanhuanxy.multifunexport.tools.origin.base;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.exception.origin.RdbmsException;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSQLExecutor;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.origin.dm.DmExecutor;
import com.yanhuanxy.multifunexport.tools.origin.mysql.MySqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.oracle.OracleSqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.sqlserver.SqlServerSqlExecutor;

import java.sql.SQLException;

/**
 * ddl sql执行器 工厂
 * @author yym
 * @date 2020/08/27
 */
public class SQLExecutorFactory {

   /**
    * 根据数据库类型返回对应的接口
    * @param dcDataSourceDto 数据源
    * @return BaseSQLExecutor
    */
   public static BaseSQLExecutor getByDbType(DcDataSourceDto dcDataSourceDto) {
      //获取dbType
      String datasource = dcDataSourceDto.getType();
      if (JdbcConstants.MYSQL.equals(datasource)) {
         return getMySqlExecutorInstance(dcDataSourceDto,null);
      } else if (JdbcConstants.ORACLE.equals(datasource)) {
         return getOracleQueryToolInstance(dcDataSourceDto,null);
      } else if (JdbcConstants.SQL_SERVER.equals(datasource)) {
         return getSqlserverQueryToolInstance(dcDataSourceDto,null);
      }else if(JdbcConstants.DM.equals(datasource)){
         return  getDmQueryToolInstance(dcDataSourceDto,null);
      }else {
         throw new UnsupportedOperationException("暂不支持的类型：".concat(datasource));
      }
   }

   /**
    * 根据数据库类型返回对应的接口
    * @param dcDataSourceDto 数据源
    * @return BaseSQLExecutor
    */
   public static BaseSQLExecutor getByDbType(DcDataSourceDto dcDataSourceDto,Boolean localCache) {
      //获取dbType
      String datasource = dcDataSourceDto.getType();
      if (JdbcConstants.MYSQL.equals(datasource)) {
         return getMySqlExecutorInstance(dcDataSourceDto,localCache);
      } else if (JdbcConstants.ORACLE.equals(datasource)) {
         return getOracleQueryToolInstance(dcDataSourceDto,localCache);
      } else if (JdbcConstants.SQL_SERVER.equals(datasource)) {
         return getSqlserverQueryToolInstance(dcDataSourceDto,localCache);
      }else if(JdbcConstants.DM.equals(datasource)){
         return  getDmQueryToolInstance(dcDataSourceDto,localCache);
      } else {
         throw new UnsupportedOperationException("暂不支持的类型：".concat(datasource));
      }
   }

   private static BaseSQLExecutor getMySqlExecutorInstance(DcDataSourceDto dcDataSourceDto,Boolean localCache) {
      try {
         if(localCache != null){
            return new MySqlExecutor(dcDataSourceDto,localCache);
         }else {
            return new MySqlExecutor(dcDataSourceDto);
         }
      } catch (Exception e) {
         throw RdbmsException.asConnException(JdbcConstants.MYSQL,
                 e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
      }
   }

   private static BaseSQLExecutor getOracleQueryToolInstance(DcDataSourceDto dcDataSourceDto,Boolean localCache) {
      try {
         if(localCache != null){
            return new OracleSqlExecutor(dcDataSourceDto,localCache);
         }else {
            return new OracleSqlExecutor(dcDataSourceDto);
         }
      } catch (SQLException e) {
         throw RdbmsException.asConnException(JdbcConstants.ORACLE,
                 e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
      }
   }

   private static BaseSQLExecutor getSqlserverQueryToolInstance(DcDataSourceDto dcDataSourceDto, Boolean localCache) {
      try {
         if(localCache != null){
            return new SqlServerSqlExecutor(dcDataSourceDto,localCache);
         }else {
            return new SqlServerSqlExecutor(dcDataSourceDto);
         }
      } catch (SQLException e) {
         throw RdbmsException.asConnException(JdbcConstants.SQL_SERVER,
                 e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
      }
   }


   private static BaseSQLExecutor getDmQueryToolInstance(DcDataSourceDto dcDataSourceDto, Boolean localCache){
      try{
         if(localCache != null){
            return  new DmExecutor(dcDataSourceDto,localCache);
         }else{
            return  new DmExecutor(dcDataSourceDto);
         }

      }catch (SQLException e){
         throw RdbmsException.asConnException(JdbcConstants.DM,
                 e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
      }

   }
}
