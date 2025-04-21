package com.banking.loanservice.serviceImplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.loanservice.Dto.AccountDto;
import com.banking.loanservice.entity.LoanApplication;
import com.banking.loanservice.entity.LoanDetails;
import com.banking.loanservice.entity.LoanStatus;
import com.banking.loanservice.entity.LoanType;
import com.banking.loanservice.exception.LoanApplicationNotFoundException;
import com.banking.loanservice.feignClient.AccountClient;
import com.banking.loanservice.repository.LoanApplicationRepository;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplementationTest {
	
	@Mock
	private LoanApplicationRepository loanApplicationRepository;
	
	
	@InjectMocks
	private LoanServiceImplementation loanServiceImplementation;
	
	private LoanApplication mockLoanApplication;
	
	private LoanDetails mockLoanDetails;
	
	@Mock
	private AccountClient accountClient;
	
	@BeforeEach
	public void setUp() {
		mockLoanApplication=new LoanApplication();
		mockLoanApplication.setAccountId(1);
		mockLoanApplication.setLoanAmount(10000);
		mockLoanApplication.setLoanApplicationId(1);
		mockLoanApplication.setLoanIssueDate(LocalDate.now());
		mockLoanApplication.setLoanStatus(LoanStatus.APPROVED);
		mockLoanApplication.setLoanType(LoanType.HOMELOAN);
		mockLoanApplication.setUserId(1);
		
		mockLoanDetails=new LoanDetails();
		mockLoanDetails.setEmiAmount(25000);
		mockLoanDetails.setInterestRate(9);
		mockLoanDetails.setLoanDetailsId(1);
		mockLoanDetails.setTenure(24);
		mockLoanDetails.setLoanApplication(mockLoanApplication);
		
		mockLoanApplication.setLoanDetails(mockLoanDetails);
	}
	
	@Test
	void getLoanInfoByIdSuccessTest() throws LoanApplicationNotFoundException {
		when(loanApplicationRepository.findById(mockLoanApplication.getLoanApplicationId())).thenReturn(Optional.of(mockLoanApplication));
		
		LoanApplication loanInfoById = loanServiceImplementation.getLoanInfoById(mockLoanApplication.getLoanApplicationId());
		
		verify(loanApplicationRepository,times(1)).findById(mockLoanApplication.getLoanApplicationId());
		
	}
	
	@Test
	void getLoanInfoByIdFailureTest() throws LoanApplicationNotFoundException{
		int id=99;
		when(loanApplicationRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(LoanApplicationNotFoundException.class, ()->{
			loanServiceImplementation.getLoanInfoById(id);
		});
		
		verify(loanApplicationRepository,times(1)).findById(id);
	}

	@Test
	void getLoanDetailsByAccountIdSuccessTest() throws LoanApplicationNotFoundException {
		AccountDto mockAccount=new AccountDto();
		mockAccount.setAccountId(1);
		
		when(accountClient.getAccountById(1)).thenReturn(mockAccount);
		when(loanApplicationRepository.findByAccountId(mockLoanApplication.getAccountId())).thenReturn(List.of(mockLoanApplication));
		List<LoanApplication> loanDetailsByAccountId = loanServiceImplementation.getLoanDetailsByAccountId(mockLoanApplication.getAccountId());
		verify(loanApplicationRepository,times(1)).findByAccountId(mockLoanApplication.getAccountId());
		assertEquals(mockLoanApplication.getAccountId(), loanDetailsByAccountId.get(0).getAccountId());
	}
	
	@Test
	void getLoanDetailsByAccountIdFailureTest() {
		AccountDto mockAccount=new AccountDto();
		mockAccount.setAccountId(100);
		
		when(accountClient.getAccountById(100)).thenReturn(mockAccount);
		when(loanApplicationRepository.findByAccountId(100)).thenReturn(Collections.emptyList());
		
		assertThrows(LoanApplicationNotFoundException.class, ()->{
			loanServiceImplementation.getLoanDetailsByAccountId(100);
		});
		
		verify(accountClient,times(1)).getAccountById(100);
		verify(loanApplicationRepository,times(1)).findByAccountId(100);
	}
}
