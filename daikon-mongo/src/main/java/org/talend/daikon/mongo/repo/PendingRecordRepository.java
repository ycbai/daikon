package org.talend.daikon.mongo.repo;

import java.util.List;

import org.talend.daikon.mongo.model.PendingRecord;

public interface PendingRecordRepository<K, V> {

    List<PendingRecord<K, V>> findPendingRecordByApplication(String appName);

    PendingRecord<K, V> savePendingRecord(PendingRecord record);

    void delete(String pendingRecordId);

    boolean hasHighPriorityPendingRecords(String applicationName);
}
