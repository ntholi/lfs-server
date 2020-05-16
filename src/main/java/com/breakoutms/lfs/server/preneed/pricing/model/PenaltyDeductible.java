package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
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
@EqualsAndHashCode(callSuper = true, exclude = "funeralScheme")
@AllArgsConstructor @NoArgsConstructor
public class PenaltyDeductible extends AuditableEntity<Integer> {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Column(columnDefinition = "TINYINT UNSIGNED")
	@Max(255)
	private int months;
	
	@Digits(integer=9, fraction=2)
	@Column(precision=11, scale=2)
	@Min(value = 0L, message = "{validation.number.negative}")
	private BigDecimal amount;
	
	@ToString.Exclude
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;
	
	public PenaltyDeductible(int months, BigDecimal amount) {
		this.months = months;
		this.amount = amount;
	}
}
