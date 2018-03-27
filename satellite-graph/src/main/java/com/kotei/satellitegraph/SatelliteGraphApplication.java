package com.kotei.satellitegraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SatelliteGraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatelliteGraphApplication.class, args);
	}
}
