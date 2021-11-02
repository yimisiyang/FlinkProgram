package com.tj712.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/1
 * @Time: 21:08
 * @author: ThinkPad
 */
public class StreamWordCount {
    public static void main(String[] args) throws Exception {
        //创建流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //1.从文件中读取
//        String inputPath = "F:\\Program\\FlinkProgram\\src\\main\\resources\\Hello.txt";
//        DataStreamSource<String> inputDataStream = env.readTextFile(inputPath);

        //parameter tool工具从程序启动参数中提取配置项,args来源与main方法中的参数
//        ParameterTool parameterTool = ParameterTool.fromArgs(args);
//        String host = parameterTool.get("host");
//        int port = parameterTool.getInt("port");

        //2. 从socket文本流中读取数据
        DataStreamSource<String> inputDataStream = env.socketTextStream("182.92.57.11",7777);
        //基于数据流进行转换计算
        SingleOutputStreamOperator<Tuple2<String, Integer>> resultStream = inputDataStream.flatMap(new WordCount.MyFlatMapper())
                .keyBy(0)
                .sum(1);
        resultStream.print();

        // 执行任务
        env.execute();
    }
}
