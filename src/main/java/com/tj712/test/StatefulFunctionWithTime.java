package com.tj712.test;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.sql.Types;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/30
 * @Time: 10:16
 * @author: ThinkPad
 */
public class StatefulFunctionWithTime extends KeyedProcessFunction<Integer,Integer, Void> {
    private ValueState<Integer> state;
    ListState<Long> updateTimes;

    @Override
    public void open(Configuration parameters) throws Exception {
        state = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("state", TypeInformation.of(Integer.class)));
        updateTimes = getRuntimeContext().getListState(new ListStateDescriptor<Long>("updateTimes", TypeInformation.of(Long.class)));

    }

    @Override
    public void processElement(Integer value, Context ctx, Collector<Void> out) throws Exception {
        state.update(value + 1);
        updateTimes.add(System.currentTimeMillis());
    }
}
