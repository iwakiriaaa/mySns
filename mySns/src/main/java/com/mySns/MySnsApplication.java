package com.mySns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mySns"})
public class MySnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySnsApplication.class, args);
	}

}
