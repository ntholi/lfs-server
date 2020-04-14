package lfs.server.preneed.pricing.valid;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.ValidationTest;
import lfs.server.preneed.pricing.FuneralScheme;
import lfs.server.preneed.pricing.FuneralScheme.FuneralSchemeBuilder;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class FuneralSchemeValidations extends ValidationTest<FuneralScheme>{
	
	private FuneralSchemeBuilder builder = FuneralScheme.builder();
	
	@Test
	void name() {
		assertThat(validate(builder.name("").build()))
			.containsKey("name")
			.containsValue(notBlank)
			.size().isEqualTo(1);

		assertThat(validate(builder.name(null).build()))
			.containsKey("name")
			.containsValue(notBlank)
			.size().isEqualTo(1);

		assertThat(validate(builder.name("Hello World").build()))
			.isEmpty();	
	}
	
	
	@Test
	void registrationFee() {
		validatePrecisionAndScale("registrationFee", 6,2);
		
		assertThat(validate(withName().registrationFee(new BigDecimal(-2)).build()))
			.containsKey("registrationFee")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().registrationFee(new BigDecimal("10.234")).build()))
			.containsKey("registrationFee")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().registrationFee(new BigDecimal(1_000_000)).build()))
			.containsKey("registrationFee")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().registrationFee(new BigDecimal("100000.24")).build()))
			.isEmpty();
	}
	
	@Test
	void monthsBeforeActive() {
		assertThat(validate(withName().monthsBeforeActive(-2).build()))
			.containsKey("monthsBeforeActive")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().monthsBeforeActive(256).build()))
			.containsKey("monthsBeforeActive")
			.containsValue(max)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().monthsBeforeActive(255).build()))
			.isEmpty();
	}
	
	@Test
	void penaltyFee() {
		validatePrecisionAndScale("registrationFee", 6,2);
		
		assertThat(validate(withName().penaltyFee(new BigDecimal(-2)).build()))
			.containsKey("penaltyFee")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().penaltyFee(new BigDecimal("10.234")).build()))
			.containsKey("penaltyFee")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().penaltyFee(new BigDecimal(1_000_000)).build()))
			.containsKey("penaltyFee")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().penaltyFee(new BigDecimal("100000.24")).build()))
			.isEmpty();
	}
	
	@Test
	void monthsBeforePenalty() {
		assertThat(validate(withName().monthsBeforePenalty(-2).build()))
			.containsKey("monthsBeforePenalty")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().monthsBeforePenalty(256).build()))
			.containsKey("monthsBeforePenalty")
			.containsValue(max)
			.size().isEqualTo(1);
		
		assertThat(validate(withName().monthsBeforePenalty(255).build()))
			.isEmpty();
	}

	private FuneralSchemeBuilder withName() {
		return builder.name("Hello");
	}
}
