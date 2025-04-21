package com.banking.transactionService.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.banking.transactionService.dto.TransactionEmailRequest;

@FeignClient(name="notification-service")
public interface NotificationClient {

	@PostMapping("/sendEmail")
	void sendTransactionEmail(@RequestBody TransactionEmailRequest request);
}
