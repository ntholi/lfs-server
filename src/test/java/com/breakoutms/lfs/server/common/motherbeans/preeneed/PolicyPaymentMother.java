package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import java.util.HashSet;
import java.util.Set;

import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;

public class PolicyPaymentMother extends AuditableMother<PolicyPayment, Long>{

	public PolicyPaymentMother() {
		super();
	}
	
	public PolicyPaymentMother(Policy policy) {
		super();
		entity.setPolicy(policy);
	}

	public PolicyPaymentMother id(long id) {
		entity.setId(id);
		return this;
	}
	
	public PolicyPaymentMother policy(Policy policy) {
		entity.setPolicy(policy);
		return this;
	}

	public PolicyPaymentMother payment(PolicyPaymentDetails payment) {
		var payments = entity.getPolicyPaymentDetails();
		if(payments == null) {
			payments = new HashSet<>();
		}
		payments.add(payment);
		return this;
	}
	
	public PolicyPaymentMother withPremiumForCurrentMonth() {
		Policy policy = entity.getPolicy();
		var payment = PolicyPaymentDetails.premiumOf(Period.now(), policy.getPremiumAmount());
		
		//reset payment details
		Set<PolicyPaymentDetails> paymentDetails = new HashSet<>();
		paymentDetails.add(payment);
		entity.setPolicyPaymentDetails(paymentDetails);
		
		return this;
	}

}
