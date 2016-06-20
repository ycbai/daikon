package fr.talend.zipkin;

/**
 * a consumer for the span data on RabbitMQ that pushes it into a Zipkin span store, 
 * so it can be queried and visualized using the embedded Zipkin UI
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

@SpringBootApplication
@EnableZipkinStreamServer
public class ZipkinStreamServerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ZipkinStreamServerApplication.class, args);
	}
}
