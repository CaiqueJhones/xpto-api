package com.xptosystems.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xptosystems.api.city.CityService;

@Configuration
public class CSVStart {
    
    @Bean
    CommandLineRunner starter(CityService cityService) {
        return args -> {
            cityService.readCSV();
        };
    }

}
