package org.talend.samplZipkin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.talend.samplZipkin.model.Person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProducerController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

	@Autowired
	private ReplySender replySender;

	@RequestMapping("/")
	public String hi() throws InterruptedException {
		logger.info("Home page");

		Person p = new Person();
		p.setId(1);
		p.setFirstname("Vincent");
		p.setLastname("LE SQUERE");

		ObjectMapper mapper = new ObjectMapper();
		try {
			this.replySender.sendMessage(mapper.writeValueAsString(p));
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
		}
		return "hi";
	}
}
