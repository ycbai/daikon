package org.talend.daikon.mongo.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

/**
 * Repository layer configuration
 */
@Configuration
@EnableMongoRepositories
@EnableMongoAuditing
public class RepoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ProjectionFactory projectionFactory() {
        return new SpelAwareProxyProjectionFactory();
    }

    @Bean
    public CustomConversions customConversions() {
        final List<Object> converters = new ArrayList<>();
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MongoCustomConverter.class);
        beansWithAnnotation.values().forEach(converters::add);
        return new CustomConversions(converters);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory mongoDbFactory, MongoMappingContext mongoMappingContext,
            CustomConversions customConversions) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory),
                mongoMappingContext);
        converter.setCustomConversions(customConversions);
        return converter;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter)
            throws UnknownHostException {
        return new MongoTemplate(mongoDbFactory, mappingMongoConverter);
    }
}
