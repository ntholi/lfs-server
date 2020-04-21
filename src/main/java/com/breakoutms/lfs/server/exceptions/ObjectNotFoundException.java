package com.breakoutms.lfs.server.exceptions;

@SuppressWarnings("serial")
public class ObjectNotFoundException extends RuntimeException {

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
