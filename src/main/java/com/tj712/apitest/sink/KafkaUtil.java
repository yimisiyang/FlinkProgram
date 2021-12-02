package com.tj712.apitest.sink;

import com.alibaba.fastjson.JSON;
import com.tj712.apitest.beans.SensorReading;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @Date: 2021/12/2
 * @Time: 20:20
 * @author: ThinkPad
 */
public class KafkaUtil {
    public static final String broker_list = "localhost:9092";
    public static final String topic = "SensorReading";

    /**
     * 向kafka中写入数据
     */
    public static void writeToKafka() throws InterruptedException{
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(props);

        for (int i = 0; i <= 100; i++) {
            SensorReading sensorReading = new SensorReading("1" , 20211111L, 30.0);
            ProducerRecord record = new ProducerRecord<>(topic, null, null, JSON.toJSONString(sensorReading));
            producer.send(record);
        }
        producer.flush();
    }


}
