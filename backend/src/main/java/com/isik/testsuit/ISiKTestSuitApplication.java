package com.isik.testsuit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ISiKTestSuitApplication {

    private static final Logger logger = LogManager.getLogger(ISiKTestSuitApplication.class);

    private static final String start = "\n *****************************************\n" + " *  ISiK-Test-Tool started successfully  *\n" + " *****************************************";

    public static void main(String[] args) {
        SpringApplication.run(ISiKTestSuitApplication.class, args);
        logger.info("Swagger OpenAPI: http://localhost:8080/swagger-ui/index.html");
        logger.info(start);
    }

}
