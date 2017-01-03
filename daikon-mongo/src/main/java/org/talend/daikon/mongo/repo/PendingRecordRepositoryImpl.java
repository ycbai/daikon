package org.talend.daikon.mongo.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.talend.daikon.mongo.model.PendingRecord;
import org.talend.daikon.mongo.model.RecordPriority;

@Repository
public class PendingRecordRepositoryImpl implements PendingRecordRepository {

    @Autowired
    private MongoPendingRecordRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PendingRecord> findPendingRecordByApplication(String appName) {
        Criteria criteria = Criteria.where("applicationName").is(appName);
        Query with = new Query(criteria).with(new Sort(new Sort.Order(Sort.Direction.ASC, "creationDate")));
        return mongoTemplate.find(with, PendingRecord.class);
    }

    @Override
    public PendingRecord savePendingRecord(PendingRecord record) {
        return repository.save(record);
    }

    @Override
    public void delete(String pendingRecordId) {
        repository.delete(pendingRecordId);
    }

    @Override
    public boolean hasHighPriorityPendingRecords(String applicationName) {
        return !repository.findByApplicationNameAndRecordPriority(applicationName, RecordPriority.HIGH).isEmpty();
    }
}
