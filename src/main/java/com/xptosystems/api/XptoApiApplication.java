package com.xptosystems.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class XptoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XptoApiApplication.class, args);
    }
}
