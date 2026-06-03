package com.aisupport.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AiSupportApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiSupportApiApplication.class, args);
    }

}
