package com.example.version1;

import com.example.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author Evandro Carvalho
 */
public class KafkaAvroConsumerV1 {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("group.id", "my-avro-consumer");
        properties.setProperty("enable.auto.commit", "false");
        properties.setProperty("auto.off.set.reset", "earliest");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, Customer> consumer = new KafkaConsumer<String, Customer>(properties);
        String topic = "customer-avro";

        consumer.subscribe(Collections.singleton(topic));
        System.out.println("waiting for data...");

        while (true) {
            final ConsumerRecords<String, Customer> records = consumer.poll(500L);
            for (ConsumerRecord<String, Customer> record : records) {
//                record.key();
//                record.partition();
//                record.offset();
                Customer customer = record.value();
                System.out.println(customer);
            }
            consumer.commitSync();
        }
//        consumer.close();
    }
}
