package com.example.controller;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.MessageThirdLevelApplication;

@FeignClient(serviceId = MessageThirdLevelApplication.ZIPKIN_CLIENT_C)
public interface RestMessageReader {

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> readMessage();
}
