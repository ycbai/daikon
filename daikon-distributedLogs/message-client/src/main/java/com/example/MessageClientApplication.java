package com.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.SpanExtractor;
import org.springframework.cloud.sleuth.SpanInjector;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.IntegrationComponentScan;

import com.example.extractor.CustomHttpServletRequestSpanExtractor;
import com.example.injector.CustomHttpServletResponseSpanInjector;

@EnableDiscoveryClient
@EnableFeignClients
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableBinding(Sink.class)
@IntegrationComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class MessageClientApplication {

	/**
	 * the service with which we're communicating
	 */
	public static final String ZIPKIN_CLIENT_B = "message-service";

	@Bean
	Sampler sampler() {
		return span -> true;
	}

	@Bean
	@Primary
	SpanInjector<HttpServletResponse> customHttpServletResponseSpanInjector() {
	  return new CustomHttpServletResponseSpanInjector();
	}
	
	@Bean
	@Primary
	SpanExtractor<HttpServletRequest> customHttpServletResponseSpanExtractor() {
	  return new CustomHttpServletRequestSpanExtractor();
	}
	public static void main(String[] args) {
		SpringApplication.run(MessageClientApplication.class, args);
	}
}
