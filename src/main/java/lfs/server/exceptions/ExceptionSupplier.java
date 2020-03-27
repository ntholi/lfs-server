package lfs.server.exceptions;

import java.util.function.Supplier;

public final class ExceptionSupplier {

	private ExceptionSupplier() {}
	
	public static Supplier<RuntimeException> corpseNotFound(String tagNo){
		return () ->
			new ObjectNotFoundException("Corpse with tagNo '"+ tagNo +"' not found");
	}
}
