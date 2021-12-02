package com.tj712.apitest.sink;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/12/2
 * @Time: 8:02
 * @author: ThinkPad
 */
public class MySink {
    /**
     * RedisSink
     */
    public static RedisSink myRedisSink(String hostname, Integer port){
        FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
                .setHost(hostname)
                .setPort(port)
                .build();

        return new RedisSink<>(config, new MyRedisMapper());
    }

    /**
     * KafkaSink
     */
    public static FlinkKafkaProducer myKafkaSink(){
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
        return myProducer;
    }

    /**
     * 自定义RedisMapper
     */
    public static class MyRedisMapper implements RedisMapper<SensorReading>{
        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.HSET,"sensor_tamp");
        }

        @Override
        public String getKeyFromData(SensorReading data) {
            return data.getId();
        }

        @Override
        public String getValueFromData(SensorReading data) {
            return data.getTemperature().toString();
        }
    }
}
