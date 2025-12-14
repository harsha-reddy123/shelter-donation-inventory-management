package org;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DonorAppApplication {

    public static void main(String[] args) {
        System.out.println("DonorAppApplication initialization");
        SpringApplication.run(DonorAppApplication.class, args);
        System.out.println("DonorAppApplication end");
    }

}