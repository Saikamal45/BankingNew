package com.banking.transactionService.serviceImplementation;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banking.transactionService.dto.AccountDto;
import com.banking.transactionService.dto.TransactionEmailRequest;
import com.banking.transactionService.dto.UserDto;
import com.banking.transactionService.entity.Transaction;
import com.banking.transactionService.entity.TransactionStatus;
import com.banking.transactionService.entity.TransactionType;
import com.banking.transactionService.exception.AccountNotFoundException;
import com.banking.transactionService.exception.InsufficientBalanceException;
import com.banking.transactionService.exception.TransactionNotFoundException;
import com.banking.transactionService.feignClient.AccountClient;
import com.banking.transactionService.feignClient.NotificationClient;
import com.banking.transactionService.feignClient.UserClient;
import com.banking.transactionService.repository.TransactionRepository;
import com.banking.transactionService.service.TransactionService;
import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private NotificationClient notificationClient;
	
	@Override
	public Transaction deposit(int accountId, double amount) throws AccountNotFoundException, InsufficientBalanceException {
		if (amount <= 0) {
          throw new IllegalArgumentException("deposit amount must be greater than zero.");
      }
		try {
			AccountDto accountById = accountClient.getAccountById(accountId);
			
			if(amount>accountById.getBalance()) {
				throw new InsufficientBalanceException("Insufficent Balance.... Available Balance is :"+accountById.getBalance());
			}
			
			Transaction transaction=new Transaction();
			transaction.setAccountId(accountById.getAccountId());
			transaction.setAmount(amount);
			transaction.setTransactionType(TransactionType.DEPOSIT);
			transaction.setTransactionStatus(TransactionStatus.SUCCESS);
			transaction.setTransactionCreatedAt(LocalDateTime.now());
			
			Transaction savedTransaction = transactionRepository.save(transaction);
			
			accountClient.updateBalance(accountId, amount, "DEPOSIT");
			
			Optional<UserDto> userById = userClient.getUserById(accountById.getUserId());
			TransactionEmailRequest transactionEmailRequest=new TransactionEmailRequest();
			transactionEmailRequest.setToEmail(userById.get().getEmail());
			transactionEmailRequest.setAmount(amount);
		    transactionEmailRequest.setTransactionType("DEPOSIT");
			
			notificationClient.sendTransactionEmail(transactionEmailRequest);
			return savedTransaction;
			
		} catch (FeignException.NotFound e) {
			throw new AccountNotFoundException("Account with ID " + accountId + " not found");
		}
		
	}

	@Override
	public Transaction withdrawal(int accountId, double amount) throws InsufficientBalanceException {
		if(amount<=0) {
			 throw new IllegalArgumentException("WithDrawal amount must be greater than zero.");
		}
		
		AccountDto accountById = accountClient.getAccountById(accountId);
		if(amount>accountById.getBalance()) {
			throw new InsufficientBalanceException("Insufficent Balance.... Available Balance is :"+accountById.getBalance());
		}
		Transaction transaction=new Transaction();
		transaction.setAccountId(accountById.getAccountId());
		transaction.setAmount(amount);
		transaction.setTransactionCreatedAt(LocalDateTime.now());
		transaction.setTransactionType(TransactionType.WITHDRAWAL);
		transaction.setTransactionStatus(TransactionStatus.SUCCESS);
		
		Transaction saved = transactionRepository.save(transaction);
		accountClient.updateBalance(accountId, amount, "WITHDRAW");
		
		Optional<UserDto> userById = userClient.getUserById(accountById.getUserId());
		TransactionEmailRequest transactionEmailRequest=new TransactionEmailRequest();
		transactionEmailRequest.setToEmail(userById.get().getEmail());
		transactionEmailRequest.setAmount(amount);
	    transactionEmailRequest.setTransactionType("WITHDRAW");
		
		notificationClient.sendTransactionEmail(transactionEmailRequest);
		return saved;
	}

	@Override
	@Transactional
	public Transaction transfer(int fromAccountId, int toAccountId, double amount) throws InsufficientBalanceException,AccountNotFoundException {
		if(amount<=0) {
			 throw new IllegalArgumentException("Transfer amount must be greater than zero.");
		}
		AccountDto accountById = accountClient.getAccountById(fromAccountId);
		AccountDto accountById2 = accountClient.getAccountById(toAccountId);
		if(amount>accountById.getBalance()) {
			throw new InsufficientBalanceException("Insufficent Balance.... Available Balance is :"+accountById.getBalance());
		}
		Transaction transaction=new Transaction();
		transaction.setAccountId(accountById.getAccountId());
		transaction.setAmount(amount);
		transaction.setTransactionCreatedAt(LocalDateTime.now());
		transaction.setTransactionType(TransactionType.TRANSFER);
		transaction.setTransactionStatus(TransactionStatus.SUCCESS);

		Transaction saved = transactionRepository.save(transaction);
		
		accountClient.updateBalance(fromAccountId, amount, "WITHDRAW");
		accountClient.updateBalance(toAccountId, amount, "DEPOSIT");
		
		Optional<UserDto> userById = userClient.getUserById(accountById.getUserId());
		TransactionEmailRequest transactionEmailRequest=new TransactionEmailRequest();
		transactionEmailRequest.setToEmail(userById.get().getEmail());
		transactionEmailRequest.setAmount(amount);
	    transactionEmailRequest.setTransactionType("TRANSFER");
		
		notificationClient.sendTransactionEmail(transactionEmailRequest);
		
		return saved;

	}

	@Override
	public List< Transaction >getTransactionsByAccountId(int accountId) throws AccountNotFoundException {
		AccountDto accountById = accountClient.getAccountById(accountId);
		List<Transaction> transactionsByAccountId = transactionRepository.findTransactionsByAccountId(accountId);
		return transactionsByAccountId;
	}

	@Override
	public List<Transaction> getTransactionsByDateRange(int accountId, String startDate, String endDate)
			throws AccountNotFoundException {
		  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

		    LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
		    LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);
		    
		AccountDto accountById = accountClient.getAccountById(accountId);
if(accountById==null) {
	throw new AccountNotFoundException("Account with ID " + accountId + " not found");
}

		return transactionRepository.findByAccountIdAndTransactionCreatedAtBetween(accountId, startDateTime, endDateTime);
	}

	@Override
	public Transaction getTransactionById(int transactionId) throws TransactionNotFoundException {
		Transaction transactionById = transactionRepository.findById(transactionId).
		orElseThrow(()-> new TransactionNotFoundException("Transaction with Id :"+transactionId+" is Not found...."));
		return transactionById;
	}

	@Override
	public List<Transaction> getLastNTransactions(int accountId, int count) throws AccountNotFoundException {
		AccountDto accountById = accountClient.getAccountById(accountId);
		
		List<Transaction> transactionsByAccountId = transactionRepository.findTransactionsByAccountId(accountId);
		
		return transactionsByAccountId.stream()
				.sorted((t1,t2)->t2.getTransactionCreatedAt().compareTo(t1.getTransactionCreatedAt()))
				.limit(count)
				.collect(Collectors.toList());
		
		
	}
	

}
