package com.banking.loanservice.service;

import java.util.List;
import com.banking.loanservice.entity.LoanApplication;
import com.banking.loanservice.exception.LoanApplicationNotFoundException;

public interface LoanService {

	LoanApplication applyForLoan(LoanApplication loanApplication);
	
	LoanApplication getLoanInfoById(int loanApplicationId) throws LoanApplicationNotFoundException;
	
	List<LoanApplication> getLoanDetailsByAccountId(int accountId) throws LoanApplicationNotFoundException;
}
