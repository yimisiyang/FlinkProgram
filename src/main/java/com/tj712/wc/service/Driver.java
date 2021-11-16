package com.tj712.wc.service;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/9
 * @Time: 13:59
 * @author: ThinkPad
 *
 * flink 流处理消费kafka数据源，定时批量将数据写入MySQL、CK
 */
public class Driver {
    public static void main(String[] args) throws Exception {
        // 1. 创建flink 运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 连接kafka数据源
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("39.97.181.124:9092")
                .setTopics("sensor")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        DataStreamSource<String> dataStream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        // 3. 对数据进行分组处理


        //


        //

    }

}
