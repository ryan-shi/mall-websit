package com.ryan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:dubbo.properties")
@ImportResource("classpath:dubbo-consumer.xml")
public class MallWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallWebApplication.class, args);
	}
}
