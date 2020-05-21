package com.breakoutms.lfs.server.exceptions;

public enum ErrorCode {

	USER_ALREADY_EXISTS(1),
	
	INVALID_CREDENTIALS(2),
	
	ACCESS_DENIED(3),
	
	POLICY_NOT_FOUND(30),
	
	ACCOUNT_NOT_ACTIVE(31),
	
	PAYMENT_ALREADY_MADE(40),
	
	INVALID_OPERATION(50),
	
	NOT_FOUND(404),
	
	INTERNAL_SERVER_ERROR(500);

	private int code;

	ErrorCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
