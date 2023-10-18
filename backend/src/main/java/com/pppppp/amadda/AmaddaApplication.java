package com.pppppp.amadda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AmaddaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmaddaApplication.class, args);
    }

}
