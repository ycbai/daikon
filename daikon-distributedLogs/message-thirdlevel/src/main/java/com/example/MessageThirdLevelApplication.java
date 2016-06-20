package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.Sampler;
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
public class MessageThirdLevelApplication {

	/**
	 * the service with which we're communicating
	 */
	public static final String ZIPKIN_CLIENT_C = "message-client";

	@Bean
	Sampler sampler() {
		return span -> true;
	}

//	@Bean
//	@Primary
//	SpanInjector<HttpServletResponse> customHttpServletResponseSpanInjector() {
//	  return new CustomHttpServletResponseSpanInjector();
//	}
//	
//	@Bean
//	@Primary
//	SpanExtractor<HttpServletRequest> customHttpServletResponseSpanExtractor() {
//	  return new CustomHttpServletRequestSpanExtractor();
//	}
	public static void main(String[] args) {
		SpringApplication.run(MessageThirdLevelApplication.class, args);
	}
}
