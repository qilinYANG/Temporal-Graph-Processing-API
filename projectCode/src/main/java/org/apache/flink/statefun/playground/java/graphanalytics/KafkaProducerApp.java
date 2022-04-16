package org.apache.flink.statefun.playground.java.graphanalytics;

import java.io.File;
import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProducerApp {
    public static void main(String[] args) throws Exception {
        final String topicName = "quickstart";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file names in ./data folder: ");
        File fileName = new File("./data/" + scanner.nextLine().trim());
        scanner.close();
        Scanner scFiles = new Scanner(fileName);

        // Kafka Producer API
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        Producer<String, String> producer = new KafkaProducer<>(props, new StringSerializer(), new StringSerializer());

        while(scFiles.hasNextLine()) {
            // create message
            String[] inputStr = scFiles.nextLine().trim().split(" ");
            String jsonString = String.format("{\"task\": \"ADD\", \"src\": \"%1$s\", \"dst\": \"%2$s\", \"t\": \"%3$s\"}", inputStr[0], inputStr[1], inputStr[2]);
            System.out.println(jsonString);

            // send message with no key to Kafka broker
            producer.send(new ProducerRecord<>(topicName, jsonString));
        }
        producer.close();
        scFiles.close();
    }    
}
