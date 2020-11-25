package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import com.breakoutms.lfs.common.enums.PaymentMode;

public interface SalesProjection {

	Integer getId();
	
	@Value("#{target.getQuotation().getId()}")
	Integer getQuotationNo();
	
	@Value("#{target.getBurialDetails().getLeavingTime()}")
	LocalDateTime getLeavingTime();
	
	@Value("#{target.getBurialDetails().getBurialPlace()}")
	String getBurialPlace();
	
	@Value("#{target.getBurialDetails().getPhysicalAddress()}")
	String getPhysicalAddress();
	
	BigDecimal getTotalCost();
	
	BigDecimal getPayableAmount();
	
	BigDecimal getTopup();
	
	LocalDate getBuyingDate();
	
	PaymentMode getPaymentMode();
}
