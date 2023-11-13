package com.pppppp.amadda;

import com.pppppp.amadda.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@PropertySource(value = {
    "classpath:env.yml",
}, factory = EnvConfig.class)
public class AmaddaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmaddaApplication.class, args);
    }

}
