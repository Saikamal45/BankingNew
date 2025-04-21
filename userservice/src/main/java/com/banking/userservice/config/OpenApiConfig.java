package com.banking.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI userServiceApi() {
		return new OpenAPI()
				.info(new Info().title("User Service Api")
						.description("This is Rest Api for User Service"))
				;
		
	}
}
