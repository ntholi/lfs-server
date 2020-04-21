package com.breakoutms.lfs.server.preneed;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.core.entity.District;
import com.breakoutms.lfs.server.core.entity.Gender;
import com.breakoutms.lfs.server.preneed.pricing.FuneralScheme;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "policyList")
public class PolicyDTO extends RepresentationModel<PolicyDTO>{

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
	private double premium;
	private boolean active;
}
