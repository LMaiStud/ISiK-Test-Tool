package com.isik.testsuit.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppConfigTest {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() {
        context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    @Test
    void testRestTemplateBean() {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        assertNotNull(restTemplate, "RestTemplate should not be null");
    }

    @Test
    void testBeanType() {
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        assert (restTemplate instanceof RestTemplate);
    }
    
    @AfterEach
    void tearDown() {
        context.close();
    }
}