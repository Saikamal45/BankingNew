package com.banking.accountService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.banking.accountService.entity.Account;
import com.banking.accountService.exception.AccountNotFoundException;
import com.banking.accountService.exception.InsufficientBalanceException;
import com.banking.accountService.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@PreAuthorize("hasRole('Admin')")
	@PostMapping("/createAccount")
	public ResponseEntity<Account> createAccount(@RequestBody Account account){
		Account createAccount = accountService.createAccount(account);
		return new ResponseEntity<Account>(createAccount,HttpStatus.OK);
	}
	
	@GetMapping("/getAccountById")
	public ResponseEntity<Account> getAccountDetails(@RequestParam int accountId) throws AccountNotFoundException{
		Account accountById = accountService.getAccountById(accountId);
		return new ResponseEntity<Account>(accountById,HttpStatus.OK);
	}
	
	@PutMapping("/updateAccount")
	public ResponseEntity<Account> updateAccount(@RequestParam int accountId,@RequestBody Account account) throws AccountNotFoundException{
		Account updateAccount = accountService.updateAccount(accountId, account);
		return new ResponseEntity<Account>(updateAccount,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','Customer')")
	@GetMapping("/getBalance")
	public ResponseEntity<Double> getBalance(@RequestParam int accountId)throws AccountNotFoundException{
		double accountBalanace = accountService.accountBalanace(accountId);
		return new ResponseEntity<Double>(accountBalanace,HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAccount")
	public ResponseEntity<String> deleteAccount(@RequestParam int accountId)throws AccountNotFoundException{
		String deleteAccount = accountService.deleteAccount(accountId);
		return new ResponseEntity<String>(deleteAccount,HttpStatus.OK);
	}
	
	@PutMapping("/updateBalance")
	public ResponseEntity<Account> updateBalance(@RequestParam int accountId,@RequestParam double amount, @RequestParam String transactionType)throws AccountNotFoundException{
	Account updateBalance = accountService.updateBalance(accountId, amount, transactionType);
	return new ResponseEntity<Account>(updateBalance,HttpStatus.OK);
	}
	

}

