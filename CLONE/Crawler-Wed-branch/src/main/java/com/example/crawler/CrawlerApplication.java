package com.example.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.crawler")
public class CrawlerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(CrawlerApplication.class, args);
    }
}