package fr.talend.logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Consumer {
	
	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Consumer.class, args);
	}
}