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

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.sales.items.ItemType;
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
public class FuneralSchemeBenefit extends AuditableEntity<Integer> {

	public enum Deductable {
		FREE, DEDUCTABLE
	}
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private ItemType itemType;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('FREE','DEDUCTABLE')")
	private Deductable deductable;
	
	@Column(precision=5, scale=4)
	@Digits(integer=1, fraction=4)
	private BigDecimal discount;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;

	
	public boolean isFree() {
		return (deductable != null && deductable == Deductable.FREE);
	}
}
