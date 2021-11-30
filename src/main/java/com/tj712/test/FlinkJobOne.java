package com.tj712.test;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.state.api.ExistingSavepoint;
import org.apache.flink.state.api.Savepoint;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/30
 * @Time: 9:42
 * @author: ThinkPad
 */
public class FlinkJobOne {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment bEnv   = ExecutionEnvironment.getExecutionEnvironment();
        ExistingSavepoint savepoint = Savepoint.load(bEnv, "hdfs://path/", new HashMapStateBackend());
        savepoint.readListState("my-uid", "list-state", Types.INT);
    }
}
