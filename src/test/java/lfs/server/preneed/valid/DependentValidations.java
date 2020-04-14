package lfs.server.preneed.valid;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.ValidationTest;
import lfs.server.preneed.Dependent;
import lfs.server.preneed.Dependent.DependentBuilder;
import lfs.server.preneed.Policy;
import lfs.server.preneed.Policy.PolicyBuilder;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class DependentValidations extends ValidationTest<Dependent>{

	private DependentBuilder builder = Dependent.builder();
	
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
	
	
	private DependentBuilder withRequired() {
		return builder.names("Hello")
				.surname("World");
	}
}
