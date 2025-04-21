package com.banking.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEmailRequest {
	private String toEmail;
    private double amount;
    private String transactionType;

}
