package org.talend.daikon;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "org.talend.daikon.kafka" })
@Configuration
@EnableScheduling
@EnableAutoConfiguration
public class DaikonKafkaConfiguration {
}
