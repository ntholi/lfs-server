package lfs.server.preneed.pricing.valid;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.ValidationTest;
import lfs.server.preneed.pricing.PenaltyDeductible;
@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class PenaltyDeductableValidations extends ValidationTest<PenaltyDeductible>{

	private PenaltyDeductible.PenaltyDeductibleBuilder builder = PenaltyDeductible.builder();
	
	
	@Test
	void monthsBeforeActive() {
		assertThat(validate(builder.months(-2).build()))
			.containsKey("months")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.months(256).build()))
			.containsKey("months")
			.containsValue(max)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.months(255).build()))
			.isEmpty();
	}
	
	@Test
	void amount() throws Exception {
		validatePrecisionAndScale("amount", 9,2);
		
		assertThat(validate(builder.amount(new BigDecimal(-2)).build()))
			.containsKey("amount")
			.containsValue(negative)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.amount(new BigDecimal("10.102")).build()))
			.containsKey("amount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.amount(new BigDecimal(1_000_000_000)).build()))
			.containsKey("amount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(validate(builder.amount(new BigDecimal(100_000_000)).build())).isEmpty();
		assertThat(validate(builder.amount(new BigDecimal("0.12")).build())).isEmpty();
	}
}
