package com.banking.transactionService.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.transactionService.entity.Transaction;
import com.banking.transactionService.entity.TransactionStatus;
import com.banking.transactionService.entity.TransactionType;
import com.banking.transactionService.exception.AccountNotFoundException;
import com.banking.transactionService.exception.GlobalExceptionHandler;
import com.banking.transactionService.exception.InsufficientBalanceException;
import com.banking.transactionService.exception.TransactionNotFoundException;
import com.banking.transactionService.service.TransactionService;

@WebMvcTest(TransactionController.class)
@Import(GlobalExceptionHandler.class)
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TransactionService transactionService;
	
	private Transaction mockTransaction;
	
	@BeforeEach
	public void setUp() {
		mockTransaction =new Transaction();
		mockTransaction.setAccountId(1);
		mockTransaction.setAmount(1000);
		mockTransaction.setTransactionCreatedAt(LocalDateTime.now());
		mockTransaction.setTransactionId(1);
		mockTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
		mockTransaction.setTransactionType(TransactionType.DEPOSIT);
	}
	
	@Test
	public void depositSuccessTest() throws InsufficientBalanceException, Exception {
		when(transactionService.deposit(mockTransaction.getAccountId(), mockTransaction.getAmount())).thenReturn(mockTransaction);
		
		mockMvc.perform(put("/transactions/deposit")
				.param("accountId",String.valueOf((int)mockTransaction.getAccountId()))
				.param("amount", String.valueOf((Double)mockTransaction.getAmount())))
		.andExpect(status().isOk());
		
		verify(transactionService,times(1)).deposit(mockTransaction.getAccountId(), mockTransaction.getAmount());
	}
	
	@Test
	public void depositFailureTest() throws InsufficientBalanceException, Exception {
		when(transactionService.deposit(99, 1000)).thenThrow(new AccountNotFoundException("Account Not Found"));
		
		mockMvc.perform(put("/transactions/deposit")
				.param("accountId", "99")
				.param("amount", "1000"))
		.andExpect(status().isNotFound());
		
		verify(transactionService,times(1)).deposit(99, 1000);
	}
	
	@Test
	public void shouldReturn400_whenInsufficientBalance() throws InsufficientBalanceException, Exception {
		when(transactionService.deposit(1, 2000)).thenThrow(new InsufficientBalanceException("Insufficeint Balance..."));
		mockMvc.perform(put("/transactions/deposit")
				.param("accountId", "1")
				.param("amount", "2000"))
		.andExpect(status().isBadRequest());
		
		verify(transactionService,times(1)).deposit(1, 2000);
	}
	
	@Test
	public void withdrawSuccessTest() throws InsufficientBalanceException, Exception {
		when(transactionService.withdrawal(1, 500)).thenReturn(mockTransaction);
		
		mockMvc.perform(post("/transactions/withdraw")
				.param("accountId", "1")
				.param("amount", "500"))
		.andExpect(status().isOk());
		
		verify(transactionService,times(1)).withdrawal(1, 500);
	}
	
	@Test
	public void withdrawFailureTest() throws InsufficientBalanceException, Exception {
when(transactionService.withdrawal(99, 500)).thenThrow(new AccountNotFoundException("Account Not Found..."));
		
		mockMvc.perform(post("/transactions/withdraw")
				.param("accountId", "99")
				.param("amount", "500"))
		.andExpect(status().isNotFound());
		
		verify(transactionService,times(1)).withdrawal(99, 500);
	}
	
	@Test
	public void withdrawFailureInSufficientBalanceTest() throws Exception, InsufficientBalanceException {
when(transactionService.withdrawal(1, 5000)).thenThrow(new InsufficientBalanceException("Insufficient Balance..."));
		
		mockMvc.perform(post("/transactions/withdraw")
				.param("accountId", "1")
				.param("amount", "5000"))
		.andExpect(status().isBadRequest());
		
		verify(transactionService,times(1)).withdrawal(1, 5000);
	}
	
	@Test
	public void  transferSuccessTest() throws InsufficientBalanceException, Exception {
		when(transactionService.transfer(1, 2, 500)).thenReturn(mockTransaction);
		
		mockMvc.perform(post("/transactions/transfer")
				.param("fromAccountId", "1")
				.param("toAccountId", "2")
				.param("amount", "500"))
		.andExpect(status().isOk());
		
		verify(transactionService,times(1)).transfer(1, 2, 500);
	}
	
	@Test
	public void  transferFailureTest() throws InsufficientBalanceException, Exception {
		when(transactionService.transfer(1, 2, 500)).thenThrow(new AccountNotFoundException("AccountNotFound...."));
		
		mockMvc.perform(post("/transactions/transfer")
				.param("fromAccountId", "1")
				.param("toAccountId", "2")
				.param("amount", "500"))
		.andExpect(status().isNotFound());
		
		verify(transactionService,times(1)).transfer(1, 2, 500);
	}
	
	@Test
	public void  transferFailureInsufficientBalanceTest() throws InsufficientBalanceException, Exception {
		when(transactionService.transfer(1, 2, 500)).thenThrow(new InsufficientBalanceException("Insufficient Balance..."));
		
		mockMvc.perform(post("/transactions/transfer")
				.param("fromAccountId", "1")
				.param("toAccountId", "2")
				.param("amount", "500"))
		.andExpect(status().isBadRequest());
		
		verify(transactionService,times(1)).transfer(1, 2, 500);
	}
	
	
	@Test
	public void getTransactionByAccountIdSuccessTest() throws Exception {
		when(transactionService.getTransactionsByAccountId(mockTransaction.getAccountId())).thenReturn(List.of(mockTransaction));
		
		mockMvc.perform(get("/transactions/getTransactions")
				.param("accountId",String.valueOf(mockTransaction.getAccountId())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].accountId").value(1));
		
		verify(transactionService,times(1)).getTransactionsByAccountId(mockTransaction.getAccountId());
				
	}
	
	@Test
	public void getTransactionByAccountIdFailureTest() throws Exception {
		when(transactionService.getTransactionsByAccountId(99)).thenThrow(new AccountNotFoundException("Account Not Found"));
		
		mockMvc.perform(get("/transactions/getTransactions")
				.param("accountId","99"))
				.andExpect(status().isNotFound());
		
		verify(transactionService,times(1)).getTransactionsByAccountId(99);
	}
	
	
	
	@Test
	public void getTransactionsCreatedBetweenSuccessTest() throws Exception{
		when(transactionService.getTransactionsByDateRange(mockTransaction.getAccountId(),"2025-04-08T14:30:45" ,"2025-04-09T14:30:45"))
		.thenReturn(List.of(mockTransaction));
		
		mockMvc.perform(get("/transactions/getTransactionsByDate")
				.param("accountId", String.valueOf(mockTransaction.getAccountId()))
				.param("startDate", "2025-04-08T14:30:45")
				.param("endDate", "2025-04-09T14:30:45"))
		.andExpect(status().isOk());
		
		verify(transactionService,times(1)).getTransactionsByDateRange(mockTransaction.getAccountId(),"2025-04-08T14:30:45" ,"2025-04-09T14:30:45");
	}
	
	
	@Test
	public void getTransactionsCreatedBetweenFailureTest() throws Exception {
		when(transactionService.getTransactionsByDateRange(99,"2025-04-08T14:30:45" ,"2025-04-09T14:30:45"))
		.thenThrow(new AccountNotFoundException("Account Not Found.."));
		
		mockMvc.perform(get("/transactions/getTransactionsByDate")
				.param("accountId", "99")
				.param("startDate","2025-04-08T14:30:45")
				.param("endDate", "2025-04-09T14:30:45"))
		.andExpect(status().isNotFound());
				
		verify(transactionService,times(1)).getTransactionsByDateRange(99,"2025-04-08T14:30:45" ,"2025-04-09T14:30:45" );
	}
	
	@Test
	public void getTransactionByIdSuccessTest() throws Exception {
		when(transactionService.getTransactionById(mockTransaction.getTransactionId())).thenReturn(mockTransaction);
		
		mockMvc.perform(get("/transactions/getTransactionById")
				.param("transactionId", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.transactionId").value(1))
				.andExpect(jsonPath("$.transactionStatus").value("SUCCESS"));
		
		verify(transactionService,times(1)).getTransactionById(mockTransaction.getTransactionId());
	}
	
	@Test
	public void getTransactionByIdFailureTest() throws Exception {
		when(transactionService.getTransactionById(99)).thenThrow(new TransactionNotFoundException("No Transaction found"));
		
		mockMvc.perform(get("/transactions/getTransactionById")
				.param("transactionId", "99"))
		        .andExpect(status().isNotFound());
		
		verify(transactionService,times(1)).getTransactionById(99);
		
	}

	@Test
	public void getLastNTransactionsSuccessTest() throws Exception {
		when(transactionService.getLastNTransactions(mockTransaction.getAccountId(), 5)).thenReturn(List.of(mockTransaction));
		mockMvc.perform(get("/transactions/getLastNTransactions")
				.param("accountId", String.valueOf(mockTransaction.getAccountId()))
				.param("count", "5"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].accountId").value(1));
		
		verify(transactionService,times(1)).getLastNTransactions(mockTransaction.getAccountId(), 5);
	}
	
	@Test
	public void getLastNTransactionsFailureTest() throws Exception {
		when(transactionService.getLastNTransactions(99, 5)).thenThrow(new AccountNotFoundException("Account Not Found.."));
		
		mockMvc.perform(get("/transactions/getLastNTransactions")
				.param("accountId", "99")
				.param("count", "5"))
		.andExpect(status().isNotFound());
		
		verify(transactionService,times(1)).getLastNTransactions(99, 5);
		
	}
}
