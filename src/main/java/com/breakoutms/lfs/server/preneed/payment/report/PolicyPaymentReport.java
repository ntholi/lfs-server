package com.breakoutms.lfs.server.preneed.payment.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PolicyPaymentReport {

	private String policyNumber;
	private String names;
	private String planType;
	private String paymentType;
	private Period period;
	private BigDecimal amount;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime date;
}
