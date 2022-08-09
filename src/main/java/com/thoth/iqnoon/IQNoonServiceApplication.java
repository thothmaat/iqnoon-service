package com.thoth.iqnoon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.thoth.iqnoon.mapper")
public class IQNoonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IQNoonServiceApplication.class, args);
	}

}
