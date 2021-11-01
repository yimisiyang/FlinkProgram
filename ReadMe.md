# 1.maven依赖的作用域

参考文档：https://www.jianshu.com/p/c5d84c2c7fc8

解决问题：flink运行报错

```log
Exception in thread "main" java.lang.NullPointerException: Cannot find compatible factory for specified execution.target (=local)
    at org.apache.flink.util.Preconditions.checkNotNull(Preconditions.java:104)
    at org.apache.flink.api.java.ExecutionEnvironment.executeAsync(ExecutionEnvironment.java:937)
    at org.apache.flink.api.java.ExecutionEnvironment.execute(ExecutionEnvironment.java:860)
    at org.apache.flink.api.java.ExecutionEnvironment.execute(ExecutionEnvironment.java:844)
    at org.apache.flink.api.java.DataSet.collect(DataSet.java:413)
    at org.apache.flink.api.java.DataSet.print(DataSet.java:1652)
    at org.apache.flink.api.scala.DataSet.print(DataSet.scala:1864)
    at com.rz.flinkdemo.wc.WordCount$.main(WordCount.scala:22)
    at com.rz.flinkdemo.wc.WordCount.main(WordCount.scala)
```

问题原因是： `flink-streaming-scala_2.12` 依赖的作用域不对。