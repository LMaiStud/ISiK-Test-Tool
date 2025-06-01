package com.isik.testsuit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    /**
     * RestTemplate für POST und GET usw.
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
