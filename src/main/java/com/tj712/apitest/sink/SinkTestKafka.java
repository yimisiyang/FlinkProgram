package com.tj712.apitest.sink;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/30
 * @Time: 19:16
 * @author: ThinkPad
 */
public class SinkTestKafka {
    public static void main(String[] args) throws Exception{
        //1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //2.选择数据源
        DataStreamSource<String> inputStream = env.readTextFile("F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt");

        //3. 转换成SensorReading类型
        DataStream<String> streamOperator = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2])).toString();
        });

        //4. 方式1：添加到kafka sink,使用addSink添加
        //streamOperator.addSink(new FlinkKafkaProducer<String>("localhost:9092", "sinkTest", new SimpleStringSchema()));
        // 方式2：添加到kafka sink, 使用addSink添加

        streamOperator.addSink(MySink.myKafkaSink());
        //5.执行
        env.execute();
    }


}
