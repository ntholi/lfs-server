package com.breakoutms.lfs.server.preneed.deceased.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.preneed.policy.model.Dependent;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
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
        name = "policy_payment_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})

@SQLDelete(sql = "UPDATE deceased_client SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class DeceasedClient extends AuditableEntity<Long> {

	@Id
	@GeneratedValue(generator = "deceased_client_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "policy_number")
	private Policy policy;

	@ManyToOne
	private Corpse corpse;
	
	@ManyToOne
	private Dependent dependent;
	
	@Digits(integer = 8, fraction = 2)
	@Column(precision = 10, scale = 2)
	private BigDecimal payout;
}
