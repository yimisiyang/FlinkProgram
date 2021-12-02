package com.tj712.examples.scalaTuples;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/16
 * @Time: 8:43
 * @author: ThinkPad
 */
@Slf4j
public class Example {
    public static void main(String[] args) throws Exception{
        // 1. 创建Flink执行环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Person> flintstones = env.fromElements(
                new Person("zs",18),
                new Person("ls",19),
                new Person("zw",20)
        );

        DataStream<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.age > 18;
            }
        });

        adults.print();
        env.execute();


    }
}
