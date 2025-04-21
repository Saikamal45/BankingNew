package com.banking.accountService.service;

import com.banking.accountService.entity.Account;
import com.banking.accountService.exception.AccountNotFoundException;
import com.banking.accountService.exception.InsufficientBalanceException;

public interface AccountService {

	Account createAccount(Account account);
	
	Account getAccountById(int accountId) throws AccountNotFoundException;
	
	Account updateAccount(int accountId,Account account) throws AccountNotFoundException;
	
	String deleteAccount(int accountId) throws AccountNotFoundException;
	
	double accountBalanace(int accountId) throws AccountNotFoundException;
	
	Account updateBalance(int accountId,double amount,String transactionType)throws AccountNotFoundException;
	

}
