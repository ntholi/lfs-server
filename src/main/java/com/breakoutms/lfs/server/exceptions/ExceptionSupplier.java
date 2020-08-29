package com.breakoutms.lfs.server.exceptions;

import java.util.function.Supplier;

import com.breakoutms.lfs.server.util.WordUtils;

public final class ExceptionSupplier {

	private static final String NOT_FOUND = "' not found";
	private static final String NO_POLICY_ERROR = "Policy Number '%s' not found";
	private static final String NO_CORPSE_ERROR = "Corpse with tag number '%s' not found";

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
		return nullUpdate(WordUtils.humenize(entityClass.getSimpleName()));
	}
	
	public static Supplier<RuntimeException> policyNotFound(String policyNumber){
		return () ->
			new ObjectNotFoundException(String.format(NO_POLICY_ERROR, policyNumber));
	}
	
	public static Supplier<RuntimeException> corpseNoteFound(String tagNo) {
		return () ->
		new ObjectNotFoundException(String.format(NO_CORPSE_ERROR, tagNo));
	}
	
	public static Supplier<RuntimeException> nullUpdate(String objectName){
		return () ->
			new ObjectNotFoundException(objectName+" object provided is null, cannot update a null object");
	}
}
