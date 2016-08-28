package org.talend.samplZipkin.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.talend.samplZipkin.model.Person;

@RestController
public class ProducerController {
	
	private static final Log log = LogFactory.getLog(ProducerController.class);

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/")
	public String hi() throws InterruptedException {
		log.info("Home page");

		Person p = new Person();
		p.setId(1);
		p.setFirstname("Vincent");
		p.setLastname("LE SQUERE");
		this.restTemplate.postForObject("http://localhost:8082/person", p, String.class);
		return "hi";
	}
}
