package com.example.hellospringboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableCaching
@EnableMongoRepositories("com.example.hellospringboot.DAO")
@EnableScheduling
public class HellospringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(HellospringbootApplication.class, args);
    }
}
