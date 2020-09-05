package com.breakoutms.lfs.server.preneed.policy.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.breakoutms.lfs.common.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PolicyReport {

	private String policyNumber;
	private String names;
	private Gender gender;
	private LocalDate registrationDate;
	@JsonIgnore private LocalDate dateOfBirth;
	private BigDecimal premiumAmount;
	private BigDecimal coverAmount;
	private String planType;
	
	public Integer getAge() {
		Integer age = null;
		if(dateOfBirth != null) {
			age = (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
		}
		return age;
	}
}
