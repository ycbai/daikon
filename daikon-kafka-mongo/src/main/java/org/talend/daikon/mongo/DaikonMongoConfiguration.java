package org.talend.daikon.mongo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = { "org.talend.daikon.mongo" })
@Configuration
@EnableAutoConfiguration
public class DaikonMongoConfiguration {
}
