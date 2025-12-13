package org.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "org.entity")
public class DonorAppApplication {

    public static void main(String[] args) {
        System.out.println("DonorAppApplication initialization");
        SpringApplication.run(DonorAppApplication.class, args);
        System.out.println("DonorAppApplication end");
    }

}