package com.food.ordering.system.customer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.food.ordering.system")
@EnableJpaRepositories(basePackages = {"com.food.ordering.system.customer.service.dataaccess",
                                        "com.food.ordering.system.dataaccess"})
@EntityScan(basePackages = {"com.food.ordering.system.customer.service.dataaccess",
                            "com.food.ordering.system.dataaccess"})
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
