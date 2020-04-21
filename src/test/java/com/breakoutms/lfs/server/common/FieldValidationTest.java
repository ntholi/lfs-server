package com.breakoutms.lfs.server.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import com.breakoutms.lfs.server.util.FieldUtils;

public abstract class FieldValidationTest<T> {

	@Value( "${validation.number.positive}" )
	private String positiveNumber;
	private String blank = "must not be blank";
	private String digit = "numeric value out of bounds (<%d digits>.<%d digits> expected)";
	
	private String log = "Validating that %s constrained is available for %s in "+getType();
	private String pastDate = "must be a past date";
	private String pastOrPresent = "must be a date in the past or in the present";

	@Autowired
	private Environment env;
	
	protected void validatePastDate(String fieldName) throws Exception {
		System.out.println(String.format(log, "Past Date", fieldName));
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		field.setAccessible(true);
		
		LocalDate value = LocalDate.now().plusDays(1);
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, Past.class, value))
			.isEqualTo(pastDate);
		
		value = LocalDate.now();
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, Past.class, value))
			.isEqualTo(pastDate);

		
		value = LocalDate.now().minusDays(1);
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
	}
	
	protected void validatePastDateTime(String fieldName) throws Exception {
		System.out.println(String.format(log, "Past DateTime", fieldName));
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		field.setAccessible(true);
		
		LocalDateTime value = LocalDateTime.now().plusDays(1);
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, Past.class, value))
			.isEqualTo(pastDate);
		
		value = LocalDateTime.now();
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, Past.class, value))
			.isEqualTo(pastDate);

		
		value = LocalDateTime.now().minusDays(1);
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
	}
	
	protected void validatePastOrPresentDate(String fieldName) throws Exception {
		System.out.println(String.format(log, "Past or Present Date", fieldName));
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		field.setAccessible(true);
		
		LocalDate value = LocalDate.now().plusDays(1);
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, Past.class, value))
			.isEqualTo(pastOrPresent);

		value = LocalDate.now();
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
		
		value = LocalDate.now().minusDays(1);
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
	}
	
	protected void validatePastOrPresentDateTime(String fieldName) throws Exception {
		System.out.println(String.format(log, "Past or Present DateTime", fieldName));
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		field.setAccessible(true);
		
		LocalDateTime value = LocalDateTime.now().plusDays(1);
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, Past.class, value))
			.isEqualTo(pastOrPresent);

		value = LocalDateTime.now();
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
		
		value = LocalDateTime.now().minusDays(1);
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
	}

	protected void validatePosetiveValue(String fieldName) throws Exception {
		System.out.println(String.format(log, "positive value", fieldName));
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		field.setAccessible(true);
		
		Object val = toMatchingType("-1", field.getType());
		field.set(obj, val);
		assertThat(value(validate(obj), fieldName, Min.class, val))
			.isEqualTo(positiveNumber);
		
		val = toMatchingType("1", field.getType());
		field.set(obj, val);
		assertThat(validate(obj).get(fieldName)).isNull();
	}

	protected void validateBigDecimal(String fieldName, Integer validInteger, Integer validFraction) throws Exception {
		System.out.println(String.format(log, "decimals with <"+validInteger
				+" integer>.<"+validFraction+" decimal>", fieldName));
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		assertNotNull(field, "Did not find field with name '"+fieldName+"' in "+ getType());
		field.setAccessible(true);

		validatePrecisionAndScale(field, validInteger, validFraction);
		
		String validInt = generateInt(validInteger);
		String invalidInt = generateInt(validInteger+1);
		String validFrac = "0."+generateInt(validFraction);
		String invalidFrac = "0."+generateInt(validFraction+1);
		
		String val = invalidFrac;
		field.set(obj, new BigDecimal(val));
		assertThat(value(validate(obj), fieldName, Digits.class, val))
			.isEqualTo(String.format(digit, validInteger, validFraction));
		
		val = invalidInt;
		field.set(obj, new BigDecimal(val));
		assertThat(value(validate(obj), fieldName, Digits.class, val))
			.isEqualTo(String.format(digit, validInteger, validFraction));
		
		field.set(obj, new BigDecimal(validInt));
		assertThat(validate(obj).get(fieldName)).isNull();
		
		field.set(obj, new BigDecimal(validFrac));
		assertThat(validate(obj).get(fieldName)).isNull();

	}

	private void validatePrecisionAndScale(Field field, Integer integer, Integer fraction) {
		assertThat(field.getType()).isEqualTo(BigDecimal.class);
		Column column = field.getAnnotation(Column.class);
		assertNotNull(column, "Fields annotated with @Digits should also be annotated with "
				+ "@Column and provide values for precision and scale");
		assertTrue(column.precision() == (integer + fraction), "precision should be "+ (integer + fraction));
		assertThat(column.scale()).isEqualTo(fraction);
	}

	protected void validateNotBlank(String fieldName) throws Exception {
		System.out.println(String.format(log, "Not Blank", fieldName));
		
		T obj = newObject();
		Field field = FieldUtils.getField(obj.getClass(), fieldName);
		field.setAccessible(true);

		Object value = toMatchingType("", field.getType());
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, NotBlank.class, value))
		.isEqualTo(blank);

		value = null;
		field.set(obj, value);
		assertThat(value(validate(obj), fieldName, NotBlank.class, value))
		.isEqualTo(blank);

		value = toMatchingType("250", field.getType());
		field.set(obj, value);
		assertThat(validate(obj).get(fieldName)).isNull();
	}
	
	private Object toMatchingType(String str, Class<?> type) {
		if(str == null) return null;

		if(BigDecimal.class.isAssignableFrom(type)) {
			return new BigDecimal(str);
		}
		else if(Long.class.isAssignableFrom(type) || Long.TYPE.isAssignableFrom(type)) {
			return Long.valueOf(str);
		}
		else if(Integer.class.isAssignableFrom(type) || Integer.TYPE.isAssignableFrom(type)) {
			return Integer.valueOf(str);
		}
		
		return str;
	}

	private String generateInt(int length) {
		StringBuilder sb = new StringBuilder();
		int preVal;
		for(int i = length; i > 0; i--){
			if(i >= 10) {
				preVal = i - 10;
				preVal = preVal > 0? preVal : 1;
			}
			else preVal = i;
			sb.append(preVal);
		}
		String val = sb.toString();
		return val.isBlank()? "0" : val;
	}

	private T newObject() throws Exception {
		return getType().getDeclaredConstructor().newInstance();
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getType() {
		ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
		Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
		return type;
	}

	private String value(Map<Object, String> map, String fieldName, 
			Class<?> validationType, Object providedValue) {
		String value = map.get(fieldName);
		if(value == null) {
			throw new RuntimeException("Did not find "+validationType.getSimpleName()+" Constraint Violation set for field "+ 
					fieldName+" of "+ getType() +", when testing with value '"+ providedValue+"'");
		}
		if(value.contains("{") && value.contains("}"))
			return env.getProperty(value.replace("{", "").replace("}", ""));
		else return value;
	}

	private Map<Object, String> validate(T entity) {
		var validatorFactory = Validation.buildDefaultValidatorFactory();
		var validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> validations = validator.validate(entity);
		Map<Object, String> map = validations
				.stream().
				collect(Collectors.toMap(
						violation -> violation.getPropertyPath().toString(), 
						ConstraintViolation::getMessage));
		return map;
	}
}
