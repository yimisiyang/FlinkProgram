package com.tj712.apitest.table;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/12/14
 * @Time: 15:55
 * @author: ThinkPad
 */
public class Example {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //1. 读取数据
        String inputPath = "F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt";
        DataStream<String> dataStream = env.readTextFile(inputPath);

        //2. 转换为POJO
        dataStream.map(line ->{
           String[] fields = line.split(",");
           return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        //3. 创建表的执行环境
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);

        //4. 基于流创建一张表
        Table dataTable = tableEnvironment.fromDataStream(dataStream);

        // 5. 调用table api 进行转换操作
        dataTable.select("id, temperature").where("id = 'sensor_1'");

        // 6.执行sql
        tableEnvironment.createTemporaryView("sensor", dataTable);
        String sql = "select id, temperature from sensor where id = 'sensor_1'";
        Table resultSqlTable = tableEnvironment.sqlQuery(sql);

        tableEnvironment.toAppendStream(resultSqlTable, Row.class).print("result");
        tableEnvironment.toAppendStream(resultSqlTable, Row.class).print("sql");
        env.execute();

    }
}
