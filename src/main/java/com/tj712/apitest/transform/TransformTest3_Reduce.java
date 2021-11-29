package com.tj712.apitest.transform;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/29
 * @Time: 19:36
 * @author: ThinkPad
 */
public class TransformTest3_Reduce {
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

        //4.分组
        KeyedStream<SensorReading, String> keyedStream = streamOperator.keyBy((KeySelector<SensorReading, String>) SensorReading::getId);

        //5.分组聚合（取最大温度值以及最新的时间戳）,value1为前面聚合出来的数据，value2为最新的数据
        keyedStream.reduce(new ReduceFunction<SensorReading>() {
            @Override
            public SensorReading reduce(SensorReading value1, SensorReading value2) throws Exception {
                return (new SensorReading(value1.getId(), value2.getTimestamp(), Math.max(value1.getTemperature(),value2.getTemperature())));
            }
        });

        //6.执行
        env.execute("ReduceJob");
    }
}
