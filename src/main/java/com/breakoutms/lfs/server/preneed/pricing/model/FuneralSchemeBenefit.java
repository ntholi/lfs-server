package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.ProductType;
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
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@SQLDelete(sql = "UPDATE funeral_scheme_benefit SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class FuneralSchemeBenefit extends AuditableEntity<Integer> {

	public enum Deductable {
		FREE, DEDUCTABLE
	}
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private ProductType productType;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('FREE','DEDUCTABLE')")
	private Deductable deductable;
	
	@Column(precision=5, scale=4)
	@Digits(integer=1, fraction=4)
	private BigDecimal discount;
	
	@ToString.Exclude
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;

	
	public boolean isFree() {
		return (deductable != null && deductable == Deductable.FREE);
	}
}
