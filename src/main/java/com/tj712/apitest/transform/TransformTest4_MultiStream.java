package com.tj712.apitest.transform;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/29
 * @Time: 20:03
 * @author: ThinkPad
 */
public class TransformTest4_MultiStream {
    public static void main(String[] args) throws Exception {
        //1.构建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //2.读取数据源
        DataStreamSource<String> inputStream = env.readTextFile("F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt");

        //3. 转换成SensorReading类型
        DataStream<SensorReading> streamOperator = inputStream.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] split = value.split(",");
                return (new SensorReading(split[0], new Long(split[1]), new Double(split[2])));
            }
        });

        //4.分流操作，按照温度值30℃为界进行分流。SplitStream.java在Flink1.13版本以被删除。

        //5.执行
        env.execute();

    }
}
