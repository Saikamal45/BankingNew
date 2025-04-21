package com.banking.loanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LoanserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanserviceApplication.class, args);
	}

}
