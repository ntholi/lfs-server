package com.breakoutms.lfs.server.preneed.payment.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.breakoutms.lfs.common.enums.PolicyPaymentType;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PolicyPaymentReport {

	private String policyNumber;
	private String names;
	private String planType;
	private PolicyPaymentType paymentType;
	@JsonIgnore
	private Period _period;
	private BigDecimal amount;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime date;
	
	public String getPeriod() {
		return _period.toString();
	}
}
