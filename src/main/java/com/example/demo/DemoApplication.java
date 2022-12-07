package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;


//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@MapperScan("com.example.demo.dao")
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
