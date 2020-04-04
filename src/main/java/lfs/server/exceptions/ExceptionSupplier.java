package lfs.server.exceptions;

import java.io.Serializable;
import java.util.function.Supplier;

public final class ExceptionSupplier {

	private ExceptionSupplier() {}
	
	public static Supplier<RuntimeException> notFound(String message){
		return () ->
			new ObjectNotFoundException(message);
	}
	
	public static Supplier<RuntimeException> notFound(String objectName, Serializable id){
		return () ->
			new ObjectNotFoundException(objectName+ " with id '"+ id +"' not found");
	}
	
	public static Supplier<RuntimeException> corpseNotFound(String tagNo){
		return () ->
			new ObjectNotFoundException("Corpse with tagNo '"+ tagNo +"' not found");
	}
}
