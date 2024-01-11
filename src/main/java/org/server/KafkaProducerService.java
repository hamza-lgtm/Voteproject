package org.server;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaProducerService {
    private static final String KAFKA_BROKER = "your-kafka-broker:9092";  // Replace with your Kafka broker address

    public static void sendVoteToKafka(String vote, String topic) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", KAFKA_BROKER);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            // Send the vote to the specified Kafka topic
            producer.send(new ProducerRecord<>(topic, vote));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
