package com.banking.loanservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

	private int accountId;
	private int userId;
	private String accountType;
	private double balance;
	
}
