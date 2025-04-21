package com.banking.transactionService.feignClient;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.banking.transactionService.dto.UserDto;

@FeignClient(name="user-service",path="/user")
public interface UserClient {

	@GetMapping("/getUserById/{id}")
	Optional<UserDto> getUserById(@PathVariable int id);
	
	@GetMapping("/getUser")
	UserDto getUserByEmail(@RequestParam String email);
}
