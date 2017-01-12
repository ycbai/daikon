package org.talend.daikon.mongo.repo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.talend.daikon.mongo.repo.model.PersistentPendingRecord;
import org.talend.daikon.repo.RecordPriority;
import org.talend.daikon.repo.model.PendingRecord;

@Repository
public class PendingRecordRepositoryImpl implements PendingRecordRepository {

    @Autowired
    private MongoPendingRecordRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PendingRecord> findPendingRecordByApplication(String appName) {
        Criteria criteria = Criteria.where("applicationName").is(appName);
        Query query = new Query(criteria).with(new Sort(new Sort.Order(Sort.Direction.ASC, "creationDate")));
        List<PersistentPendingRecord> persistentPendingRecords = mongoTemplate.find(query, PersistentPendingRecord.class);
        return persistentPendingRecords.stream().map(PendingRecord.class::cast).collect(Collectors.toList());
    }

    @Override
    public PendingRecord savePendingRecord(PendingRecord record) {
        return repository.save(new PersistentPendingRecord(record));
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
