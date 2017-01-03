package org.talend.daikon.mongo.repo;

import static org.junit.Assert.*;
import static org.talend.daikon.mongo.model.RecordPriority.LOW;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.talend.daikon.mongo.model.PendingRecord;
import org.talend.daikon.mongo.model.RecordPriority;
import org.talend.daikon.mongo.util.TestRepositoryAbstract;

public class TestPendingRecordRepository extends TestRepositoryAbstract {

    @Autowired
    private PendingRecordRepository<String, String> mongoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSavePendingRecord() {
        mongoRepository
                .savePendingRecord(new PendingRecord<>("externalApp1", new ProducerRecord<>("topic1", "key1", "value1"), LOW));
        mongoRepository
                .savePendingRecord(new PendingRecord<>("externalApp2", new ProducerRecord<>("topic2", "key2", "value2"), LOW));

        List<PendingRecord> pendingRecords = mongoTemplate.findAll(PendingRecord.class, "pendingRecords");

        assertNotNull(pendingRecords);
        assertEquals(2, pendingRecords.size());

        PendingRecord pendingRecord1 = pendingRecords.get(0);
        assertEquals("externalApp1", pendingRecord1.getApplicationName());
        assertNotNull(pendingRecord1.getId());
        assertNotNull(pendingRecord1.getCreationDate());
        assertNotNull(pendingRecord1.getLastUpdateDate());
        assertNotNull(pendingRecord1.getKey());
        assertNotNull(pendingRecord1.getValue());
        assertEquals("topic1", pendingRecord1.getTopic());
        assertEquals("key1", pendingRecord1.getKey());
        assertEquals("value1", pendingRecord1.getValue());

        PendingRecord pendingRecord2 = pendingRecords.get(1);
        assertEquals("externalApp2", pendingRecord2.getApplicationName());
        assertNotNull(pendingRecord2.getId());
        assertNotNull(pendingRecord2.getCreationDate());
        assertNotNull(pendingRecord2.getLastUpdateDate());
        assertNotNull(pendingRecord2.getKey());
        assertNotNull(pendingRecord2.getValue());
        assertEquals("topic2", pendingRecord2.getTopic());
        assertEquals("key2", pendingRecord2.getKey());
        assertEquals("value2", pendingRecord2.getValue());
    }

    @Test
    public void testFindPendingRecordByApplication() {
        PendingRecord<String, String> pr1 = new PendingRecord<>("externalApp1", new ProducerRecord<>("topic1", "key1", "value1"),
                LOW);
        PendingRecord<String, String> pr2 = new PendingRecord<>("externalApp2", new ProducerRecord<>("topic2", "key2", "value2"),
                LOW);
        PendingRecord<String, String> pr3 = new PendingRecord<>("externalApp2", new ProducerRecord<>("topic3", "key3", "value3"),
                LOW);
        mongoRepository.savePendingRecord(pr1);
        mongoRepository.savePendingRecord(pr2);
        mongoRepository.savePendingRecord(pr3);

        List<PendingRecord<String, String>> records = mongoRepository.findPendingRecordByApplication("externalApp1");
        assertEquals(1, records.size());
        assertEquals("topic1", records.get(0).getTopic());

        records = mongoRepository.findPendingRecordByApplication("externalApp2");
        assertEquals(2, records.size());
        assertEquals("value2", records.get(0).getValue());
        assertEquals("value3", records.get(1).getValue());
    }

    @Test
    public void testHasHighPriorityPendingRecords() {
        PendingRecord<String, String> pr1 = new PendingRecord<>("externalApp1", new ProducerRecord<>("topic1", "key1", "value1"),
                LOW);
        PendingRecord<String, String> pr2 = new PendingRecord<>("externalApp2", new ProducerRecord<>("topic2", "key2", "value2"),
                LOW);
        PendingRecord<String, String> pr3 = new PendingRecord<>("externalApp2", new ProducerRecord<>("topic3", "key3", "value3"),
                LOW);
        pr3.setRecordPriority(RecordPriority.HIGH);

        mongoRepository.savePendingRecord(pr1);
        mongoRepository.savePendingRecord(pr2);
        mongoRepository.savePendingRecord(pr3);

        assertTrue(mongoRepository.hasHighPriorityPendingRecords("externalApp2"));

        mongoRepository.delete(pr3.getId());
        assertFalse(mongoRepository.hasHighPriorityPendingRecords("externalApp2"));
        assertFalse(mongoRepository.hasHighPriorityPendingRecords("externalApp1"));
    }

    @Test
    public void testDelete() {
        PendingRecord<String, String> pr1 = mongoRepository
                .savePendingRecord(new PendingRecord<>("externalApp1", new ProducerRecord<>("topic1", "key1", "value1"), LOW));
        PendingRecord<String, String> pr2 = mongoRepository
                .savePendingRecord(new PendingRecord<>("externalApp2", new ProducerRecord<>("topic2", "key2", "value2"), LOW));
        PendingRecord<String, String> pr3 = mongoRepository
                .savePendingRecord(new PendingRecord<>("externalApp2", new ProducerRecord<>("topic3", "key3", "value3"), LOW));

        mongoRepository.delete(pr2.getId());

        List<PendingRecord<String, String>> records = mongoRepository.findPendingRecordByApplication("externalApp2");
        assertEquals(1, records.size());
        assertEquals("topic3", records.get(0).getTopic());

        mongoRepository.delete(pr1.getId());

        records = mongoRepository.findPendingRecordByApplication("externalApp1");
        assertEquals(0, records.size());

        mongoRepository.delete(pr3.getId());
        records = mongoRepository.findPendingRecordByApplication("externalApp2");
        assertEquals(0, records.size());
    }
}