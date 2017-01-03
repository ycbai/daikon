package org.talend.daikon.kafka.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.talend.daikon.DaikonKafkaConfiguration;
import org.talend.daikon.mongo.util.MongoCustomConverter;
import org.talend.daikon.mongo.util.RepoConfiguration;

import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.Mongo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@DirtiesContext
@SpringBootTest(classes = DaikonKafkaConfiguration.class)
public abstract class TestRepositoryAbstract {

    @Rule
    public MongoDbRule mongoDbRule = MongoDbRule.MongoDbRuleBuilder.newMongoDbRule().defaultSpringMongoDb("daikonkafka");

    /**
     *
     * nosql-unit requirement
     *
     */
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MongoTemplate mongoTemplate;

    @After
    public void tearDown() {
        cleanDb();
    }

    /**
     * Method which is going to remove all data
     * from the collection in database
     */
    protected void cleanDb() {
        Set<String> collectionsName = mongoTemplate.getCollectionNames();
        for (String collectionName : collectionsName) {
            if (!collectionName.contains("system.indexes"))
                mongoTemplate.remove(new Query(), collectionName);
        }
    }

    @Configuration
    @EnableMongoRepositories(basePackages = "org.talend.daikon.mongo")
    @ComponentScan(basePackages = "org.talend.daikon.mongo", useDefaultFilters = false, includeFilters = @ComponentScan.Filter(value = {
            Repository.class, MongoCustomConverter.class }, type = FilterType.ANNOTATION))
    @Import(RepoConfiguration.class)
    static public class MongoTestConfiguration extends AbstractMongoConfiguration {

        private static String MONGO_DB = "daikonkafka";

        @Autowired
        private ApplicationContext applicationContext;

        @Override
        @Bean
        public Mongo mongo() throws Exception {
            return new Fongo(MONGO_DB).getMongo();
        }

        @Override
        public CustomConversions customConversions() {
            final List<Object> converters = new ArrayList<>();
            Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MongoCustomConverter.class);
            beansWithAnnotation.values().forEach(converters::add);
            return new CustomConversions(converters);
        }

        @Override
        protected String getDatabaseName() {
            return MONGO_DB;
        }
    }

}