package org.talend.daikon.kafka.mongo.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Repository layer configuration
 */
@Configuration
@EnableMongoRepositories
@EnableMongoAuditing
public class RepoConfiguration {

}
