package com.breakoutms.lfs.server.preneed.policy.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.breakoutms.lfs.common.enums.District;
import com.breakoutms.lfs.common.enums.Gender;
import com.breakoutms.lfs.common.enums.PolicyStatus;

public interface PolicyProjection {

	public String getPolicyNumber();
	
	public String getNames();
	
	public String getSurname();
	
	public Gender getGender();
	
	public LocalDate getDateOfBirth();
	
	public String getPhoneNumber();
	
	public District getDistrict();
	
	public LocalDate getRegistrationDate();
	
	public BigDecimal getPremiumAmount();
	
	public BigDecimal getCoverAmount();
	
	public PolicyStatus getStatus();
	
	public String getPassportNumber();
	
	public String getNationalIdNumber();
	
	public String getResidentialArea();
	
	public boolean isDeceased();
}
