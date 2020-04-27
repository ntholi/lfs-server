package com.breakoutms.lfs.server.preneed.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.breakoutms.lfs.server.core.entity.District;
import com.breakoutms.lfs.server.core.entity.Gender;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PolicyViewModel extends RepresentationModel<PolicyViewModel>{

	private String policyNumber;
	private String names;
	private String surname;
	private Gender gender;
	private LocalDate dateOfBirth;
	private String phoneNumber;
	private String passportNumber;
	private String nationalIdNnumber;
	private String residentialArea;
	private District district;
	private String country;
	private boolean deceased;
	private LocalDate registrationDate;
	private FuneralScheme funeralScheme;
	private BigDecimal premiumAmount;
	private BigDecimal coverAmount;
	private boolean active;
	private List<Dependent> dependents;
	private LocalDateTime createdAt;

	public int getAge() {
		return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
	}
	
	protected int getAgeAtRegistration() {
		LocalDate regDate = registrationDate;
		if(regDate == null) {
			regDate = getCreatedAt().toLocalDate();
		}
		if(regDate == null) {
			regDate = LocalDate.now();
		}
		return (int) ChronoUnit.YEARS.between(dateOfBirth, regDate);
	}
}
