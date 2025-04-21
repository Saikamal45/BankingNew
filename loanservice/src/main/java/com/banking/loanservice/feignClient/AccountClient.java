package com.banking.loanservice.feignClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.banking.loanservice.Dto.AccountDto;

@FeignClient(name = "account-service",path = "/account")
public interface AccountClient {

	@GetMapping("/getAccountById")
	AccountDto getAccountById(@RequestParam int accountId);
}
