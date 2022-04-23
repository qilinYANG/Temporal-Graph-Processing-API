package org.apache.flink.statefun.playground.java.graphanalytics;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * KafkaConsumer is a data source that will retrieve topics/requests from Kafka.
 * This is done so that we don't lose any message and the message will be sent in order.
 * 
 * This is created using the DataStream API, not the Stateful Funcs API (not sure if it'll work)
 */
public class KafkaConsumer {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final String bootstrapServer = System.getenv("BROKER_ADDRESS");
        final String bootstrapServerPort = System.getenv("BROKER_ADDRESS_PORT");
        System.out.println(bootstrapServer);
        KafkaSource<String> source = KafkaSource.<String>builder()
            .setBootstrapServers(String.format("%s:%s", bootstrapServer, bootstrapServerPort))
            .setTopics("quickstart")
            .setGroupId("flinkapp")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();

        DataStream<String> kafkaSource = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
        kafkaSource.print();

        env.execute("Consume events from Kafka");
    }
}
