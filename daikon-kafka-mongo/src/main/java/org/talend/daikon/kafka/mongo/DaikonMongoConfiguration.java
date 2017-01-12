package org.talend.daikon.kafka.mongo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = { "org.talend.daikon.kafka.mongo" })
@Configuration
@EnableAutoConfiguration
public class DaikonMongoConfiguration {
}
