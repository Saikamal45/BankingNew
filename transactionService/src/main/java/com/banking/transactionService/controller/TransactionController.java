package com.banking.transactionService.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.banking.transactionService.entity.Transaction;
import com.banking.transactionService.entity.TransactionStatus;
import com.banking.transactionService.entity.TransactionType;
import com.banking.transactionService.exception.AccountNotFoundException;
import com.banking.transactionService.exception.InsufficientBalanceException;
import com.banking.transactionService.exception.TransactionNotFoundException;
import com.banking.transactionService.service.TransactionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.GET;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	
private static Logger logger=LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;
	
	//@PreAuthorize("hasRole('Customer')")
	@PutMapping("/deposit")
	//@CircuitBreaker(name = "accountbreaker",fallbackMethod = "getFallBackResponse")
	//@Retry(name = "depositRetry",fallbackMethod = "getFallBackResponse")
	//@RateLimiter(name="depositLimiter",fallbackMethod = "getFallBackResponse")
	public ResponseEntity<Transaction> deposit(@RequestParam int accountId,@RequestParam double  amount)throws AccountNotFoundException, InsufficientBalanceException{
		Transaction deposit = transactionService.deposit(accountId, amount);
		return new ResponseEntity<Transaction>(deposit,HttpStatus.OK);
	}
	
//	public ResponseEntity<Transaction> getFallBackResponse(int accountId,double amount,Exception exception){
//		Transaction fallBackTransaction=new Transaction();
//		fallBackTransaction.setTransactionId(0);
//		fallBackTransaction.setAccountId(accountId);
//		fallBackTransaction.setAmount(amount);
//		fallBackTransaction.setTransactionStatus(TransactionStatus.PENDING);
//		fallBackTransaction.setTransactionType(TransactionType.DEPOSIT);
//		
//		logger.error("FallBack Triggered due to :{}",exception.getMessage());
//		return new ResponseEntity<Transaction>(fallBackTransaction,HttpStatus.SERVICE_UNAVAILABLE);
//		
//	}
//	
	@PostMapping("/withdraw")
	public ResponseEntity<Transaction> withdraw(@RequestParam int accountId,@RequestParam double amount)throws InsufficientBalanceException{
		Transaction withdrawal = transactionService.withdrawal(accountId, amount);
		return new ResponseEntity<Transaction>(withdrawal,HttpStatus.OK);
	}
	
	@PostMapping("/transfer")
	public ResponseEntity<Transaction> transfer(@RequestParam int fromAccountId,@RequestParam int toAccountId,@RequestParam double amount)throws AccountNotFoundException,InsufficientBalanceException{
		Transaction transfer = transactionService.transfer(fromAccountId, toAccountId, amount);
		return new ResponseEntity<Transaction>(transfer,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Customer')")
	@GetMapping("/getTransactions")
	public ResponseEntity<List<Transaction>> getTransactionByAccountId(@RequestParam int accountId)throws AccountNotFoundException{
		List<Transaction> transactionsByAccountId = transactionService.getTransactionsByAccountId(accountId);
		return new ResponseEntity<List<Transaction>>(transactionsByAccountId,HttpStatus.OK);
		
	}
	
	@GetMapping("/getTransactionsByDate")
	public ResponseEntity<List<Transaction>> getTransactionsCreatedBetween(@RequestParam int accountId,
			@RequestParam String startDate,@RequestParam String endDate)throws AccountNotFoundException{
		List<Transaction> transactionsByDateRange = transactionService.getTransactionsByDateRange(accountId, startDate, endDate);
		return new ResponseEntity<List<Transaction>>(transactionsByDateRange,HttpStatus.OK);
	}
	
	@GetMapping("/getTransactionById")
	public ResponseEntity<Transaction> getTransactionById(@RequestParam int transactionId)throws TransactionNotFoundException{
		Transaction transactionById = transactionService.getTransactionById(transactionId);
		return new ResponseEntity<Transaction>(transactionById,HttpStatus.OK);
	}
	
	@GetMapping("/getLastNTransactions")
	public ResponseEntity<List< Transaction>> getLastNTransactions(@RequestParam int accountId,@RequestParam int count) throws AccountNotFoundException{
		List<Transaction> lastNTransactions = transactionService.getLastNTransactions(accountId, count);
		return new ResponseEntity<List<Transaction>>(lastNTransactions,HttpStatus.OK);
	}
	
	
}
