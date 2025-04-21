package com.banking.loanservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.banking.loanservice.Dto.UserDto;

@FeignClient(name="user-service")
public interface UserClient {

		
	@GetMapping("/user/getUser")
	UserDto getUserByEmail(@RequestParam String email);
}
