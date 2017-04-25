package com.example.product.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sajacaros on 2017-04-24.
 */
@Configuration
public class AppConfiguration {
    @Bean
    public ObjectMapper jsonMapper() {
        return new ObjectMapper();
    }
}
