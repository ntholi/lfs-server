package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
        name = "sales_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE sales SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Sales extends AuditableEntity<Integer> {

	public enum PaymentMode {
		PRENEED,
		SOCIETY,
		CASH,
		OTHER
	}
	
	@Id
	@GeneratedValue(generator = "sales_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Quotation quotation;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private BurialDetails burialDetails;
	
	@Enumerated(EnumType.STRING)
	private PaymentMode paymentMode;
	
	private LocalDate buyingDate;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	@Column(nullable=false, precision = 9, scale = 2)
	private BigDecimal totalCost;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	@Column(nullable=false, precision = 9, scale = 2)
	private BigDecimal payableAmount;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	@Column(nullable=false, precision = 9, scale = 2)
	private BigDecimal topup;
}
