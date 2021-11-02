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

# 2. flink流处理时设置并行度

```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.setParallelism(8); //默认以计算机CPU核数作为并行度
```

# 3. flink部署安装

阿里云的opt目录下已部署完成

# 4. nc命令

```shell
nc -lk 7777      #用来发socket数据流
```

# 5. slot太小导致跑不起来

将flink配置文件中的以下参数改为4（推荐设置为CPU的核心数）

```
taskmanager.numberOfTaskSlots: 4
```

集群资源太少，需要的资源太多导致运行不起来

1）调大集群资源比如改（taskmanager.numberOfTaskSlots: 4）

2）将job的并行度调低

# 6. 提交作业的方式

1）通过web页面提交

2）命令行提交命令

```shell
# 提交作业
./bin/flink run -c com.tj712.wc.StreamWordCount -p 3 F:\Program\FlinkProgram\src\main\java\com\tj712\wc\StreamWordCount.java
# 查看运行的作业
./bin/flink list
# 查看所有作业
./bin/flink list -a
# 取消某个作业
./bin/flink cancel 59868f5e539c7322f876c517986ff375 # 取消调某个任务，cancel后跟任务id
```

# 7. Yarn 模式

以yarn模式部署flink任务时，要求flink是有Hadoop支持的版本，Hadoop环境需要保证版本在2.2以上，并且集群中安装有HDFS服务。

flink提供了两种在yarn上运行的模式，分别为Session-Cluster和Per-Job-Cluster模式。



