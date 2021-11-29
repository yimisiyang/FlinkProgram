package com.tj712.test;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/25
 * @Time: 15:30
 * @author: ThinkPad
 */
public class JDBCReader  extends RichSourceFunction<Tuple2<String,String>> {
    private static final Logger logger = LoggerFactory.getLogger(JDBCReader.class);
    private Connection connection = null;
    private PreparedStatement ps = null;

    // 用于打开数据库连接
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        Class.forName(" "); //加载数据库驱动
        connection = DriverManager.getConnection(" ", " ", " ");//获取连接
        ps = connection.prepareStatement("  "); // 查询语句

    }

    // 执行查询并返回结果
    @Override
    public void run(SourceContext<Tuple2<String, String>> ctx) throws Exception {
        try {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("nick");
                String id = resultSet.getString("user_id");
                logger.error("readJDBC name:{}", name);
                Tuple2<String,String> tuple2 = new Tuple2<>();
                tuple2.setFields(id,name);
                ctx.collect(tuple2);
            }
        } catch (Exception e) {
            logger.error("runException:{}", e);
        }
    }

    @Override
    public void cancel() {
        try {
            super.close();
            if (connection != null) {
                connection.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            logger.error("runException:{}", e);
        }

    }
}
