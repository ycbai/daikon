package fr.talend.logs.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;

@Component
public class LoggerFilter implements Filter {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Tracer tracer;
	
	public void destroy() {
		// Nothing to do
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(servletRequest);
		
		String body = IOUtils.toString(wrappedRequest.getReader());
		tracer.addTag("body", body);
		log.info("Request JSON Content - body content : " + body);
		wrappedRequest.resetInputStream();
		
		request.getParameterMap().forEach((key, value) -> {
			for (int i = 0; i < value.length; i++) {
				tracer.addTag(key + " " + i, value[i]);
			}
		});
		Enumeration headerNames = servletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			tracer.addTag(headerName, servletRequest.getHeader(headerName));
		}
		
		Enumeration attributeNames = servletRequest.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = (String) attributeNames.nextElement();
			tracer.addTag(attributeName, servletRequest.getAttribute(attributeName).toString());
		}

		Enumeration parameterNames = servletRequest.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			tracer.addTag(parameterName, servletRequest.getAttribute(parameterName).toString());
		}
		
		chain.doFilter(wrappedRequest, response);

	}

	public void init(FilterConfig arg0) throws ServletException {
		// Nothing to do
	}
	
}