package com.breakoutms.lfs.server.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 6129048725067432509L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
