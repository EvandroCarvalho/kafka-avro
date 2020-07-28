package com.example.version1;

import com.example.Customer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author Evandro Carvalho
 */
public class KafkaAvroProducerV1 {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "10");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");

        KafkaProducer<String, Customer> kafkaProducer = new KafkaProducer<String, Customer>(properties);
        String topic = "customer-avro";

        Customer customer = Customer.newBuilder()
                .setFirstName("Evandro")
                .setLastName("Junior")
                .setAge(25)
                .setHeight(185.5f)
                .setWeight(85)
                .build();

        ProducerRecord<String, Customer> producerRecord = new ProducerRecord<String, Customer>(
                topic, customer
        );

        kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                if (exception == null) {
                    System.out.println("Success");
                    // partition@offset
                    System.out.println(recordMetadata.toString());
                } else {
                    exception.printStackTrace();
                }
            }
        });
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
