package com.example.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.MessageClientApplication;

@RestController
@RequestMapping("/message")
public class MessageClientRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Tracer tracer;
	@Autowired
	private SpanAccessor accessor;

	@Autowired
	private RestMessageReader restReader;

	@Bean
	private RestTemplate getTemplate() {
		return new RestTemplate();
	}

	@RequestMapping("/template")
	public ResponseEntity<Map<String, String>> template() {

		String url = "http://" + MessageClientApplication.ZIPKIN_CLIENT_B;

		ParameterizedTypeReference<Map<String, String>> ptr = new ParameterizedTypeReference<Map<String, String>>() {
		};

		ResponseEntity<Map<String, String>> responseEntity = this.restTemplate
				.exchange(url, HttpMethod.GET, null, ptr);
		

		Span currentSpan = tracer.getCurrentSpan();
		responseEntity.getBody().keySet().forEach(key -> {
			tracer.addTag(key, responseEntity.getBody().get(key));
			currentSpan.logEvent(key);
		});
		
		return ResponseEntity.ok()
				.contentType(responseEntity.getHeaders().getContentType())
				.body(responseEntity.getBody());
	}

	@RequestMapping("/feign")
	public Map<String, String> feign() {
		return this.restReader.readMessage();
	}
}
