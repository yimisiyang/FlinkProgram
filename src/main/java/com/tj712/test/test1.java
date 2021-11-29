package com.tj712.test;

import com.tj712.wc.test.WordCount;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/25
 * @Time: 15:14
 * @author: ThinkPad
 */
public class test1 {
    public static void main(String[] args) throws Exception{
        // 1. 构建Flink 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 获取数据源
        String path = "F:\\Program\\FlinkProgram\\src\\main\\resources\\Hello.txt";
        DataStreamSource<String> dataStreamSource = env.readTextFile(path);
        // 3. 构建算子操作

        SingleOutputStreamOperator<String> filter = dataStreamSource.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String s) throws Exception {
                String[] s1 = s.split(" ");
                for (int i = 0; i < s1.length; i++) {

                    //1. 将MySQL中的数据读出来

                    //2. 如果这个单词在MySQL中不存在就给过滤掉。
                }

                return false;
            }
        });
        filter.print();
        // 4. 执行
        env.execute();
    }
}
