package com.banking.loanservice.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(LoanApplicationNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleLoanApplicationNotFoundException(LoanApplicationNotFoundException ex){
		Map<String, Object> res=new HashMap<String, Object>();
		res.put("timeStamp", LocalDateTime.now());
		res.put("message", ex.getMessage());
		res.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<Map<String,Object>>(res,HttpStatus.NOT_FOUND);
	}
}
