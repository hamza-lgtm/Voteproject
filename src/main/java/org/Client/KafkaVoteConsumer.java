package org.Client;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaVoteConsumer {

    private final String topic;
    private Consumer<String, String> consumer;
    private static final String KAFKA_BROKER = "localhost:9092";  // Replace with your Kafka broker address

    public KafkaVoteConsumer(String topic) {
        this.topic = topic;
        initializeConsumer();
    }

    private void initializeConsumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public ConsumerRecords<String, String> pollVotes() {
        return consumer.poll(Duration.ofMillis(1000)); // Polls for 1 second
    }


    public void close() {
        if (consumer != null) {
            consumer.close();
        }
    }


}