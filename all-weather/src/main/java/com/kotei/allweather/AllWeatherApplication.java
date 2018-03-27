package com.kotei.allweather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AllWeatherApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllWeatherApplication.class, args);
	}
}
