package com.breakoutms.lfs.server.exceptions;

public class AccountNotActiveException extends RuntimeException{

	private static final long serialVersionUID = -5967487177479520574L;

	public AccountNotActiveException(String message) {
		super(message);
	}
}
