package org.talend.samplZipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableZipkinStreamServer
public class ZipkinCollector
{
    public static void main( String[] args )
    {
    	SpringApplication.run(ZipkinCollector.class, args);
    }
}
