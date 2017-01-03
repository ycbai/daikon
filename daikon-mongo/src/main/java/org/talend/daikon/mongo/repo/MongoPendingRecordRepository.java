package org.talend.daikon.mongo.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.talend.daikon.mongo.model.PendingRecord;
import org.talend.daikon.mongo.model.RecordPriority;

public interface MongoPendingRecordRepository extends MongoRepository<PendingRecord, String> {

    List<PendingRecord> findByApplicationNameAndRecordPriority(String applicationName, RecordPriority priority);
}
