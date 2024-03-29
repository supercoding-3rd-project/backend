package com.github.devsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevSnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevSnsApplication.class, args);
    }

}
