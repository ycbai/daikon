package org.talend.daikon.kafka;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestKafkaMessageScheduler {

    @InjectMocks
    private KafkaMessageScheduler scheduler;

    @Mock
    private KafkaProducerWrapper kafkaProducerWrapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendPendingRecords() {
        scheduler.sendPendingRecords();

        verify(kafkaProducerWrapper).sendPendingRecords();
    }
}