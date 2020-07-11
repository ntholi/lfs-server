package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
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
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.core.enums.PolicyPaymentType;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
        @Index(columnList = "premiumId", name = "unique_premium_payment", unique=true)
})

@SqlResultSetMapping(
	    name="period",
	    classes={
	        @ConstructorResult(
	            targetClass=Period.class,
	            columns={
	                @ColumnResult(name="month", type = Integer.class),
	                @ColumnResult(name="year", type = Integer.class)
	            }
	        )
	    }
	)

@NamedNativeQuery(name="PolicyPaymentDetails.getLastPayedPeriod", 
	query="SELECT month, year FROM policy_payment_details "
			+ " WHERE ( deleted = 0)"
			+ " AND policy_number = :policyNumber"
			+ " ORDER BY premium_id DESC"
			+ " LIMIT 1",
		resultSetMapping="period")

@SQLDelete(sql = "UPDATE policy_payment_details SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class PolicyPaymentDetails extends AuditableEntity<Long> {
	
	@Id
	@GeneratedValue(generator = "policy_payment_details_id")
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "CHAR(15)")
	private PolicyPaymentType type;
	
	@NotNull
	@Embedded
	private Period period;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private BigDecimal amount;
    
	@NotNull
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private PolicyPayment policyPayment;
	
	private boolean markedAsPaid;
	
	@Column(columnDefinition = "CHAR(14)")
	private String premiumId;
	
	// Has been added for faster lookup, allowing lookup by policy 
	// without having to join PolicyPayment table
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "policy_number")
	private Policy policy;
	
	public PolicyPaymentDetails(PolicyPaymentType type, BigDecimal amount, Period period) {
		this.type = type;
		this.amount = amount;
		this.period = period;
	}
	
	public PolicyPaymentDetails(PolicyPaymentType type, BigDecimal amount, Period period, Policy policy) {
		this.type = type;
		this.amount = amount;
		this.period = period;
		this.policy = policy;
	}
	
	public boolean isPenalty() {
		return type != null && type == PolicyPaymentType.PENALTY;
	}
	
	public boolean isPremium() {
		return type != null && type == PolicyPaymentType.PREMIUM;
	}
	
	public boolean isRegistration() {
		return type != null && type == PolicyPaymentType.REGISTRATION;
	}
	
	public static PolicyPaymentDetails premiumOf(Period period, BigDecimal amount) {
		return new PolicyPaymentDetails(PolicyPaymentType.PREMIUM, amount, period);
	}
	
	public static PolicyPaymentDetails penaltyOf(BigDecimal amount) {
		return new PolicyPaymentDetails(PolicyPaymentType.PENALTY, amount, null);
	}
	
	public static PolicyPaymentDetails premiumFor(Policy policy, Period period) {
		PolicyPaymentDetails details =  premiumOf(period, policy.getPremiumAmount());
		details.setPolicy(policy);
		return details;
	}
	
	public static PolicyPaymentDetails premiumFor(Policy policy) {
		return premiumFor(policy, Period.now());
	}
	
	public PolicyPaymentDetails forPolicyPayment(PolicyPayment policyPayment) {
		this.policyPayment = policyPayment;
		return this;
	}

	public boolean hasSamePeriod(PolicyPaymentDetails info) {
		if(info.getPeriod() != null && info.getPeriod() != null) {
			Period period = info.getPeriod();
			return this.period.equals(period);
		}
		return false;
	}
}
