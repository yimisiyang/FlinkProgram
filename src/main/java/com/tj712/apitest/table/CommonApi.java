package com.tj712.apitest.table;

import lombok.val;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.BatchTableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/12/15
 * @Time: 10:29
 * @author: ThinkPad
 */
public class CommonApi {
    public static void main(String[] args) throws Exception{
        // 1. 设置执行环境
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);

        // 2. 创建表的执行环境
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(environment);

        // 1.1 基于老版本planner的流处理
        EnvironmentSettings oldStreamSetting = EnvironmentSettings.newInstance()
                .useOldPlanner()
                .inStreamingMode()
                .build();

        StreamTableEnvironment oldStreamTableEnvironment = StreamTableEnvironment.create(environment, oldStreamSetting);

        // 1.2 基于老版本planner的批处理
        ExecutionEnvironment batchEnv = ExecutionEnvironment.getExecutionEnvironment();
        BatchTableEnvironment batchTableEnvironment = BatchTableEnvironment.create(batchEnv);

        // 1.3 基于新版本的planner流处理
        EnvironmentSettings newStreamSetting = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment newStreamTableEnv = StreamTableEnvironment.create(environment, newStreamSetting);


        // 执行
        environment.execute();
    }
}
