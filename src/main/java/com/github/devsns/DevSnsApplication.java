package com.github.devsns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
@ComponentScan({"com.github.devsns.domain", "com.github.devsns.global"})
public class DevSnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevSnsApplication.class, args);
    }

}
