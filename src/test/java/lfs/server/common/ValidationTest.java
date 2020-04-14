package lfs.server.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.springframework.beans.factory.annotation.Value;

import lfs.server.util.FieldUtils;

public abstract class ValidationTest<T> {
	
	@Value( "{validation.number.negative}" )
	protected String negative;
	
	@Value( "{validation.number.short}" )
	protected String shortString;
	
	@Value("{javax.validation.constraints.Max.message}")
	protected String max;
	
	@Value("{javax.validation.constraints.Digits.message}")
	protected String digits;
	
	@Value("{javax.validation.constraints.NotBlank.message}")
	protected String notBlank;
	
	protected Map<String, String> valid(T entity) {
		var validatorFactory = Validation.buildDefaultValidatorFactory();
		var validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> validations = validator.validate(entity);
		Map<String, String> map = validations
				.stream().
				collect(Collectors.toMap(
						v -> v.getPropertyPath().toString(), 
						v -> {
							return v.getMessageTemplate();
						}));
		return map;
	}
	
	protected void validatePrecisionAndScale(String fieldName, Integer integer, Integer fraction) {
		Field field = FieldUtils.getField(getType(), fieldName);
		assertNotNull(field, "Did not find field with name '"+fieldName+"' in "+ getType());
		field.setAccessible(true);
		
		assertThat(field.getType()).isEqualTo(BigDecimal.class);
		Column column = field.getAnnotation(Column.class);
		assertNotNull(column, "Fields annotated with @Digits should also be annotated with "
				+ "@Column and provide values for precision and scale");
		assertTrue(column.precision() == (integer + fraction), "precision should be "+ (integer + fraction));
		assertThat(column.scale()).isEqualTo(fraction);
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getType() {
		ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
		Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
		return type;
	}
}
