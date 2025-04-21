package com.banking.transactionService.service;


import java.util.List;

import com.banking.transactionService.entity.Transaction;
import com.banking.transactionService.exception.AccountNotFoundException;
import com.banking.transactionService.exception.InsufficientBalanceException;
import com.banking.transactionService.exception.TransactionNotFoundException;

public interface TransactionService {

	Transaction deposit(int accountId,double amount) throws AccountNotFoundException, InsufficientBalanceException;
	
	Transaction withdrawal(int accountId,double amount)  throws InsufficientBalanceException;
	
	Transaction transfer(int fromAccountId,int toAccountId,double amount)throws InsufficientBalanceException,AccountNotFoundException;
	
	List<Transaction> getTransactionsByAccountId(int accountId)throws AccountNotFoundException;
	
	List<Transaction> getTransactionsByDateRange(int accountId,String startDate,String endDate)throws AccountNotFoundException;
	
	Transaction getTransactionById(int transactionId) throws TransactionNotFoundException;
	
	List<Transaction> getLastNTransactions(int accountId,int count) throws AccountNotFoundException;
}
