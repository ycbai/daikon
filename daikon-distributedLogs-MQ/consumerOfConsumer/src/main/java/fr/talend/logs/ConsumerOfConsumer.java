package fr.talend.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan
@EnableBinding(Processor.class)
public class ConsumerOfConsumer {
	
	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumerOfConsumer.class, args);
	}
}