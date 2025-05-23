package com.mtran.mvc;

import com.mtran.mvc.service.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KeycloakMiniApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeycloakMiniApplication.class, args);
    }
}
