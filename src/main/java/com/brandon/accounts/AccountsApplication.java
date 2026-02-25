package com.brandon.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "Brandon Accounts microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Bandon Gandon",
						email = "bandon@gandon.com",
						url = "test"
				),
				license = @License(
						name = "Apache 2.0",
						url = "test"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Brandon Accounts microservice REST API Documentation",
				url = "test"
		)
)
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
