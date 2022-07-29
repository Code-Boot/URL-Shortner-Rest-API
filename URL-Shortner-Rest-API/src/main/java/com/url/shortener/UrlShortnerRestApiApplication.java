package com.url.shortener;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UrlShortnerRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortnerRestApiApplication.class, args);
	}

}
