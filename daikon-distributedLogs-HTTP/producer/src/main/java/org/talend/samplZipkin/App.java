package org.talend.samplZipkin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
public class App 
{
	private static final Log log = LogFactory.getLog(App.class);

    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// Use this for debugging (or if there is no Zipkin server running on port 9411)
/*	@Bean
	@ConditionalOnProperty(value = "sample.zipkin.enabled", havingValue = "false")
	public ZipkinSpanReporter spanCollector() {
		return new ZipkinSpanReporter() {
			public void report(zipkin.Span span) {
				log.info(String.format("Reporting span [%s]", span));
			}
		};
	}*/
}
