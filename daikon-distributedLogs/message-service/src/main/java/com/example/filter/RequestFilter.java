package com.example.filter;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class RequestFilter extends AbstractRequestLoggingFilter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		ServletInputStream in;
		try {
			in = request.getInputStream();
			byte[] buf = new byte[1000];
			StringBuilder sb = new StringBuilder();
			for (int nChunk = in.read(buf); nChunk != -1; nChunk = in.read(buf)) {
				sb.append(new String(buf, 0, nChunk));
			}
			log.info("Request JSON Content - message " + message + " - body content : " +  sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		log.info(message);
	}

}
