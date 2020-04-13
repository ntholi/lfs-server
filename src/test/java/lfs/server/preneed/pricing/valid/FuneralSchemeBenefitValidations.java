package lfs.server.preneed.pricing.valid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.FieldValidationTest;
import lfs.server.preneed.pricing.FuneralSchemeBenefit;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class FuneralSchemeBenefitValidations extends FieldValidationTest<FuneralSchemeBenefit>{

	@Test
	void field_validations() throws Exception {
		assertTrue(true);
		
		validateBigDecimal("discount", 1,4);
	}
}
