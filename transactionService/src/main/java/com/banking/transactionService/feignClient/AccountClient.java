package com.banking.transactionService.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.banking.transactionService.dto.AccountDto;

@FeignClient(name = "account-service",path = "/account")
public interface AccountClient {

	@GetMapping("/getAccountById")
	AccountDto getAccountById(@RequestParam int accountId);
	
	@PutMapping("/updateBalance")
	void updateBalance(@RequestParam int accountId,@RequestParam double amount,@RequestParam String transactionType);
}
