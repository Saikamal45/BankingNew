package com.banking.transactionService.exception;

public class TransactionNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionNotFoundException(String msg) {
		super(msg);
	}
}
