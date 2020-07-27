package com.breakoutms.lfs.server.preneed.policy.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import com.breakoutms.lfs.common.enums.District;
import com.breakoutms.lfs.common.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class PolicyDTO {
	private String policyNumber;
	
	@NotBlank
	@Size(min = 2, max = 60)
	private String names;
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String surname;
	
	private Gender gender;
	
	@PastOrPresent
	private LocalDate dateOfBirth;
	
	@Nullable
	@Size(min = 3, max = 50)
	private String phoneNumber;
	
	@Nullable
	@Size(min = 3, max = 50)
	private String passportNumber;

	@Nullable
	@Size(min = 3, max = 40)
	private String nationalIdNumber;
	
	@Nullable
	private String residentialArea;
	
	private District district;
	
	@Nullable
	@Size(min = 3, max = 50)
	private String country;
	
	private boolean deceased;
	
	@NotNull
	private LocalDate registrationDate;
	
	@NotBlank
	private String funeralScheme;

	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	private BigDecimal premiumAmount;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	private BigDecimal coverAmount;
	
	private boolean active;

	private List<Dependent> dependents;
}
