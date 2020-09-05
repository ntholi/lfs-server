package com.breakoutms.lfs.server.preneed.payment.report;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PolicyPaymentUser {

	private String names;
	private BigDecimal amount;
}
