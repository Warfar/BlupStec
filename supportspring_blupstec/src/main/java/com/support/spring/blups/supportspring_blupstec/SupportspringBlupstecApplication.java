package com.support.spring.blups.supportspring_blupstec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//Centro de nuestro
@SpringBootApplication
@ComponentScan(basePackages = "com.support.spring.blups.supportspring_blupstec")
@EntityScan(basePackages = "com.support.spring.blups.supportspring_blupstec.entidades")
@EnableJpaRepositories(basePackages = "com.support.spring.blups.supportspring_blupstec.Repocitorios")
public class SupportspringBlupstecApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupportspringBlupstecApplication.class, args);
	}

}
