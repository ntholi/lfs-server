package com.breakoutms.lfs.server.exceptions;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class CentralExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String TIMESTAMP = "timestamp";
	private static final String ERROR_CODE = "status";
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	

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
		Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        body.put(ERROR, getErrorName(rootCause));
        body.put(MESSAGE, rootCause.getMessage());
        body.put(ERROR_CODE, errorCode.getCode());
        return new ResponseEntity<>(body, status);
	}

    private String getErrorName(Throwable ex) {
    	String name = ex.getClass().getSimpleName();
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

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(ERROR_CODE, status.value());
        String[] fields = getFieldsWithErrors(ex.getBindingResult());
        String message = "Invalid input for "+ concat(fields);
        body.put(MESSAGE, message);
        body.put(ERROR, "Invalid Input Error");
        
        List<Error> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new Error(x.getField(), x.getDefaultMessage()))
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
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

	class Error {
    	String fieldName;
    	String message;
    	
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String field) {
			this.fieldName = field;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		Error(String field, String message) {
			this.fieldName = field;
			this.message = message;
		}
    }
}
