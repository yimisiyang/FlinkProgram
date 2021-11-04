package com.tj712.wc.service;

import com.tj712.wc.entity.J_Student;
import com.tj712.wc.util.J_MyClickHouseUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/11/4
 * @Time: 15:11
 * @author: ThinkPad
 */
public class ClickHouseSinkWriteTest {
    public static void main(String[] args) throws Exception {
        // 构造Flink执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        // 设置flink的并行度
        env.setParallelism(1);

        // source, 从什么地方获取数据
        String path = "F:\\Program\\FlinkProgram\\src\\main\\resources\\Hello.txt";
        DataStreamSource<String> inputStreamSource = env.readTextFile(path);

        // Transform 操作
        SingleOutputStreamOperator<J_Student> dataStream = inputStreamSource.map(new MapFunction<String, J_Student>() {
            @Override
            public J_Student map(String data) throws Exception {
                String[] split = data.split(" ");
                return J_Student.of(Integer.parseInt(split[0]),
                        split[1],
                        Integer.parseInt(split[2]));
            }
        });

        //sink
        String sql = "INSERT INTO default.students (id, name, age) VALUES (?,?,?)";
        J_MyClickHouseUtil jdbcSink = new J_MyClickHouseUtil(sql);
        dataStream.addSink(jdbcSink);
        dataStream.print();

        env.execute("clickhouse sink test");

    }
}
