package com.breakoutms.lfs.server.preneed.policy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.breakoutms.lfs.server.common.ValidationTest;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.model.Policy.PolicyBuilder;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class ValidatePolicy extends ValidationTest<Policy>{

	private PolicyBuilder builder = Policy.builder();
	
	@Test
	void names() {
		assertThat(validate(withRequired().names("1").build()))
			.containsKey("names")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().names(string(61)).build()))
			.containsKey("names")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().names(null).build()))
			.containsKey("names")
			.containsValue(notBlank)
			.size().isEqualTo(1);
		
		assertThat(validateMultipe(withRequired().names("").build())
				.get("names"))
			.hasSize(2)
			.contains(size, notBlank);

		assertThat(validate(withRequired().names("Hello World").build()))
			.isEmpty();
		assertThat(validate(withRequired().names(string(60)).build()))
			.isEmpty();	
	}
	
	@Test
	void surname() {
		assertThat(validate(withRequired().surname("1").build()))
			.containsKey("surname")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().surname(string(51)).build()))
			.containsKey("surname")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().surname(null).build()))
			.containsKey("surname")
			.containsValue(notBlank)
			.size().isEqualTo(1);
		
		assertThat(validateMultipe(withRequired().surname("").build())
				.get("surname"))
			.hasSize(2)
			.contains(size, notBlank);
	

		assertThat(validate(withRequired().surname("Hello World").build()))
			.isEmpty();
		assertThat(validate(withRequired().surname(string(50)).build()))
			.isEmpty();	
	}
	
	@Test
	void phoneNumber() {
		assertThat(validate(withRequired().phoneNumber("12").build()))
			.containsKey("phoneNumber")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().phoneNumber("").build()))
			.containsKey("phoneNumber")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().phoneNumber(string(51)).build()))
			.containsKey("phoneNumber")
			.containsValue(size)
			.size().isEqualTo(1);
	

		assertThat(validate(withRequired().phoneNumber("+26612345678").build()))
			.isEmpty();
		assertThat(validate(withRequired().phoneNumber(string(50)).build()))
			.isEmpty();	
	}
	
	@Test
	void dateOfBirth() {
		LocalDate today = LocalDate.now();
		
		assertThat(validate(withRequired().dateOfBirth(today.plusDays(1)).build()))
			.containsKey("dateOfBirth")
			.containsValue(pastOrPresent)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().dateOfBirth(today).build()))
			.isEmpty();
		
		assertThat(validate(withRequired().dateOfBirth(today.minusDays(1)).build()))
			.isEmpty();
	}
	
	@Test
	void nationalIdNumber() {
		assertThat(validate(withRequired().nationalIdNumber("12").build()))
			.containsKey("nationalIdNumber")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().nationalIdNumber("").build()))
			.containsKey("nationalIdNumber")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().nationalIdNumber(string(41)).build()))
			.containsKey("nationalIdNumber")
			.containsValue(size)
			.size().isEqualTo(1);
	

		assertThat(validate(withRequired().nationalIdNumber("236612345678").build()))
			.isEmpty();
		assertThat(validate(withRequired().nationalIdNumber(string(40)).build()))
			.isEmpty();	
	}
	
	@Test
	void residentialArea() {
		assertThat(validate(withRequired().residentialArea("x").build()))
			.containsKey("residentialArea")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().residentialArea("").build()))
			.containsKey("residentialArea")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().residentialArea(string(151)).build()))
			.containsKey("residentialArea")
			.containsValue(size)
			.size().isEqualTo(1);
	

		assertThat(validate(withRequired().residentialArea("TY").build()))
			.isEmpty();
		assertThat(validate(withRequired().residentialArea("Ha Pita, Maseru").build()))
			.isEmpty();
		assertThat(validate(withRequired().residentialArea(string(150)).build()))
			.isEmpty();	
	}
	
	@Test
	void country() {
		assertThat(validate(withRequired().country("TY").build()))
			.containsKey("country")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().country("").build()))
			.containsKey("country")
			.containsValue(size)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().country(string(151)).build()))
			.containsKey("country")
			.containsValue(size)
			.size().isEqualTo(1);
	

		assertThat(validate(withRequired().country("DRC").build()))
			.isEmpty();
		assertThat(validate(withRequired().country("Ha Pita, Maseru").build()))
			.isEmpty();
		assertThat(validate(withRequired().country(string(50)).build()))
			.isEmpty();	
	}
	
	@Test
	void premiumAmount() {
		validatePrecisionAndScale("premiumAmount", 6,2);
		
		assertThat(validate(withRequired().premiumAmount(new BigDecimal(-1)).build()))
			.containsKey("premiumAmount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().premiumAmount(new BigDecimal("12.123")).build()))
			.containsKey("premiumAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().premiumAmount(new BigDecimal(1_000_000L)).build()))
			.containsKey("premiumAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().premiumAmount(new BigDecimal("0.23")).build()))
			.isEmpty();
		assertThat(validate(withRequired().premiumAmount(new BigDecimal("100000.00")).build()))
			.isEmpty();
	}
	
	@Test
	void coverAmount() {
		validatePrecisionAndScale("coverAmount", 8,2);
		
		assertThat(validate(withRequired().coverAmount(new BigDecimal(-1)).build()))
			.containsKey("coverAmount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().coverAmount(new BigDecimal("12.123")).build()))
			.containsKey("coverAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().coverAmount(new BigDecimal(100_000_000)).build()))
			.containsKey("coverAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withRequired().coverAmount(new BigDecimal("0.23")).build()))
			.isEmpty();
		assertThat(validate(withRequired().coverAmount(new BigDecimal(10_000_000)).build()))
			.isEmpty();
		assertThat(validate(withRequired().coverAmount(new BigDecimal("10000000.00")).build()))
			.isEmpty();
	}
	
	private PolicyBuilder withRequired() {
		return builder.names("Hello")
				.surname("World")
				.registrationDate(LocalDate.now());
	}
}
