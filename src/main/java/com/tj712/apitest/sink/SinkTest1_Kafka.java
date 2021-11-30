package com.tj712.apitest.sink;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/30
 * @Time: 19:16
 * @author: ThinkPad
 */
public class SinkTest1_Kafka {
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

        //4. 方式2：添加到kafka sink, 使用addSink添加

        Properties properties = new Properties();
        properties.setProperty("bootstrap.server", "localhost:9092");
        KafkaSerializationSchema<String> serializationSchema = new KafkaSerializationSchema<String>() {
            @Override
            public ProducerRecord<byte[], byte[]> serialize(String element, @Nullable Long timestamp) {
                return new ProducerRecord<>(
                        "my-topic",
                        element.getBytes(StandardCharsets.UTF_8));
            }
        };

        FlinkKafkaProducer myProducer = new FlinkKafkaProducer<>(
                "my-topic",
                serializationSchema,
                properties,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

        streamOperator.addSink(myProducer);


        //5.执行
        env.execute();
    }


}
