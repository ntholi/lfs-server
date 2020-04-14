package lfs.server.preneed.pricing.valid;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.ValidationTest;
import lfs.server.preneed.pricing.Premium;
import lfs.server.preneed.pricing.Premium.PremiumBuilder;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class PremiumValidations extends ValidationTest<Premium>{

	private PremiumBuilder builder = Premium.builder();
	
	@Test
	void minmumAge() {
		assertThat(valid(builder.minmumAge(-1).build()))
			.containsKey("minmumAge")
			.containsValue(negative)
			.size().isEqualTo(1);
		assertThat(valid(builder.minmumAge(256).build()))
			.containsKey("minmumAge")
			.containsValue(max)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.minmumAge(1).build())).isEmpty();
	}
	
	@Test
	void maximumAge() {
		assertThat(valid(builder.maximumAge(-1).build()))
			.containsKey("maximumAge")
			.containsValue(negative)
			.size().isEqualTo(1);
	
		assertThat(valid(builder.maximumAge(256).build()))
			.containsKey("maximumAge")
			.containsValue(max)
			.size().isEqualTo(1);
	
		assertThat(valid(builder.maximumAge(1).build())).isEmpty();
	}
	
	@Test
	void premiumAmount() {
		validatePrecisionAndScale("premiumAmount", 6,2);
		
		assertThat(valid(builder.premiumAmount(new BigDecimal(-1)).build()))
			.containsKey("premiumAmount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.premiumAmount(new BigDecimal("12.123")).build()))
			.containsKey("premiumAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.premiumAmount(new BigDecimal(1_000_000L)).build()))
			.containsKey("premiumAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.premiumAmount(new BigDecimal("100000.00")).build()))
			.isEmpty();
	}
	
	@Test
	void coverAmount() {
		validatePrecisionAndScale("coverAmount", 10,2);
		
		assertThat(valid(builder.coverAmount(new BigDecimal(-1)).build()))
			.containsKey("coverAmount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.coverAmount(new BigDecimal("12.123")).build()))
			.containsKey("coverAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.coverAmount(new BigDecimal(12_000_000_000L)).build()))
			.containsKey("coverAmount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.coverAmount(new BigDecimal("1000000000.00")).build()))
			.isEmpty();
	}
}
