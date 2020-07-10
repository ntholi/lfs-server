package com.breakoutms.lfs.server.preneed.pricing.valid;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.breakoutms.lfs.server.common.ValidationTest;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium.PremiumBuilder;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class PremiumValidations extends ValidationTest<Premium>{

	private PremiumBuilder builder = Premium.builder();
	
	@Test
	void minimumAge() {
		assertThat(validate(builder.minimumAge(-1).build()))
			.containsKey("minimumAge")
			.containsValue(negative)
			.size().isEqualTo(1);
		assertThat(validate(builder.minimumAge(256).build()))
			.containsKey("minimumAge")
			.containsValue(max)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.minimumAge(1).build())).isEmpty();
	}
	
	@Test
	void maximumAge() {
		assertThat(validate(builder.maximumAge(-1).build()))
			.containsKey("maximumAge")
			.containsValue(negative)
			.size().isEqualTo(1);
	
		assertThat(validate(builder.maximumAge(256).build()))
			.containsKey("maximumAge")
			.containsValue(max)
			.size().isEqualTo(1);
	
		assertThat(validate(builder.maximumAge(1).build())).isEmpty();
	}
	
	@Test
	void premiumAmount() {
		validatePrecisionAndScale("premiumAmount", 6,2);
		
		assertThat(validate(builder.premiumAmount(new BigDecimal(-1)).build()))
			.containsKey("premiumAmount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.premiumAmount(new BigDecimal("12.123")).build()))
			.containsKey("premiumAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.premiumAmount(new BigDecimal(1_000_000L)).build()))
			.containsKey("premiumAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.premiumAmount(new BigDecimal("0.23")).build()))
			.isEmpty();
		assertThat(validate(builder.premiumAmount(new BigDecimal(100_000)).build()))
			.isEmpty();
		assertThat(validate(builder.premiumAmount(new BigDecimal("100000.00")).build()))
			.isEmpty();
	}
	
	@Test
	void coverAmount() {
		validatePrecisionAndScale("coverAmount", 8,2);
		
		assertThat(validate(builder.coverAmount(new BigDecimal(-1)).build()))
			.containsKey("coverAmount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.coverAmount(new BigDecimal("12.123")).build()))
			.containsKey("coverAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.coverAmount(new BigDecimal(100_000_000)).build()))
			.containsKey("coverAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.coverAmount(new BigDecimal("0.23")).build()))
			.isEmpty();
		assertThat(validate(builder.coverAmount(new BigDecimal(10_000_000)).build()))
			.isEmpty();
		assertThat(validate(builder.coverAmount(new BigDecimal("10000000.00")).build()))
			.isEmpty();
	}
}
