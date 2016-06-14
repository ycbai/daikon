package com.example.extractor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanExtractor;

public class CustomHttpServletRequestSpanExtractor implements SpanExtractor<HttpServletRequest> {

	@Override
	public Span joinTrace(HttpServletRequest carrier) {
		long traceId = Span.hexToId(carrier.getHeader("correlationId"));
		long spanId = Span.hexToId(carrier.getHeader("mySpanId"));
		
		// extract all necessary headers
		Span.SpanBuilder builder = Span.builder().traceId(traceId).spanId(spanId);
		// build rest of the Span
		return builder.build();
	}

}
