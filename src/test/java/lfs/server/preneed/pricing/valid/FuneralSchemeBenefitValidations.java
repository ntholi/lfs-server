package lfs.server.preneed.pricing.valid;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.ValidationTest;
import lfs.server.preneed.pricing.FuneralSchemeBenefit;
import lfs.server.preneed.pricing.FuneralSchemeBenefit.FuneralSchemeBenefitBuilder;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class FuneralSchemeBenefitValidations extends ValidationTest<FuneralSchemeBenefit>{

	private FuneralSchemeBenefitBuilder builder = FuneralSchemeBenefit.builder();
	
	@Test
	void discount() throws Exception {
		validatePrecisionAndScale("discount", 1,4);
		
		assertThat(valid(builder.discount(new BigDecimal("10.10")).build()))
			.containsKey("discount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.discount(new BigDecimal("2.10012")).build()))
			.containsKey("discount")
			.containsValue(digits)
			.size().isEqualTo(1);
		
		assertThat(valid(builder.discount(new BigDecimal("2")).build())).isEmpty();
		assertThat(valid(builder.discount(new BigDecimal("0.1204")).build())).isEmpty();
	}
}
