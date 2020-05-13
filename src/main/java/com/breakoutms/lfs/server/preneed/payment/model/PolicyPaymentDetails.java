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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.persistence.IdGenerator;

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
        name = "policy_payment_details_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})
@Table(indexes = {
        @Index(columnList = "premiumPaymentId", name = "unique_premium_payment", unique=true)
})
public class PolicyPaymentDetails extends AuditableEntity<Long> {

	public enum Type{
		PREMIUM, PENALTY, REGISTRATION, UPGRADE_FEE
	}
	
	@Id
	@GeneratedValue(generator = "policy_payment_details_id")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "CHAR(15)")
	private Type type;
	
	@Embedded
	private Period period;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private BigDecimal amount;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="policyPayment")
	private PolicyPayment policyPayment;
	
	private boolean markedAsPaid;
	
	@Column(columnDefinition = "CHAR(14)")
	private String premiumPaymentId;
	
	public PolicyPaymentDetails(Type type, BigDecimal amount, Period period) {
		this.type = type;
		this.amount = amount;
		this.period = period;
	}
	
	public static PolicyPaymentDetails premiumOf(Period period, BigDecimal amount) {
		return new PolicyPaymentDetails(Type.PREMIUM, amount, period);
	}
	
	public static PolicyPaymentDetails penaltyOf(BigDecimal amount) {
		return new PolicyPaymentDetails(Type.PENALTY, amount, null);
	}

	public boolean hasSamePeriod(PolicyPaymentDetails info) {
		if(info.getPeriod() != null && info.getPeriod() != null) {
			Period period = info.getPeriod();
			return this.period.equals(period);
		}
		return false;
	}
}
