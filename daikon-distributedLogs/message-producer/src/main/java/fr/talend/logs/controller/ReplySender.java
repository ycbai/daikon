package fr.talend.logs.controller;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface ReplySender {

	@Gateway(requestChannel = Source.OUTPUT)
	void sendMessage(String msg);

}
