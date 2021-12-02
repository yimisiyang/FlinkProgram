package com.tj712.apitest.sink;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/12/2
 * @Time: 19:45
 * @author: ThinkPad
 */
public class SinkToMySql extends RichSinkFunction<SensorReading> {
    private Connection connection;
    PreparedStatement ps;

    /**
     * open() 方法中建立连接，这样不用每次invoke的时候都要建立连接和释放连接
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = getConnection();
        String sql = " ";
        ps = this.connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        // 关闭连接释放资源
        if(connection != null){
            connection.close();
        }
        if(ps != null){
            ps.close();
        }
    }

    @Override
    public void invoke(SensorReading value, Context context) throws Exception {
        // 组装数据进行数据插入
        ps.setString(1, value.getId());
        ps.setLong(2, value.getTimestamp());
        ps.setDouble(3, value.getTemperature());
        ps.executeUpdate();
    }

    private static Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8", "root", "root@123");
        }catch (Exception e){
            System.out.println("---------------mysql get connection has exception, msg = " + e.getMessage());
        }
        return conn;
    }
}
