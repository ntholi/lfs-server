package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.jupiter.api.Test;

public class CorpseBeanValidationTest {

	@Test
	void corpse_bean_validations_test_invalid_corpse() throws Exception {
		var validatorFactory = Validation.buildDefaultValidatorFactory();
		var validator = validatorFactory.getValidator();
		Corpse corpse = new Corpse();
		corpse.setNames("T");
		corpse.setSurname("L");
		corpse.setDateOfBirth(LocalDate.now().plusDays(1));
		corpse.setDateOfDeath(LocalDate.now().plusDays(1));
		corpse.setArrivalDate(LocalDateTime.now().plusDays(1));
		
		Set<ConstraintViolation<Corpse>> validations = validator.validate(corpse);
		Map<Object, String> map = validations.stream().collect(Collectors.toMap(
				violation -> violation.getPropertyPath().toString(), 
				ConstraintViolation::getMessage));
		
		assertThat(validations).hasSize(5);
		assertThat(map.get("names")).isEqualTo("Corpse's name is too short");
		assertThat(map.get("surname")).isEqualTo("Corpse's surname is too short");
		assertThat(map.get("dateOfBirth")).isNotNull();
		assertThat(map.get("dateOfDeath")).isNotNull();
		assertThat(map.get("arrivalDate")).isNotNull();
	}
	
	@Test
	void corpse_bean_validations_test_valid_corpse() throws Exception {
		var validatorFactory = Validation.buildDefaultValidatorFactory();
		var validator = validatorFactory.getValidator();
		Corpse corpse = new Corpse();
		corpse.setNames("Neo");
		corpse.setSurname(null);
		corpse.setDateOfBirth(LocalDate.now());
		corpse.setDateOfDeath(LocalDate.now());
		corpse.setArrivalDate(LocalDateTime.now());
		
		Set<ConstraintViolation<Corpse>> validations = validator.validate(corpse);
		
		assertThat(validations).hasSize(0);
	}
}
