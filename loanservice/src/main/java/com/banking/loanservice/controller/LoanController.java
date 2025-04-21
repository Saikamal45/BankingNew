package com.banking.loanservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.banking.loanservice.entity.LoanApplication;
import com.banking.loanservice.exception.LoanApplicationNotFoundException;
import com.banking.loanservice.service.LoanService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/loans")
public class LoanController {

	@Autowired
	private LoanService loanService;
	
	@PreAuthorize("hasRole('Customer')")
	@PostMapping("/applyLoan")
	public ResponseEntity<LoanApplication> applyLoan( @RequestBody LoanApplication loanApplication){
		LoanApplication applyForLoan = loanService.applyForLoan(loanApplication);
		return new ResponseEntity<LoanApplication>(applyForLoan,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('Customer')")
	@GetMapping("/getLoanInfoById/{loanApplicationId}")
	public ResponseEntity<LoanApplication> getLoanDetailsById(@PathVariable int loanApplicationId)throws LoanApplicationNotFoundException{
		LoanApplication loanInfoById = loanService.getLoanInfoById(loanApplicationId);
		return new ResponseEntity<LoanApplication>(loanInfoById,HttpStatus.OK);
	}
	
	@GetMapping("/getLoanInfoByAccountId")
	public ResponseEntity<List<LoanApplication>> getLoanDetailsByAccountId(@RequestParam int accountId) throws LoanApplicationNotFoundException{
		List<LoanApplication> loanDetailsByAccountId = loanService.getLoanDetailsByAccountId(accountId);
		return new ResponseEntity<List<LoanApplication>>(loanDetailsByAccountId,HttpStatus.OK);
	}
}

