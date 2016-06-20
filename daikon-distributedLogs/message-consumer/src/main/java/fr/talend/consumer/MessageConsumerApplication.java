package fr.talend.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.integration.annotation.IntegrationComponentScan;

@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableBinding(Sink.class)
@IntegrationComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class MessageConsumerApplication {
	
	private static Logger logger = LoggerFactory.getLogger(MessageConsumerApplication.class);
 
//	@StreamListener(Sink.INPUT)
//	public void loggerSink(Date date) {
//		logger.info("Received: " + date.toString());
//	}

	/**
	 * the service with which we're communicating
	 */
	public static final String ZIPKIN_CLIENT_B = "message-service";

	@Bean
	Sampler sampler() {
		//return span -> true;
		return new AlwaysSampler();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MessageConsumerApplication.class, args);
	}
}
