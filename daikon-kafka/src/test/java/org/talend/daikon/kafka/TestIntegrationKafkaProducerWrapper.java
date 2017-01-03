package org.talend.daikon.kafka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.talend.daikon.mongo.model.RecordPriority.LOW;

import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.talend.daikon.kafka.util.TestKafkaConfiguration;
import org.talend.daikon.kafka.util.TestRepositoryAbstract;
import org.talend.daikon.mongo.model.PendingRecord;
import org.talend.daikon.mongo.repo.PendingRecordRepository;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;

public class TestIntegrationKafkaProducerWrapper extends TestRepositoryAbstract {

    @Value("${daikon.kafka.record.sending.application}")
    private String applicationName;

    @Autowired
    private KafkaProducerWrapper<String, String> wrapper;

    @Autowired
    private TestKafkaConfiguration.KafkaUnitWrapper kafkaUnit;

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    @Qualifier(value = "kafkaProducerClosed")
    @Autowired
    private KafkaProducer<String, String> kafkaProducerClosed;

    @Autowired
    private PendingRecordRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSendMessage_when_kafka_down_message_stored() {
        // given that kafka is down and there is currently no pending records
        assertEquals(0, repository.findPendingRecordByApplication(applicationName).size());
        wrapper.setKafkaProducer(kafkaProducerClosed);

        // when sending a message
        ProducerRecord<String, String> record = new ProducerRecord<>("topic", 1, 2L, "key", "value");
        wrapper.sendMessage(record, LOW);

        // a pending record is stored with all message data
        List<PendingRecord<String, String>> list = repository.findPendingRecordByApplication(applicationName);
        assertEquals(1, list.size());
        PendingRecord<String, String> pendingRecord = list.get(0);
        assertEquals("topic", pendingRecord.getTopic());
        assertEquals("key", pendingRecord.getKey());
        assertEquals("value", pendingRecord.getValue());
        assertEquals("testApp", pendingRecord.getApplicationName());
        assertEquals(LOW, pendingRecord.getRecordPriority());
        assertNotNull(pendingRecord.getId());
        assertEquals((Long) 2L, pendingRecord.getTimestamp());
        assertEquals((Integer) 1, pendingRecord.getPartition());
    }

    @Test
    public void testSendMessage_when_kafka_down_several_messages_stored() {
        // given that kafka is down and there is currently no pending records
        assertEquals(0, repository.findPendingRecordByApplication(applicationName).size());
        wrapper.setKafkaProducer(kafkaProducerClosed);

        // when sending several messages
        wrapper.sendMessage(new ProducerRecord<>("topic1", 1, 2L, "key", "value"), LOW);
        wrapper.sendMessage(new ProducerRecord<>("topic2", 1, 2L, "key", "value"), LOW);
        wrapper.sendMessage(new ProducerRecord<>("topic3", 1, 2L, "key", "value"), LOW);
        wrapper.sendMessage(new ProducerRecord<>("topic4", 1, 2L, "key", "value"), LOW);
        wrapper.sendMessage(new ProducerRecord<>("topic5", 1, 2L, "key", "value"), LOW);

        // 5 pending records is stored with all message data
        List<PendingRecord<String, String>> list = repository.findPendingRecordByApplication(applicationName);
        assertEquals(5, list.size());
    }

    @Test
    @UsingDataSet(locations = { "/data/pendingRecords_testApp.json" }, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void testSendPendingRecords_when_kafka_is_alive() throws Exception {
        assertEquals(2, repository.findPendingRecordByApplication(applicationName).size());
        assertEquals(1, repository.findPendingRecordByApplication("otherApp").size());

        wrapper.sendPendingRecords();

        kafkaUnit.waitForMessages("topic", 2);
        assertEquals(0, repository.findPendingRecordByApplication(applicationName).size());
        assertEquals(1, repository.findPendingRecordByApplication("otherApp").size());
    }

    @Test
    @UsingDataSet(locations = { "/data/pendingRecords_testApp.json" }, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void testSendPendingRecords_when_kafka_is_down() {
        // given that kafka is down and that there are pending records
        assertEquals(2, repository.findPendingRecordByApplication(applicationName).size());
        assertEquals(1, repository.findPendingRecordByApplication("otherApp").size());
        wrapper.setKafkaProducer(kafkaProducerClosed);
        wrapper.setApplicationName("otherApp");

        // send pending records
        wrapper.sendPendingRecords();
        wrapper.setApplicationName(applicationName);

        // pending records are not removed because not sent
        assertEquals(2, repository.findPendingRecordByApplication(applicationName).size());
        assertEquals(1, repository.findPendingRecordByApplication("otherApp").size());
    }
}