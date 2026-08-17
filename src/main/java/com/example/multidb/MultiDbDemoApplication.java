package com.example.multidb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MultiDbDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDbDemoApplication.class, args);
    }
}
