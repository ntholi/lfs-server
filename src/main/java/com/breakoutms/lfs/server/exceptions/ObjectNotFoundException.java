package com.breakoutms.lfs.server.exceptions;

public class ObjectNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4908492165304579699L;

	public ObjectNotFoundException() {
		super("Object not found");
	}
	
	public ObjectNotFoundException(CharSequence message) {
		super(message.toString());
	}
	
	public ObjectNotFoundException(Number id) {
		super("Object with id '"+id+" not found");
	}

}
