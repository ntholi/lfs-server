package com.breakoutms.lfs.server.preneed.policy.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.core.enums.District;
import com.breakoutms.lfs.server.core.enums.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "policies")
public class PolicyViewModel extends RepresentationModel<PolicyViewModel>{

	private String policyNumber;
	private String names;
	private String surname;
	private Gender gender;
	private LocalDate dateOfBirth;
	private String phoneNumber;
	private String passportNumber;
	private String nationalIdNumber;
	private String residentialArea;
	private District district;
	private String country;
	private boolean deceased;
	private LocalDate registrationDate;
	private BigDecimal premiumAmount;
	private BigDecimal coverAmount;
	private boolean active;
	private LocalDateTime createdAt;

	public Integer getAge() {
		Integer age = null;
		if(dateOfBirth != null) {
			age = (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
		}
		return age;
	}
}
