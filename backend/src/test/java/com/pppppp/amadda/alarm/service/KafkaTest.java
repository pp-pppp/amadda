package com.pppppp.amadda.alarm.service;

import com.pppppp.amadda.IntegrationTestSupport;
import java.util.Arrays;
import java.util.List;
import net.mguenther.kafka.junit.EmbeddedKafkaCluster;
import net.mguenther.kafka.junit.KeyValue;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.mguenther.kafka.junit.EmbeddedKafkaCluster.provisionWith;
import static net.mguenther.kafka.junit.EmbeddedKafkaClusterConfig.defaultClusterConfig;
import static net.mguenther.kafka.junit.ObserveKeyValues.on;
import static net.mguenther.kafka.junit.SendValues.to;

class KafkaTest extends IntegrationTestSupport {

    private EmbeddedKafkaCluster kafka;

    @BeforeEach
    void setupKafka() {
        kafka = provisionWith(defaultClusterConfig());
        kafka.start();
    }

    @AfterEach
    void tearDownKafka() {
        kafka.stop();
    }

    @Test
    void shouldWaitForRecordsToBePublished() throws Exception {
        List<RecordMetadata> send = kafka.send(to("test-topic", "a", "b", "c"));
        System.out.println("send = " + Arrays.toString(send.toArray()));
//        send = [test-topic-0@0, test-topic-0@1, test-topic-0@2]
        List<KeyValue<String, String>> result = kafka.observe(on("test-topic", 3));
        System.out.println("result = " + Arrays.toString(result.toArray()));
//        result = [
//          KeyValue(key=null, value=a, headers=RecordHeaders(headers = [], isReadOnly = false), metadata=Optional.empty),
//          KeyValue(key=null, value=b, headers=RecordHeaders(headers = [], isReadOnly = false), metadata=Optional.empty),
//          KeyValue(key=null, value=c, headers=RecordHeaders(headers = [], isReadOnly = false), metadata=Optional.empty)
//          ]
    }
}