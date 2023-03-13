package com.yanhuanxy.multifunexport.tools.origin.base;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.dm.DmExecutor;
import com.yanhuanxy.multifunexport.tools.origin.mysql.MySqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.oracle.OracleSqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.sqlserver.SqlServerExecutor;

/**
 * ddl sql执行器 工厂
 * @author yym
 * @date 2020/08/27
 */
public class SqlExecutorFactory {


   /**
    * 根据数据库类型返回对应的接口
    * @param dcDataSourceDto 数据源
    * @return BaseSQLExecutor
    */
   public static BaseSqlExecutor getByDbType(InitDataSourceConnection dcDataSourceDto) {
      //获取dbType
      String datasource = dcDataSourceDto.getDbName();
      if (JdbcConstants.MYSQL.equals(datasource)) {
         return getMySqlExecutorInstance(dcDataSourceDto);
      } else if (JdbcConstants.ORACLE.equals(datasource)) {
         return getOracleQueryToolInstance(dcDataSourceDto);
      } else if (JdbcConstants.SQL_SERVER.equals(datasource)) {
         return getSqlserverQueryToolInstance(dcDataSourceDto);
      }else if(JdbcConstants.DM.equals(datasource)){
         return  getDmQueryToolInstance(dcDataSourceDto);
      }else {
         throw new UnsupportedOperationException("暂不支持的类型：".concat(datasource));
      }
   }

   private static BaseSqlExecutor getMySqlExecutorInstance(InitDataSourceConnection dcDataSourceDto) {
      return new MySqlExecutor(dcDataSourceDto);
   }

   private static BaseSqlExecutor getOracleQueryToolInstance(InitDataSourceConnection dcDataSourceDto) {
      return new OracleSqlExecutor(dcDataSourceDto);
   }

   private static BaseSqlExecutor getSqlserverQueryToolInstance(InitDataSourceConnection dcDataSourceDto) {
      return new SqlServerExecutor(dcDataSourceDto);
   }

   private static BaseSqlExecutor getDmQueryToolInstance(InitDataSourceConnection dcDataSourceDto){
      return  new DmExecutor(dcDataSourceDto);
   }
}
