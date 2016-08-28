package fr.talend.logs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.talend.logs.model.Person;

@RestController
public class MessageConsumerRestController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value="/person", method=RequestMethod.POST)
	public void addPerson(@RequestBody Person p){
		log.info("RequestBody - lastname : " + p.getLastname());

	}
	
}
