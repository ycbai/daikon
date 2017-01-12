package org.talend.daikon.kafka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.talend.daikon.repo.RecordPriority.LOW;

import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.talend.daikon.DaikonKafkaConfiguration;
import org.talend.daikon.kafka.util.PendingRecordRepositoryMock;
import org.talend.daikon.kafka.util.TestKafkaConfiguration;
import org.talend.daikon.repo.RecordPriority;
import org.talend.daikon.repo.model.PendingRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@Import(DaikonKafkaConfiguration.class)
@TestPropertySource(properties = "daikon.kafka.record.sending.application=testApp")
@DirtiesContext
public class TestIntegrationKafkaProducerWrapper {

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
    private PendingRecordRepositoryMock repository;

    @Before
    public void setup() {
        repository.clean();
    }

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
    public void testSendPendingRecords_when_kafka_is_alive() throws Exception {
        with3PendingRecords();
        assertEquals(2, repository.findPendingRecordByApplication(applicationName).size());
        assertEquals(1, repository.findPendingRecordByApplication("otherApp").size());

        wrapper.sendPendingRecords();

        List<String> messages = kafkaUnit.waitForMessages("topic", 2);
        // messages are sent chronologically following the creation date
        assertEquals("value2", messages.get(0));
        assertEquals("value1", messages.get(1));
        // only messages for the parametrized application are sent
        assertEquals(0, repository.findPendingRecordByApplication(applicationName).size());
        assertEquals(1, repository.findPendingRecordByApplication("otherApp").size());
    }

    @Test
    public void testSendPendingRecords_when_kafka_is_down() {
        with3PendingRecords();
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

    private void with3PendingRecords() {
        PendingRecord<String, String> pr1 = new PendingRecord<>();
        pr1.setId("pr1");
        pr1.setApplicationName("testApp");
        pr1.setCreationDate(200);
        pr1.setLastUpdateDate(101);
        pr1.setTopic("topic");
        pr1.setPartition(0);
        pr1.setKey("key1");
        pr1.setValue("value1");
        pr1.setTimestamp(10L);
        pr1.setRecordPriority(RecordPriority.LOW);

        PendingRecord<String, String> pr2 = new PendingRecord<>();
        pr2.setId("pr2");
        pr2.setApplicationName("testApp");
        pr2.setCreationDate(100);
        pr2.setLastUpdateDate(101);
        pr2.setTopic("topic");
        pr2.setPartition(0);
        pr2.setKey("key2");
        pr2.setValue("value2");
        pr2.setTimestamp(10L);
        pr2.setRecordPriority(RecordPriority.LOW);

        PendingRecord<String, String> pr3 = new PendingRecord<>();
        pr3.setId("pr3");
        pr3.setApplicationName("otherApp");
        pr3.setCreationDate(100);
        pr3.setLastUpdateDate(101);
        pr3.setTopic("topic");
        pr3.setPartition(0);
        pr3.setKey("key3");
        pr3.setValue("value3");
        pr3.setTimestamp(10L);
        pr3.setRecordPriority(RecordPriority.LOW);

        repository.savePendingRecord(pr1);
        repository.savePendingRecord(pr2);
        repository.savePendingRecord(pr3);
    }
}