package org.talend.daikon.kafka.mongo.repo;

import java.util.List;

import org.talend.daikon.repo.model.PendingRecord;

public interface PendingRecordRepository<K, V> {

    List<PendingRecord<K, V>> findPendingRecordByApplication(String appName);

    PendingRecord<K, V> savePendingRecord(PendingRecord record);

    void delete(String pendingRecordId);

    boolean hasHighPriorityPendingRecords(String applicationName);
}
