package com.giordanbetat.projectspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "com.giordanbetat.projectspringboot.model")
@ComponentScan(basePackages = {"com.giordanbetat.projectspringboot.*"})
@EnableJpaRepositories(basePackages = {"com.giordanbetat.projectspringboot.repository"})
@EnableTransactionManagement
public class SpringbootApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
		
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("/login");
	}
	
}