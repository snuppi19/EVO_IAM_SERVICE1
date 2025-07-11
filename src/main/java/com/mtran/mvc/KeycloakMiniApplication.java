package com.mtran.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeycloakMiniApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeycloakMiniApplication.class, args);
    }
}
