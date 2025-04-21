package com.banking.userservice.exception;

public class UserNotFound extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFound(String msg) {
		super(msg);
	}
}
