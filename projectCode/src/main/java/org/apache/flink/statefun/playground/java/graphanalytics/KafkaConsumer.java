package org.apache.flink.statefun.playground.java.graphanalytics;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * KafkaApp is an application for running Kafka consumer to retrieve topics/requests from Kafka.
 * This is done so that we don't lose any message and the message will be sent in order.
 */
public class KafkaConsumer {
    public static void main(String[] args) {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        String bootstrapServer = "0.0.0.0";
        KafkaSource<String> source = KafkaSource.<String>builder()
            .setBootstrapServers(bootstrapServer)
            .setTopics("quickstart")
            .setGroupId("flinkapp")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();

        env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
    }
    
}
