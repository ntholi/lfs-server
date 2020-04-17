package lfs.server.exceptions;

import java.util.function.Supplier;

import lfs.server.util.WordUtils;

public final class ExceptionSupplier {

	private static final String NOT_FOUND = "' not found";

	private ExceptionSupplier() {}
	
	public static Supplier<RuntimeException> notFound(String message){
		return () ->
			new ObjectNotFoundException(message);
	}
	
	public static Supplier<RuntimeException> notFound(String objectName, Object id){
		return () ->
			new ObjectNotFoundException(objectName+ " with id '"+ id +NOT_FOUND);
	}
	
	public static Supplier<RuntimeException> notFound(Class<?> entityClass, Object id){
		String objectName = WordUtils.humenize(entityClass.getSimpleName());
		return () ->
			new ObjectNotFoundException(objectName+ " with id '"+ id +NOT_FOUND);
	}
	
	public static Supplier<RuntimeException> notFoundOnUpdate(String objectName){
		return () ->
			new ObjectNotFoundException(objectName+" object provide is null, cannot update a null object");
	}
}
