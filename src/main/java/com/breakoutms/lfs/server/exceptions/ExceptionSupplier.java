package com.breakoutms.lfs.server.exceptions;

import java.util.function.Supplier;

import com.breakoutms.lfs.server.util.WordUtils;

public final class ExceptionSupplier {

	private static final String NOT_FOUND = "' not found";

	private ExceptionSupplier() {}
	
	public static Supplier<RuntimeException> notFound(Class<?> entityClass, Object id){
		return notFound(WordUtils.humenize(entityClass.getSimpleName()), id);
	}
	
	public static Supplier<RuntimeException> notFound(String objectName, Object id){
		return notFound(objectName+ " with id '"+ id +NOT_FOUND);
	}

	public static Supplier<RuntimeException> notFound(String message){
		return () ->
			new ObjectNotFoundException(message);
	}
	
	public static Supplier<RuntimeException> notFoundOnUpdate(Class<?> entityClass) {
		return notFoundOnUpdate(WordUtils.humenize(entityClass.getSimpleName()));
	}
	
	public static Supplier<RuntimeException> notFoundOnUpdate(String objectName){
		return () ->
			new ObjectNotFoundException(objectName+" object provide is null, cannot update a null object");
	}
}
