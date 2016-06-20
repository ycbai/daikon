package com.example.processor;

import java.security.UnrecoverableKeyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class MessageProcessor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Tracer tracer;
	
	@ServiceActivator(inputChannel = Sink.INPUT)
	public void onMessage(String msg) {
		this.log.info("received message: '" + msg + "'.");
		
		Span currentSpan = tracer.getCurrentSpan();
		tracer.addTag("message", msg);
		tracer.addTag("exception", new UnrecoverableKeyException("here is an exception").toString());
		currentSpan.logEvent("message");
		
		
	}
}
