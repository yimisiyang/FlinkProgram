package com.tj712.apitest.transform;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/16
 * @Time: 21:02
 * @author: ThinkPad
 */
public class TransformTest1_Base {
    public static void main(String[] args) throws Exception {
        //1.构建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.读取数据源
        DataStreamSource<String> inputStream = env.readTextFile("F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt");

        //3.map操作。把String转换为长度输出
        DataStream<Integer> mapStream = inputStream.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String value) throws Exception {
                return value.length();
            }
        });

        //3. flatmap, 按逗号切分字段
        DataStream<String> flatMapStream = inputStream.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> collector) throws Exception {
                String[] fields = value.split(",");
                for (String field: fields) {
                    collector.collect(field);
                }
            }
        });

        //3. filter,筛选sensor_1开头的数据
        DataStream<String> filterStream = inputStream.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String value) throws Exception {
                return value.startsWith("sensor_1");
            }
        });

        //4. 打印输出
        mapStream.print("map");
        flatMapStream.print("flatMap");
        filterStream.print("filter");

        //5. 执行
        env.execute("基本算子操作");
    }
}
