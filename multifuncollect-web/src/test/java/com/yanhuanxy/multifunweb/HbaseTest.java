package com.yanhuanxy.multifunweb;

import com.yanhuanxy.multifunservice.hadoop.HbaseDemo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HbaseTest {


    @Test
    public void test() {
        HbaseDemo instance = HbaseDemo.getInstance();
        try {
            instance.listTables("testdb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        Configuration conf = HBaseConfiguration.create();
        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
        conf.addResource("hbase-site.xml");

        String url = "jdbc:phoenix:192.168.0.222:2182:/hbase";
        Properties properties = new Properties();
        properties.setProperty("phoenix.schema.isNamespaceMappingEnabled", "true");
        properties.setProperty("phoenix.schema.mapSystemTablesToNamespace", "true");
        properties.setProperty("phoenix.query.timeoutMs", "1200000");
        properties.setProperty("phoenix.connection.autoCommit", "true");
        properties.setProperty("hbase.rpc.timeout", "1200000");
        properties.setProperty("hbase.client.scanner.timeout.period", "1200000");
        properties.put("key", conf.get("key"));
        try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            java.sql.Connection connection = DriverManager.getConnection(url, properties);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from test1");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
