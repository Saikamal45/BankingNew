package com.banking.loanservice.serviceImplementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.loanservice.Dto.AccountDto;
import com.banking.loanservice.entity.LoanApplication;
import com.banking.loanservice.entity.LoanDetails;
import com.banking.loanservice.entity.LoanStatus;
import com.banking.loanservice.entity.LoanType;
import com.banking.loanservice.exception.LoanApplicationNotFoundException;
import com.banking.loanservice.feignClient.AccountClient;
import com.banking.loanservice.repository.LoanApplicationRepository;
import com.banking.loanservice.service.LoanService;


@Service
public class LoanServiceImplementation implements LoanService{

	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Autowired
	private AccountClient accountClient;
	
	@Override
	public LoanApplication applyForLoan(LoanApplication loanApplication) {
		
		loanApplication.setLoanStatus(LoanStatus.APPROVED);
	    loanApplication.setLoanIssueDate(LocalDate.now());
	    
	    LoanType loanType = loanApplication.getLoanType();
	    
	    double interestRate = getInterestRateByLoanType(loanType);
	    
	    LoanDetails loanDetails=new LoanDetails();
	    loanDetails.setInterestRate(interestRate);
	    loanDetails.setTenure(24);
	    
	    double emiPerMonth = calculateEmi(loanApplication.getLoanAmount(),interestRate,24);
	    loanDetails.setEmiAmount(emiPerMonth);
	    
	    loanDetails.setLoanApplication(loanApplication);
	    loanApplication.setLoanDetails(loanDetails);
		return loanApplicationRepository.save(loanApplication);
	}

	

	private double getInterestRateByLoanType(LoanType loanType) {
		switch (loanType) {
        case HOMELOAN: return 6.5;
        case CARLOAN: return 7.5;
        case EDUCATIONLOAN: return 5.0;
        case PERSONALOAN: return 10.0;
        default: return 8.0; // Default rate for unknown loan types
    }
		
	}
	
	private double calculateEmi(double principal, double annualInterestRate, int tenureInMonths) {
	    double monthlyInterestRate = annualInterestRate / (12 * 100); // Convert annual rate to monthly
	    return (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureInMonths)) /
	           (Math.pow(1 + monthlyInterestRate, tenureInMonths) - 1);
	}

	@Override
	public LoanApplication getLoanInfoById(int loanApplicationId) throws LoanApplicationNotFoundException{
		// TODO Auto-generated method stub
	return loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(()-> new LoanApplicationNotFoundException("LoanApplication with Id :"+loanApplicationId+"is Not Found..."));
		
	}



	@Override
	public List<LoanApplication> getLoanDetailsByAccountId(int accountId) throws LoanApplicationNotFoundException{
	 AccountDto accountById = accountClient.getAccountById(accountId);
		
 List<LoanApplication> loanApplications= loanApplicationRepository.findByAccountId(accountId);
 
 if(loanApplications.isEmpty()) {
	 throw new LoanApplicationNotFoundException("LoanApplication is Not Found With AccountId :"+accountId);
 }
 return loanApplications;
	}

}
