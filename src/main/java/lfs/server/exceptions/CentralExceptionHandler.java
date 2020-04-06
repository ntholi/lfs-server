package lfs.server.exceptions;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lfs.server.util.WordUtils;
import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class CentralExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String TIMESTAMP = "timestamp";
	private static final String STATUS = "status";
	private static final String ERROR = "error";
	private static final String STACK_TRACE = "stack_trace";

	@ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex, WebRequest request) {
        Map<String, Object> body = generatBody(ex);
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleException(Throwable ex, WebRequest request) {
        Map<String, Object> body = generatBody(ex);
        body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
        body.put(STACK_TRACE, Arrays.stream(ex.getStackTrace())
        		.map(StackTraceElement::toString)
        		.collect(Collectors.joining("\n")));
        log.error("Error: "+ ex.getMessage(), ex);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	private Map<String, Object> generatBody(Throwable ex) {
		Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(ERROR, ex.getMessage());
		return body;
	}

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
    		HttpHeaders headers, 
    		HttpStatus status, 
    		WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, status.value());
        String[] fields = getFieldsWithErrors(ex.getBindingResult());
        String error = "Invalid input for "+ concat(fields);
        body.put(ERROR, error);
        
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
			sb.append(field);
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
