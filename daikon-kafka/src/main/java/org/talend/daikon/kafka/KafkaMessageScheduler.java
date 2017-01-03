package org.talend.daikon.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled sending of pending records. It can be disabled in order to let a single instance try to send pending
 * records.
 *
 * This scheduler fetch all pending records in database which match the application name, and tries to send them again.
 */
@Component
@ConditionalOnProperty(name = "daikon.kafka.record.sending.enable")
public class KafkaMessageScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageScheduler.class);

    @Autowired
    private KafkaProducerWrapper kafkaProducerWrapper;

    @Scheduled(fixedDelayString = "${daikon.kafka.record.sending.delay:30000}")
    public void sendPendingRecords() {
        LOGGER.debug("Sending pending records from scheduler");
        kafkaProducerWrapper.sendPendingRecords();
    }
}
