package com.banking.transactionService.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.banking.transactionService.entity.Transaction;
import com.banking.transactionService.entity.TransactionStatus;
import com.banking.transactionService.entity.TransactionType;
import com.banking.transactionService.exception.InsufficientBalanceException;
import com.banking.transactionService.repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "/testdata.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TransactionIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
    
	
	@Test
	public void depositSuccessTest() throws InsufficientBalanceException, Exception {
	    int accountId = 1;
	    double depositAmount = 1000;

		
		MvcResult result = mockMvc.perform(put("/transactions/deposit")
				.param("accountId",String.valueOf(accountId))
				.param("amount", String.valueOf(depositAmount))
				.contentType(MediaType.APPLICATION_JSON))
		  .andExpect(jsonPath("$.accountId").value(accountId))
	        .andExpect(jsonPath("$.amount").value((double) depositAmount))  // amount is likely returned as double
	        .andExpect(jsonPath("$.transactionId").exists())
	        .andExpect(jsonPath("$.transactionId").isNumber())
	        .andExpect(jsonPath("$.transactionStatus").value("SUCCESS"))
	        .andExpect(jsonPath("$.transactionType").value("DEPOSIT"))
	        .andReturn();
		
        // Extract transactionId from response
        String responseContent = result.getResponse().getContentAsString();
        Transaction responseTransaction = objectMapper.readValue(responseContent, Transaction.class);
        Integer transactionId = responseTransaction.getTransactionId();

        // Verify database state
        Transaction savedTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AssertionError("Transaction not found in database"));


	           
	        assert savedTransaction.getTransactionStatus() == TransactionStatus.SUCCESS;
	        assert savedTransaction.getTransactionType() == TransactionType.DEPOSIT;
	        assert savedTransaction.getTransactionCreatedAt() != null;
	}
	



	
@Test
public void depositFailureWithInvalidAccountTest() throws Exception {
	int accountId=999;
	int depositAmount=1000;
	
	mockMvc.perform(put("/transactions/deposit")
			.param("accountId",String.valueOf(accountId))
			.param("amount", String.valueOf(depositAmount))
	        .contentType(MediaType.APPLICATION_JSON))
	.andExpect(status().isNotFound())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	.andExpect(jsonPath("$.message").value("Account with ID " + accountId + " not found"))
	.andReturn();
	
	boolean transactionsExists = transactionRepository.findAll().stream().anyMatch(t->t.getAccountId()==accountId && t.getAmount()==depositAmount);
	
	assertFalse(transactionsExists,"ransaction should not be saved for invalid account ID");
}

}
