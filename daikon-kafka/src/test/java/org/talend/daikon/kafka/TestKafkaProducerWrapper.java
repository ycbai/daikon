package org.talend.daikon.kafka;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.talend.daikon.mongo.model.PendingRecord;
import org.talend.daikon.mongo.model.RecordPriority;
import org.talend.daikon.mongo.repo.PendingRecordRepository;

@RunWith(MockitoJUnitRunner.class)
public class TestKafkaProducerWrapper {

    @InjectMocks
    private KafkaProducerWrapper<String, String> wrapper = new KafkaProducerWrapper<>();

    @Mock
    private PendingRecordRepository recordRepository;

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendMessage() {
        ProducerRecord<String, String> record = new ProducerRecord<>("topic", "key", "value");
        wrapper.sendMessage(record, RecordPriority.LOW);

        verify(kafkaProducer).send(eq(record), any());
    }

    @Test
    public void testSendPendingRecords() {
        PendingRecord<String, String> p1 = new PendingRecord<>("appName", new ProducerRecord<>("topic", "key1", "value1"),
                RecordPriority.LOW);
        PendingRecord<String, String> p2 = new PendingRecord<>("appName", new ProducerRecord<>("topic", "key2", "value2"),
                RecordPriority.LOW);
        when(recordRepository.findPendingRecordByApplication(anyString())).thenReturn(Arrays.asList(p1, p2));

        wrapper.sendPendingRecords();

        verify(kafkaProducer, times(2)).send(any(ProducerRecord.class), any());
    }
}