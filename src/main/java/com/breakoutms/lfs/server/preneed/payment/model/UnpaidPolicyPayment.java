package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.PolicyPaymentType;
import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "unpaid_policy_payment_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})
@SQLDelete(sql = "UPDATE unpaid_policy_payment SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class UnpaidPolicyPayment extends AuditableEntity<Long>{
	
	@Id
	@GeneratedValue(generator = "unpaid_policy_payment_id")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private PolicyPaymentType type;
	
	@Embedded
	private Period period;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private BigDecimal amount;
    
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Policy policy;
	
	public UnpaidPolicyPayment(PolicyPaymentDetails payment) {
		this.type = payment.getType();
		this.period = payment.getPeriod();
		this.amount = payment.getAmount();
		this.policy = payment.getPolicy();
	}
	
	public PolicyPaymentDetails getPolicyPaymentDetails() {
		PolicyPaymentDetails details = new PolicyPaymentDetails(type, amount, period);
		details.setPolicy(policy);
		return details;
	}
}
