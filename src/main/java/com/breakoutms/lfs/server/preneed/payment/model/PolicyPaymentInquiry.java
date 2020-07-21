package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.hateoas.CollectionModel;

import com.breakoutms.lfs.server.preneed.payment.PolicyPaymentMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PolicyPaymentInquiry {

	private String policyNumber;
	private String policyHolder;
	private Integer funeralScheme;
	private BigDecimal premium;
	private Period lastPayedPeriod;
	private Period nextPaymentPeriod;
	private BigDecimal penaltyDue;
	private BigDecimal premiumDue;
	private BigDecimal paymentDue;
	private List<PolicyPaymentDetails> payments;
	
	public CollectionModel<PolicyPaymentDetailsViewModel> getPayments(){
		return CollectionModel.of(PolicyPaymentMapper.INSTANCE.map(payments));
	}
}
