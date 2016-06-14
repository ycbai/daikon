package com.example.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class MessageProcessor {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@ServiceActivator(inputChannel = Sink.INPUT)
	public void onMessage(String msg) {
		MDC.put("HEYHO", "coucou");
		this.log.info("received message: '" + msg + "'.");
	}
}
