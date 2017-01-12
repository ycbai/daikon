package org.talend.daikon.repo.model;

import java.util.List;

public interface PendingRecordRepository<K, V> {

    List<PendingRecord<K, V>> findPendingRecordByApplication(String appName);

    PendingRecord<K, V> savePendingRecord(PendingRecord record);

    void delete(String pendingRecordId);

    boolean hasHighPriorityPendingRecords(String applicationName);
}
