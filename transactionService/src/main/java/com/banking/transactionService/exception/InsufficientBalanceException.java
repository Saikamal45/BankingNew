package com.banking.transactionService.exception;

public class InsufficientBalanceException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientBalanceException(String msg) {
		super(msg);
	}
}
