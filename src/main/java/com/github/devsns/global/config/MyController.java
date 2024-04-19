package com.github.devsns.global.config;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyController {

    @CrossOrigin(origins = "https://www.alco4dev.com")
    @GetMapping("/resource")
    public String getResource() {
        return "Access from https://www.alco4dev.com is allowed!";
    }
}

