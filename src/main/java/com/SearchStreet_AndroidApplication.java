package com;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableScheduling
@RestController
@ComponentScan
public class SearchStreet_AndroidApplication{
    public static void main(String[] args) {
        SpringApplication.run(SearchStreet_AndroidApplication.class, args);
    }
}