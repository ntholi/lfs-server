package lfs.server.preneed.pricing.valid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.FieldValidationTest;
import lfs.server.preneed.pricing.FuneralScheme;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
public class FuneralSchemeValidations extends FieldValidationTest<FuneralScheme>{

	@Test
	void field_validations() throws Exception {
		assertTrue(true);
		
		validateNotBlank("name");
		validateBigDecimal("registrationFee", 6,2);
		validatePosetiveValue("registrationFee");
		validatePosetiveValue("monthsBeforeActive");
		validateBigDecimal("penaltyFee", 6,2);
		validatePosetiveValue("penaltyFee");
		validatePosetiveValue("monthsBeforePenalty");
	}
}
