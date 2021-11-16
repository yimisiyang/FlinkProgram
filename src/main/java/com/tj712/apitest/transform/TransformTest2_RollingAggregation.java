package com.tj712.apitest.transform;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/16
 * @Time: 21:31
 * @author: ThinkPad
 */
public class TransformTest2_RollingAggregation {
    public static void main(String[] args) throws Exception {
        //1.构建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.读取数据源
        DataStreamSource<String> inputStream = env.readTextFile("F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt");

        //3. 转换成SensorReading类型
        DataStream<SensorReading> dataStream = inputStream.map(new MapFunction<String, SensorReading>() {
            @Override
            public SensorReading map(String value) throws Exception {
                String[] fields = value.split(",");
                return new SensorReading(fields[0],new Long(fields[1]),new Double(fields[2]));
            }
        });

        //4.分组
        KeyedStream<SensorReading, Tuple> keyedStream = dataStream.keyBy("id");

        //5.滚动聚合
        SingleOutputStreamOperator<SensorReading> resultStream = keyedStream.maxBy("temperature");
        resultStream.print();

        //6.执行
        env.execute();
    }
}
