package fr.talend.consumer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.talend.consumer.MessageConsumerApplication;
import fr.talend.consumer.reader.RestMessageReader;

@RestController
@RequestMapping("/message")
public class MessageClientRestController {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestMessageReader restReader;

	@Bean
	private RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@RequestMapping("/template")
	ResponseEntity<Map<String, String>> template() {

		String url = "http://" + MessageConsumerApplication.ZIPKIN_CLIENT_B;

		ParameterizedTypeReference<Map<String, String>> ptr =
				new ParameterizedTypeReference<Map<String, String>>() {
				};

		ResponseEntity<Map<String, String>> responseEntity =
				this.restTemplate.exchange(url, HttpMethod.GET, null, ptr);

		return ResponseEntity
				.ok()
				.contentType(responseEntity.getHeaders().getContentType())
				.body(responseEntity.getBody());
	}

	@RequestMapping("/feign")
	Map<String, String> feign() {
		return this.restReader.readMessage();
	}
}
