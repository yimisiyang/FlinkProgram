package com.tj712.apitest.transform;

import com.tj712.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/29
 * @Time: 20:55
 * @author: ThinkPad
 */
public class TransformTest5_RichFunction {
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

        //4.RichFunction操作
        SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = streamOperator.map(new MyMapper());
        resultStream.print();


        //5.执行
        env.execute("richFunction");

    }

    /**
     * 实现普通的MapFunction
     */
//    public static class MyMapper implements MapFunction<SensorReading, Tuple2<String, Integer>>{
//        @Override
//        public Tuple2<String, Integer> map(SensorReading value) throws Exception {
//
//            return new Tuple2<>(value.getId(),value.getId().length());
//        }
//    }

    /**
     * 实现RichMapFunction
     */
    public static class MyMapper extends RichMapFunction<SensorReading, Tuple2<String, Integer>>{
        @Override
        public Tuple2<String, Integer> map(SensorReading value) throws Exception {

            return new Tuple2<>(value.getId(), getRuntimeContext().getIndexOfThisSubtask());
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
        }

        @Override
        public void close() throws Exception {
            super.close();
        }
    }
}
