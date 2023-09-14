package com.yanhuanxy.multifunservice.hadoop;

import com.yanhuanxy.multifunservice.springsocket.CompetitionMessageHandler;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HbaseDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionMessageHandler.class);

    private Connection connection;
    private static Configuration configuration;

    public HbaseDemo() {
        configuration = initHBaseEnv();
    }

    /**
     * 初始化 HBase 配置
     */
    public static Configuration initHBaseEnv() {
        Configuration configuration = null;
        try {
            configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "192.168.0.222");
            configuration.set("hbase.zookeeper.property.clientPort", "2182");
            System.setProperty("hadoop.home.dir", "D:\\kf-software\\hadoop\\hadoop-2.10.2");
//            configuration.set("zookeeper.znode.parent", "/hbase");
        } catch (Exception e) {
            LOGGER.error("HBase Client Configuration Initialization exception: ", e);
        }
        return configuration;
    }

    /**
     * 静态内部类
     */
    private static class InstanceHolder {
        // 不会在外部类初始化时就直接加载，只有当调用了getInstance方法时才会静态加载，线程安全。
        private static final HbaseDemo instance = new HbaseDemo();
    }

    /**
     * 单例模式，获取HBase实例
     */
    public static HbaseDemo getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 获取namespace中所有的表名
     *
     * @param namespace
     */
    public List<String> listTables(String namespace) throws IOException {
        List<String> tableNameList = new ArrayList<>();
        // 创建连接
        connection = ConnectionFactory.createConnection(configuration);
        // 获取namespace中所有的表名
        TableName[] tbs = connection.getAdmin().listTableNamesByNamespace(namespace);

        for (TableName tableName : tbs) {
            tableNameList.add(tableName.toString());
        }
        return tableNameList;
    }

    public static void main(String[] args) {
        HbaseDemo instance = HbaseDemo.getInstance();
        try {
            List<String> testdb = instance.listTables("testdb");
            System.out.println( testdb.stream().collect(Collectors.joining(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Configuration conf = HBaseConfiguration.create();
//        conf.addResource("core-site.xml");
//        conf.addResource("hdfs-site.xml");
//        conf.addResource("hbase-site.xml");
//
//        String url = "jdbc:phoenix:192.168.0.222:2182:/hbase";
//        Properties properties = new Properties();
//        properties.setProperty("phoenix.schema.isNamespaceMappingEnabled", "true");
//        properties.setProperty("phoenix.schema.mapSystemTablesToNamespace", "true");
//        properties.setProperty("phoenix.query.timeoutMs", "1200000");
//        properties.setProperty("phoenix.connection.autoCommit", "true");
//        properties.setProperty("hbase.rpc.timeout", "1200000");
//        properties.setProperty("hbase.client.scanner.timeout.period", "1200000");
//        properties.put("key", conf.get("key"));
//        try {
//            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
//            java.sql.Connection connection = DriverManager.getConnection(url, properties);
//            PreparedStatement preparedStatement = connection.prepareStatement("select * from test1");
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1));
//            }
//            preparedStatement.close();
//            connection.close();
//        } catch (SQLException | ClassNotFoundException throwables) {
//            throwables.printStackTrace();
//        }

        CompletableFuture.runAsync(()->{
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {

            });
            CompletableFuture<Void> voidCompletableFutureB = CompletableFuture.runAsync(() -> {

            });
            CompletableFuture.allOf(voidCompletableFuture, voidCompletableFutureB).thenRun(()->{
                System.out.println(111);
            });
        });
    }


}
