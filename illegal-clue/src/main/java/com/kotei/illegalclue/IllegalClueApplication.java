package com.kotei.illegalclue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class IllegalClueApplication {

	public static void main(String[] args) {
		SpringApplication.run(IllegalClueApplication.class, args);
	}
}
