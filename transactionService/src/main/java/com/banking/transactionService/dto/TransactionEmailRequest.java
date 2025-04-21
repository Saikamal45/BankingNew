package com.banking.transactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEmailRequest {
	 private String toEmail;
	    private String subject;
	    private String body;
	    private double amount;
	    private String transactionType;
}
