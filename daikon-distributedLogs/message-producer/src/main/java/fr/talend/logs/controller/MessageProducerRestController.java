package fr.talend.logs.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.talend.logs.model.Person;

@RestController
public class MessageProducerRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ReplySender replySender;

	@RequestMapping("/")
	Map<String, String> message(HttpServletRequest httpRequest) {

		List<String> headers = Arrays.asList("x-span-id", "x-span-name", "x-trace-id");
		String key = "message";
		Map<String, String> response = new HashMap<>();

		String value = "Hi, from a REST endpoint: "	+ System.currentTimeMillis();

		response.put(key, value);
		headers.stream().filter(h -> httpRequest.getHeader(h) != null).forEach(h -> response.put(h, httpRequest.getHeader(h)));

		this.replySender.sendMessage(value);

		return response;
	}
	
	@RequestMapping(value="/person", method=RequestMethod.POST)
	public void addPerson(HttpServletRequest request, @RequestBody Person p){
		log.info("RequestBody - lastname : " + p.getLastname());
		this.replySender.sendMessage(p.getLastname());
	}
	
}
