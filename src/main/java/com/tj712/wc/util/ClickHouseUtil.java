package com.tj712.wc.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/4
 * @Time: 14:36
 * @author: ThinkPad
 */
public class ClickHouseUtil {
    private static Connection connection;
    public static Connection getConn(BasicDataSource dataSource) throws SQLException, ClassNotFoundException{
        dataSource.setDriverClassName("ru.yandex.clickhouse.ClickHouseDriver");
        dataSource.setUrl("jdbc:clickhouse://39.97.181.124:8123/default");
        dataSource.setUsername("default");
        dataSource.setPassword("");
        // 设置连接池的一些参数
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(50);
        dataSource.setMinIdle(2);

        try {
            connection = dataSource.getConnection();
            System.out.println("创建连接池：" + connection);
        }catch (Exception e){
            System.out.println("-----------ClickHouse get connection has exception , msg = " + e.getMessage());
        }
        return connection;
    }

    public static Connection getConn(String host, int port, String database) throws SQLException, ClassNotFoundException {
        Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
        String  address = "jdbc:clickhouse://" + host + ":" + port + "/" + database;
        connection = DriverManager.getConnection(address);
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
