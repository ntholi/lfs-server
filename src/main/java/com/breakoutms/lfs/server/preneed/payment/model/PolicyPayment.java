package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.preneed.model.Policy;

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
        name = "policy_payment_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})
@SQLDelete(sql = "UPDATE policy_payment SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class PolicyPayment extends AuditableEntity<Long> {
	
	@Id
	@GeneratedValue(generator = "policy_payment_id")
	private Long id;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Policy policy;
	
	@NotNull
	private LocalDateTime paymentDate;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private BigDecimal amountTendered;

	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 5, fraction = 2)
	@Column(name = "change_amount", precision = 7, scale = 2)
	private BigDecimal change;

	@OneToMany(mappedBy="policyPayment", 
			cascade=CascadeType.ALL, 
			fetch = FetchType.LAZY)
	@NotEmpty
	private Set<PolicyPaymentDetails> policyPaymentDetails;
}
