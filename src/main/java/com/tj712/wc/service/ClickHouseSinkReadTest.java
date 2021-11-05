package com.tj712.wc.service;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/4
 * @Time: 16:20
 * @author: ThinkPad
 */
public class ClickHouseSinkReadTest {
    public static void main(String[] args) throws Exception {
        // 构造flink执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        // 设置Flink的并行度
        env.setParallelism(1);

        // source, 从何处获取数据


    }
}
