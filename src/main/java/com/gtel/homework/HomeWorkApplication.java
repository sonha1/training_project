package com.gtel.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableMethodSecurity(prePostEnabled = true)
public class HomeWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeWorkApplication.class, args);
    }

}
