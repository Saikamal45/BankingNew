package com.banking.loanservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.banking.loanservice.entity.LoanApplication;
import com.banking.loanservice.entity.LoanDetails;
import com.banking.loanservice.entity.LoanStatus;
import com.banking.loanservice.entity.LoanType;
import com.banking.loanservice.repository.LoanApplicationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
@Sql(scripts = "/testdata.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LoanIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void applyLoanSuccessTest() throws JsonProcessingException, Exception {
		
		LoanDetails loanDetails=new LoanDetails();
		loanDetails.setEmiAmount(1000);
		loanDetails.setInterestRate(7.2);
		loanDetails.setTenure(24);
		
		LoanApplication loanApplication=new LoanApplication();
		loanApplication.setAccountId(1);
		loanApplication.setLoanAmount(14000);
		loanApplication.setLoanDetails(loanDetails);
		loanApplication.setLoanIssueDate(LocalDate.of(2020, 5, 10));
		loanApplication.setLoanStatus(LoanStatus.APPROVED);
		loanApplication.setLoanType(LoanType.HOMELOAN);
		loanApplication.setUserId(1);
		
		loanDetails.setLoanApplication(loanApplication);
		mockMvc.perform(post("/loans/applyLoan")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loanApplication)))
				.andExpect(status().isOk());
		
	LoanApplication savedLoanApplication = loanApplicationRepository.findByAccountId(1).get(0);
		
		assertNotNull(savedLoanApplication);
		assertEquals(14000, savedLoanApplication.getLoanAmount());
		assertEquals(LoanStatus.APPROVED, savedLoanApplication.getLoanStatus());
		assertEquals(LoanType.HOMELOAN, savedLoanApplication.getLoanType());
		assertEquals(7.2, savedLoanApplication.getLoanDetails().getInterestRate(),0.1);
	}
	
	@Test
	public void applyLoanFailureTest_MissingRequiredFields() throws Exception {
	    // Missing userId and loanAmount
	    LoanApplication invalidLoan = new LoanApplication();
	    invalidLoan.setAccountId(2003);
	    invalidLoan.setLoanType(LoanType.HOMELOAN);
	    invalidLoan.setLoanStatus(LoanStatus.PENDING);

	    mockMvc.perform(post("/loans/applyLoan")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(invalidLoan)))
	            .andExpect(status().isBadRequest()); // Expect HTTP 400
	}

	@Test
	public void loanApplicationsDetailsSuccessTest() throws Exception {
		
		mockMvc.perform(get("/loans/getLoanInfoById/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.loanApplicationId").value(1))
				.andExpect(jsonPath("$.loanType").value("HOMELOAN"))
				.andExpect(jsonPath("$.loanDetails.interestRate").value(7.2));
	}
	
	@Test
	public void loanApplicationDetailsFailureTest() throws Exception {
		mockMvc.perform(get("/loans/getLoanInfoById/999")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

}
