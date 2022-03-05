package com.tj712.apitest.source;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/4
 * @Time: 19:35
 * @author: ThinkPad
 */
public class SourceTest1_Collection {
    public static void main(String[] args) throws Exception{

        // 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment()
                .setParallelism(4);

        // 从集合中读取数据,包装成DataStream
        DataStreamSource<SensorReading> dataStreamSource = env.fromCollection(Arrays.asList(
                        new SensorReading("sensor_1", 154778199L, 35.2),
                        new SensorReading("sensor_6", 154778201L, 32.5),
                        new SensorReading("sensor_7", 154778223L, 33.4),
                        new SensorReading("sensor_10", 154778245L, 30.1)
                )
        );
        // 打印输出
        dataStreamSource.print("data");

        // 执行
        env.execute("list");
    }
}
