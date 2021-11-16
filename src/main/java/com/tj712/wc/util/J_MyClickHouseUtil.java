package com.tj712.wc.util;

import com.tj712.wc.entity.J_Student;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/4
 * @Time: 14:46
 * @author: ThinkPad
 */
public class J_MyClickHouseUtil extends RichSinkFunction<J_Student> {
    Connection connection = null;

    String sql;

    public J_MyClickHouseUtil(String sql){
        this.sql = sql;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        BasicDataSource dataSource = new BasicDataSource();
        connection = ClickHouseUtil.getConn(dataSource);
    }
    @Override
    public void close() throws Exception {
        super.close();
        if(connection != null){
            connection.close();
        }
    }

    @Override
    public void invoke(J_Student student, Context context) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,student.id);
        preparedStatement.setString(2,student.name);
        preparedStatement.setInt(3,student.age);
        preparedStatement.addBatch();
        long startTime = System.currentTimeMillis();
        int[] ints = preparedStatement.executeBatch();
        connection.commit();
        long endTime = System.currentTimeMillis();
        System.out.println("批量插入完毕用时：" + (endTime - startTime) + " -- 插入数据 = " + ints.length);
    }
}
