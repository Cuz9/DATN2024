package com.example.ogani;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.example.ogani.config.CorsConfig;

@SpringBootApplication
public class OganiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OganiApplication.class, args);
	}

}
