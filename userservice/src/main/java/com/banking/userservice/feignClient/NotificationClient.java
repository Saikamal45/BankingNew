package com.banking.userservice.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="notification-service")
public interface NotificationClient {

	@PostMapping("/sendAccountCreationMail")
	String accountCreationMail(@RequestParam String email);
}
