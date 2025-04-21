package com.banking.accountService.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleAccountNotFoundException(AccountNotFoundException ex){
		Map<String, Object> response=new HashMap<String, Object>();
		response.put("timeStamp", LocalDateTime.now());
		response.put("message", ex.getMessage());
		response.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<Map<String, Object>> handleInsufficientBalanceException(InsufficientBalanceException exception){
		Map<String, Object> res=new HashMap<String, Object>();
		res.put("timeStamp", LocalDateTime.now());
		res.put("message", exception.getMessage());
		res.put("status", HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
}
