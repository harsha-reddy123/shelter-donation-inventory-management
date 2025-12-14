package org.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.entity")
@ComponentScan(basePackages = {"org.controller", "org.services", "org.dto"})
@EnableJpaRepositories(basePackages = "org.repository")
public class DonorAppApplication {

    public static void main(String[] args) {
        System.out.println("DonorAppApplication initialization");
        SpringApplication.run(DonorAppApplication.class, args);
        System.out.println("DonorAppApplication end");
    }

}