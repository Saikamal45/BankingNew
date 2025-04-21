package com.banking.accountService.serviceImplementation;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banking.accountService.dto.UserDto;
import com.banking.accountService.entity.Account;
import com.banking.accountService.exception.AccountNotFoundException;
import com.banking.accountService.feignClient.NotificationClient;
import com.banking.accountService.feignClient.UserClient;
import com.banking.accountService.repository.AccountRepository;
import com.banking.accountService.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private NotificationClient notificationClient;
	
	@Autowired
	private UserClient userClient;

	@Override
	public Account createAccount(Account account) {
		account.setCreatedAt(LocalDate.now());
		UserDto userById = userClient.getUserById(account.getUserId());
		notificationClient.accountCreationMail(userById.getEmail());
		return accountRepository.save(account);
		
	}

	@Override
	public Account getAccountById(int accountId) throws AccountNotFoundException {
		return accountRepository.findById(accountId).
		orElseThrow(()->new AccountNotFoundException("Account with Id :"+accountId+" is Not Found..."));
		
	}

	@Override
	public Account updateAccount(int accountId, Account account) throws AccountNotFoundException {
		Account accountById = accountRepository.findById(accountId).
		orElseThrow(()->new AccountNotFoundException("Account with Id :"+accountId+" is Not Found..."));
		
		if(accountById!=null) {
			accountById.setUserId(account.getUserId());
			accountById.setAccountType(account.getAccountType());
			accountById.setBalance(account.getBalance());
			accountById.setStatus(account.getStatus());
			accountById.setCreatedAt(LocalDate.now());
			}
		return accountById;
	}

	@Override
	public String deleteAccount(int accountId) throws AccountNotFoundException{
		Account account = accountRepository.findById(accountId).
		orElseThrow(()->new AccountNotFoundException("Account with Id :"+accountId+" is Not Found..."));
	 accountRepository.deleteById(account.getAccountId());
	 
	 return "Account with ID " + accountId + " has been successfully deleted...";
	}

	@Override
	public double accountBalanace(int accountId) throws AccountNotFoundException{
		Account accountDetails = accountRepository.findById(accountId).
		orElseThrow(()->new AccountNotFoundException("Account with Id :"+accountId+" is Not Found..."));
		
		return accountDetails.getBalance();
	}

	@Override
	public Account updateBalance(int accountId, double amount, String transactionType) throws AccountNotFoundException {
		if (amount <= 0) {
          throw new IllegalArgumentException("Entered amount must be greater than zero.");
      }
		 Account account = accountRepository.findById(accountId).
			orElseThrow(()->new AccountNotFoundException("Account with Id :"+accountId+" is Not Found..."));
		 
		 if("WITHDRAW".equalsIgnoreCase(transactionType)) {
			 if(account.getBalance()<amount) {
				 throw new IllegalArgumentException("Insufficient balance!");
			 }
			 account.setBalance(account.getBalance()-amount);
		 }
		 else if ("TRANSFER".equalsIgnoreCase(transactionType)) {
			 if(account.getBalance()<amount) {
				 throw new IllegalArgumentException("Insufficient balance!");
			 }
			 account.setBalance(account.getBalance()-amount);
		}
		 else if ("DEPOSIT".equalsIgnoreCase(transactionType)) {
			account.setBalance(account.getBalance()+amount);
		}
		 else
		 {
			 throw new IllegalArgumentException("Invalid transaction type!");
		 }
		return accountRepository.save(account);
	}



}
