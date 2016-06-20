package fr.talend.consumer.reader;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.talend.consumer.MessageConsumerApplication;

@FeignClient(serviceId = MessageConsumerApplication.ZIPKIN_CLIENT_B)
public interface RestMessageReader {
	@RequestMapping(
			method = RequestMethod.GET,
			value = "/",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> readMessage();
}
