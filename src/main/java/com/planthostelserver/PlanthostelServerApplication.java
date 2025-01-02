package com.planthostelserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlanthostelServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanthostelServerApplication.class, args);
    }

}
