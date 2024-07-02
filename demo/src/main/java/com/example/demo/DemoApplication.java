package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@ComponentScan(basePackages = {"com.example.demo.controller", "com.example.demo.service", "com.example.demo.repository", "com.example.demo.entity","com.example.demo"})
public class DemoApplication {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        SpringApplication.run(DemoApplication.class, args);
    }

}
