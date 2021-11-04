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

# 2Flink Demo问题 

问题现象：No ExecutorFactory found to execute the application

解决方式：引入flink-client依赖，该问题从flink 1.11版本之后出现

```javascript
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-clients_2.11</artifactId>
    <version>${flink.version}</version>
</dependency>
```

问题原因：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20211104102347.png)

# 3 flink流处理时设置并行度

```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.setParallelism(8); //默认以计算机CPU核数作为并行度
```

# 4 flink部署安装

阿里云的opt目录下已部署完成，版本1.14.0 ，scala版本为2.12

# 5 nc命令

```shell
nc -lk 7777      #用来发socket数据流
```

# 6 slot太小导致跑不起来

将flink配置文件中的以下参数改为4（推荐设置为CPU的核心数）

```
taskmanager.numberOfTaskSlots: 4
```

集群资源太少，需要的资源太多导致运行不起来

1）调大集群资源比如改（taskmanager.numberOfTaskSlots: 4）

2）将job的并行度调低

# 7 提交作业的方式

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

# 8 Yarn 模式

以yarn模式部署flink任务时，要求flink是有Hadoop支持的版本，Hadoop环境需要保证版本在2.2以上，并且集群中安装有HDFS服务。

flink提供了两种在yarn上运行的模式，分别为Session-Cluster和Per-Job-Cluster模式。

# 9 安装部署clickhouse

安装rpm包：包位于阿里云的 `/opt/software/clickhose` 目录，安装过程如下：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20211103203731.png)

启动并设置为开机自启

```shell
systemctl start clickhouse-server
/usr/lib/systemd/systemd-sysv-install enable clickhouse-server
```

## 9.1 使用DBeaver连接clickhouse

DBeaver的配置如下所示，用户名为default, 密码无：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20211103204147.png)

**问题记录：**

Clickhouse连接端口默认8123，但是Clickhouse默认情况下不允许其他设备进行http连接，所以需要更改clickhouse的默认配置:

```shell
vim /etc/clickhouse-server/config.xml
```

新增一行，默认是注释的，让本地服务可以远程连接远程部署的Clickhouse服务，如下所示：

![](https://raw.githubusercontent.com/yimisiyang/cloudimage/master/Image/20211103204442.png)

**参考文章：**https://blog.51cto.com/u_12469213/2861109

## 9.2 下载数据集并导入

**hits_v1表：**

```shell
curl -O https://datasets.clickhouse.com/hits/partitions/hits_v1.tar
tar xvf hits_v1.tar -C /var/lib/clickhouse # path to ClickHouse data directory
# check permissions on unpacked data, fix if required
sudo service clickhouse-server restart
clickhouse-client --query "SELECT COUNT(*) FROM datasets.hits_v1"
```

**visits_v1表：**

```shell
curl -O https://datasets.clickhouse.com/visits/partitions/visits_v1.tar
tar xvf visits_v1.tar -C /var/lib/clickhouse # path to ClickHouse data directory
# check permissions on unpacked data, fix if required
sudo service clickhouse-server restart
clickhouse-client --query "SELECT COUNT(*) FROM datasets.visits_v1"
```

**clickhouse创建表(students)：**

```sql
CREATE TABLE students (
	`id` UInt8,
	`name` String,
	`age` UInt8
)ENGINE = TinyLog
```

**在students中插入数据：**

```sql
insert into students values(3, '李四', 19);
```

