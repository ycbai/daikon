package org.talend.daikon.kafka;

import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.talend.daikon.mongo.model.PendingRecord;
import org.talend.daikon.mongo.model.RecordPriority;
import org.talend.daikon.mongo.repo.PendingRecordRepository;

/**
 * A wrapper for producing kafka messages.
 *
 * This wrapper expects to have an available KafkaProducer bean component. This producer is used to send messages.
 * Failing sendings are recorded in the mongodb data base.
 */
@Component
public class KafkaProducerWrapper<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerWrapper.class);

    @Value("${daikon.kafka.record.sending.application}")
    private String applicationName;

    @Autowired
    private KafkaProducer<K, V> kafkaProducer;

    @Autowired
    private PendingRecordRepository<K, V> repository;

    public void sendMessage(ProducerRecord<K, V> record, RecordPriority recordPriority) {
        this.kafkaProducer.send(record, (metadata, e) -> {
            handleKafkaResponse(record, recordPriority, e);
        });
    }

    private void handleKafkaResponse(ProducerRecord<K, V> record, RecordPriority recordPriority, Exception e) {
        if (e != null) {
            PendingRecord pendingRecord = new PendingRecord<>(applicationName, record, recordPriority);
            repository.savePendingRecord(pendingRecord);
            LOGGER.warn("Following record sending failed and will be stored for application " + applicationName
                    + " until kafka is up again " + record, e);
        } else {
            LOGGER.debug("Record was sent to kafka without error " + record);
        }
    }

    /**
     * Fetch all pending records from database which matches the application name, and try to send them again
     * through kafka.
     * The method is synchronised to avoid concurrency problems when calling this method simultaneously
     */
    public synchronized void sendPendingRecords() {
        List<PendingRecord<K, V>> pendingRecords = repository.findPendingRecordByApplication(applicationName);
        LOGGER.info("Kafka message scheduler will try to send " + pendingRecords.size() + " messages for application "
                + applicationName);

        pendingRecords.forEach(pendingRecord -> {
            this.kafkaProducer.send(pendingRecord.getProducerRecord(), (metadata, e) -> {
                handleKafkaResponse(pendingRecord.getProducerRecord(), pendingRecord.getRecordPriority(), e);
                repository.delete(pendingRecord.getId());
            });
        });
    }

    public boolean hasHighPriorityPendingRecords() {
        LOGGER.debug("hasHighPriorityPendingRecords for application " + applicationName);
        return repository.hasHighPriorityPendingRecords(applicationName);
    }

    void setKafkaProducer(KafkaProducer<K, V> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
