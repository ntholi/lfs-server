package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PolicyPaymentInquiry {

	private String policyNumber;
	private String policyHolder;
	private BigDecimal premium;
	private Period lastPayedPeriod;
	private BigDecimal penaltyDue;
	private BigDecimal premiumDue;
	private BigDecimal paymentDue;
	private List<PolicyPaymentDetailsViewModel> payments;
}
