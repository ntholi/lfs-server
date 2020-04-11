package lfs.server.preneed.pricing.json;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.common.FieldValidationTest;
import lfs.server.preneed.pricing.FuneralScheme;

@TestPropertySource("classpath:messages.properties")
@ExtendWith(SpringExtension.class)
class ValidateFuneralScheme extends FieldValidationTest<FuneralScheme>{

	@Test
	void field_validations() throws Exception {
		
	}
}
