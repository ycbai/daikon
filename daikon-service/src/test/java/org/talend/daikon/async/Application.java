package org.talend.daikon.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("standalone")
@ComponentScan(basePackages = "org.talend.daikon")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
