package lfs.server.preneed.pricing.valid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.FieldValidationTest;
import lfs.server.preneed.pricing.DependentBenefit;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class DependentBenefitValidations extends FieldValidationTest<DependentBenefit>{

	@Test
	void field_validations() throws Exception {
		assertTrue(true);
		
		validatePosetiveValue("minmumAge");
		validatePosetiveValue("maximumAge");
		
		validateBigDecimal("coverAmount", 10,2);
		validatePosetiveValue("coverAmount");
	}
}
