package com.tj712.apitest.sink;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/30
 * @Time: 20:29
 * @author: ThinkPad
 */
public class SinkTest2_Redis {
    public static void main(String[] args) throws Exception {
        //1.创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //2.选择数据源
        DataStreamSource<String> inputStream = env.readTextFile("F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt");

        //3. 转换成SensorReading类型
        DataStream<SensorReading> streamOperator = inputStream.map(line -> {
            String[] fields = line.split(",");
            return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
        });

        // 4.Redis Sink
        FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
                .setHost("localhost")
                .setPort(6379)
                .build();

        streamOperator.addSink(new RedisSink<>(config, new MyRedisMapper()));


        //5.执行
        env.execute();
    }

    /**
     * 自定义RedisMapper
     */
    public static class MyRedisMapper implements RedisMapper<SensorReading>{
        /**
         * 定义保存数据到redis的命令，存成Hash表，hset sensor_tamp
         */
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
