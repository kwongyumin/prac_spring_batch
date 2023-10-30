package com.example.pracspringbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class PracSpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracSpringBatchApplication.class, args);
        System.out.println("TEST");
    }

}
