package com.breakoutms.lfs.server.exceptions;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.breakoutms.lfs.server.util.WordUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class CentralExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AccountNotActiveException.class)
	public ResponseEntity<Object> handleAccountNotActiveException(AccountNotActiveException ex, WebRequest request) {
		return response(HttpStatus.NOT_ACCEPTABLE, 
				ErrorCode.ACCOUNT_NOT_ACTIVE, ex);
	}

	@ExceptionHandler(PaymentAlreadyMadeException.class)
	public ResponseEntity<Object> handlePaymentAlreadyMadeException(PaymentAlreadyMadeException ex, WebRequest request) {
		return response(HttpStatus.NOT_ACCEPTABLE, 
				ErrorCode.PAYMENT_ALREADY_MADE, ex);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
		return response(HttpStatus.CONFLICT, 
				ErrorCode.USER_ALREADY_EXISTS, ex);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		return response(HttpStatus.UNAUTHORIZED, 
				ErrorCode.INVALID_CREDENTIALS, 
				// we don't what to UsernameNotFoundException because that will be giving the user
				// to much information
				new BadCredentialsException("Invalid username/password"));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		return response(HttpStatus.FORBIDDEN, 
				ErrorCode.ACCESS_DENIED, ex);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex, WebRequest request) {
		return response(HttpStatus.NOT_FOUND, 
				ErrorCode.NOT_FOUND, 
				ex);
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<Object> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {
		return response(HttpStatus.NOT_ACCEPTABLE, 
				ErrorCode.INVALID_OPERATION, 
				ex);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleException(Throwable ex, WebRequest request) {
		return response(HttpStatus.INTERNAL_SERVER_ERROR, 
				ErrorCode.INTERNAL_SERVER_ERROR, 
				ex);
	}

	private ResponseEntity<Object> response(HttpStatus status, ErrorCode errorCode, Throwable ex) {
		log.error(ex);
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		ErrorResult body = new ErrorResult(new Date().getTime(), errorCode.getCode(), 
				getErrorName(rootCause), rootCause.getMessage(), null);
		return new ResponseEntity<>(body, status);
	}

	private String getErrorName(Throwable ex) {
		String name = ex.getClass().getSimpleName();
//		if (ex instanceof NullPointerException) {
//			name = TODO: REPLACE EXCEPTION FOR EXCEPTION THAT SHOULD NOT BE EXPOSED TO THE USER
//		}
		
		if(name.endsWith("Exception")) {
			name = name.replace("Exception", "");
		}
		if(!name.endsWith("Error")) {
			name += "Error";
		}
		return WordUtils.humenize(name);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
			HttpHeaders headers, 
			HttpStatus status, 
			WebRequest request) {
		String[] fields = getFieldsWithErrors(ex.getBindingResult());
		String message = "Invalid input for "+ concat(fields);
		String error = "Invalid Input Error";

		List<InvalidFieldError> fieldErrors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> new InvalidFieldError(x.getField(), x.getDefaultMessage()))
				.collect(Collectors.toList());
		
		ErrorResult errorResult = new ErrorResult(new Date().getTime(), status.value(), 
				error, message, fieldErrors);
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
	}

	@Getter @ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ErrorResult {
		private long timestamp;
		private int status;
		private String error;
		private String message;
		private List<InvalidFieldError> fieldErrors;
	}
	
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InvalidFieldError {
		String fieldName;
		String message;
		
		@Override
		public String toString() {
			return String.format("[fieldName: '%s', message: '%s']", fieldName, message);
		}
	}

	private String[] getFieldsWithErrors(BindingResult result) {
		List<FieldError> errorFields = result.getFieldErrors();
		final int size = errorFields.size();
		String[] fields = new String[size];
		for (var i = 0; i < size; i++) {
			FieldError fieldError = errorFields.get(i);
			fields[i] = WordUtils.humenize(fieldError.getField());
		}
		return fields;
	}

	private String concat(String[] fields) {
		StringBuilder sb = new StringBuilder();
		int size = fields.length;
		for (int i = 0; i < size; i++) {
			String field = fields[i];
			sb.append("'").append(field).append("'");
			if(size > 1 && i == size -2) {
				sb.append(" and ");
			}
			else if(i < size -2){
				sb.append(", ");
			}
		}
		return sb.toString();
	}
}
