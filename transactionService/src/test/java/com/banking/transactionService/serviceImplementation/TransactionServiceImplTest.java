package com.banking.transactionService.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.banking.transactionService.dto.AccountDto;
import com.banking.transactionService.entity.Transaction;
import com.banking.transactionService.entity.TransactionStatus;
import com.banking.transactionService.entity.TransactionType;
import com.banking.transactionService.exception.AccountNotFoundException;
import com.banking.transactionService.exception.TransactionNotFoundException;
import com.banking.transactionService.feignClient.AccountClient;
import com.banking.transactionService.repository.TransactionRepository;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

	@Mock
	private TransactionRepository transactionRepository;
	
	@InjectMocks
	private TransactionServiceImpl transactionServiceImpl;
	
	@Mock
	private AccountClient accountClient;
	
	private Transaction mockTransaction;
	
	private AccountDto mockAccount;
	
	@BeforeEach
	public void setUp() {
		mockTransaction=new Transaction();
		mockTransaction.setAccountId(1);
		mockTransaction.setAmount(2000);
		mockTransaction.setTransactionCreatedAt(LocalDateTime.now());
		mockTransaction.setTransactionId(1);
		mockTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
		mockTransaction.setTransactionType(TransactionType.DEPOSIT);
		
		   mockAccount = new AccountDto();
		    mockAccount.setAccountId(1);
	}
	
	@Test
	public void getTransactionsByAccountIdSuccessTest() throws AccountNotFoundException {
		
		
		when(accountClient.getAccountById(mockAccount.getAccountId())).thenReturn(mockAccount);
		when(transactionRepository.findTransactionsByAccountId(1)).thenReturn(List.of(mockTransaction));
		
		List<Transaction> transactionsByAccountId = transactionServiceImpl.getTransactionsByAccountId(mockAccount.getAccountId());
		
		
		assertEquals(mockAccount.getAccountId(), transactionsByAccountId.get(0).getAccountId());
	}
	
	//Account Exists But No Transactions
	@Test
	public void getTransactionsByAccountIdFailureTest() throws AccountNotFoundException{
	
		
		when(accountClient.getAccountById(99)).thenReturn(mockAccount);
		when(transactionRepository.findTransactionsByAccountId(99)).thenReturn(Collections.emptyList());
		
//		assertThrows(AccountNotFoundException.class, ()->{
//			transactionServiceImpl.getTransactionsByAccountId(99);
//		});
		
		List<Transaction> transactionsByAccountId = transactionServiceImpl.getTransactionsByAccountId(99);
		assertTrue(transactionsByAccountId.isEmpty());
		verify(accountClient,times(1)).getAccountById(99);
		verify(transactionRepository,times(1)).findTransactionsByAccountId(99);
	}
	
	// Account NotFound
	@Test
	public void getTransactionsByAccountId_AccountNotFoundTest() throws AccountNotFoundException {
	    int accountId = 99;

	    // Simulate exception thrown by accountClient
	    when(accountClient.getAccountById(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

	    assertThrows(AccountNotFoundException.class, () -> {
	        transactionServiceImpl.getTransactionsByAccountId(accountId);
	    });

	    verify(accountClient, times(1)).getAccountById(accountId);
	    verify(transactionRepository, never()).findTransactionsByAccountId(anyInt());
	}

	@Test
	public void getTransactionByIdSuccessTest() throws TransactionNotFoundException {
		when(transactionRepository.findById(mockTransaction.getTransactionId())).thenReturn(Optional.of(mockTransaction));
		
		Transaction transactionById = transactionServiceImpl.getTransactionById(mockTransaction.getTransactionId());
		
		verify(transactionRepository,times(1)).findById(mockTransaction.getTransactionId());
		
		assertEquals(mockTransaction.getTransactionStatus(), transactionById.getTransactionStatus());
		
		
	}
	
	@Test
	public void getTransactionByIdFailureTest() throws TransactionNotFoundException{
		int id=99;
		when(transactionRepository.findById(id)).thenReturn(Optional.empty());
		
		assertThrows(TransactionNotFoundException.class, ()->{
			transactionServiceImpl.getTransactionById(id);
		});
		
		verify(transactionRepository,times(1)).findById(99);
	}
	
	@Test
	public void getLastNTransactionsSuccessTest() {
	
		when(accountClient.getAccountById(mockTransaction.getAccountId())).thenReturn(mockAccount);
		when(transactionRepository.findTransactionsByAccountId(mockTransaction.getAccountId())).thenReturn(List.of(mockTransaction));
		
		List<Transaction> lastNTransactions = transactionServiceImpl.getLastNTransactions(mockTransaction.getAccountId(), 5);
		
		verify(accountClient,times(1)).getAccountById(mockTransaction.getAccountId());
		verify(transactionRepository,times(1)).findTransactionsByAccountId(mockTransaction.getAccountId());
		
        assertEquals(mockTransaction.getTransactionId(),lastNTransactions.get(0).getTransactionId());
		
	}
	
	@Test
	public void getLastNTransactionsFailureTest() {
		when(accountClient.getAccountById(99)).thenThrow(new AccountNotFoundException("Account not found.."));
		
		assertThrows(AccountNotFoundException.class, ()->{
			transactionServiceImpl.getLastNTransactions(99, 5);
		});
		
		verify(accountClient,times(1)).getAccountById(99);
		verify(transactionRepository,never()).findTransactionsByAccountId(99);
	}
	
	
	
}
