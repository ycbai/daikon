package com.example;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.SpanInjector;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.IntegrationComponentScan;

import com.example.filter.RequestFilter;
import com.example.injector.CustomHttpServletResponseSpanInjector;

@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true)
@IntegrationComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@EnableBinding(Source.class)
public class MessageServiceApplication {
	
	@Bean
	Sampler sampler() {
		return span -> true;
	}
	
	@Bean
	@Primary
	SpanInjector<HttpServletResponse> customHttpServletResponseSpanInjector() {
	  return new CustomHttpServletResponseSpanInjector();
	}
	
	/* decomment to watch the log in kibana
	 * 
	  @Bean
	public RequestFilter requestLoggingFilter() {
		RequestFilter crlf = new RequestFilter();
	    crlf.setIncludeClientInfo(true);
	    crlf.setIncludeQueryString(true);
	    crlf.setIncludePayload(true);
	    return crlf;
	}*/
	
	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplication.class, args);
	}
}