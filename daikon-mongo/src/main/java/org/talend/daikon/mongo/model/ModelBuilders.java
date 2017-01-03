/*
 * ============================================================================
 *
 * Copyright (C) 2006-2015 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 *
 * ============================================================================
 */

package org.talend.daikon.mongo.model;

/**
 * Fluent API to build model objects
 */
public class ModelBuilders {

    private ModelBuilders() {
        // empty private constructor for utility class
    }

    public static PendingRecordBuilder buildPendingRecord() {
        return new PendingRecordBuilder();
    }

    public static class PendingRecordBuilder<K, V> {

        private PendingRecord<K, V> pendingRecord = new PendingRecord<>();

        public PendingRecordBuilder applicationName(String applicationName) {
            this.pendingRecord.setApplicationName(applicationName);
            return this;
        }

        public PendingRecordBuilder topic(String topic) {
            this.pendingRecord.setTopic(topic);
            return this;
        }

        public PendingRecordBuilder partition(Integer partition) {
            this.pendingRecord.setPartition(partition);
            return this;
        }

        public PendingRecordBuilder key(K key) {
            this.pendingRecord.setKey(key);
            return this;
        }

        public PendingRecordBuilder value(V value) {
            this.pendingRecord.setValue(value);
            return this;
        }

        public PendingRecordBuilder timestamp(Long timestamp) {
            this.pendingRecord.setTimestamp(timestamp);
            return this;
        }

        public PendingRecordBuilder priority(RecordPriority priority) {
            this.pendingRecord.setRecordPriority(priority);
            return this;
        }

        public PendingRecord<K, V> build() {
            return this.pendingRecord;
        }

    }

}
