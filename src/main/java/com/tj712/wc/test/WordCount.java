package com.tj712.wc.test;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/1
 * @Time: 19:58
 * @author: ThinkPad
 *
 * 使用flink进行批处理
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //从文件中读取数据
        String inputPath = "F:\\Program\\FlinkProgram\\src\\main\\resources\\Hello.txt";
        DataSource<String> inputDataSet = env.readTextFile(inputPath);

        //对数据集进行处理，按照空格分词展开，转换成（word,1）进行统计
        AggregateOperator<Tuple2<String, Integer>> resultSet = inputDataSet.flatMap(new MyFlatMapper())
                // 按照第一个位置的word进行分组
                .groupBy(0)
                //将第二个位置进行求和
                .sum(1);
        resultSet.print();
    }
    /**
     * 自定义类，实现FlatMapFunction接口
     */
    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String,Integer>>{
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> collector) throws Exception {
            // 按空格分隔
            String[] words = value.split(" ");
            //遍历所有word ,包装成二元组输出
            for(String word: words){
                collector.collect(new Tuple2<>(word,1));
            }
        }
    }
}
