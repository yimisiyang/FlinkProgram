package com.tj712.apitest.source;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 * 从文件中读取数据
 * @Date: 2021/11/4
 * @Time: 19:59
 * @author: ThinkPad
 */
public class SourceTest2_File {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置并行度，为了保证有序可以设置并行度为1
        env.setParallelism(4);
        //从文件中读取数据
        String inputPath = "F:\\Program\\FlinkProgram\\src\\main\\resources\\sensor.txt";
        DataStream<String> dataStream = env.readTextFile(inputPath);
        // 打印输出
        dataStream.print();

        // 执行
        env.execute();
    }

}
