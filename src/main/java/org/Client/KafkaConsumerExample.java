package org.Client;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {
    private static final String KAFKA_BROKER = "localhost:9092";  // Replace with your Kafka broker address
    private static final String TOPIC = "your-topic";  // Replace with your Kafka topic

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");  // Consumer group ID
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        try (Consumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            // Subscribe to the Kafka topic
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (true) {
                // Poll for new messages from the topic
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                // Process received records (votes)
                records.forEach(record -> {
                    System.out.println("Received vote: " + record.value());
                    // Implement your logic for processing the vote here
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
