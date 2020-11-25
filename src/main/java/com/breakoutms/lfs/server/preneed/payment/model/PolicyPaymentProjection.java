package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

public interface PolicyPaymentProjection {

	long getId();
	
	@Value("#{target.getPolicy().getPolicyNumber()}")
	String getPolicyNumber();

	BigDecimal getAmountTendered();
	
	BigDecimal getChange();
	
	LocalDateTime getPaymentDate();
}
