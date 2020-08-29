package com.breakoutms.lfs.server.revenue.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
import com.breakoutms.lfs.server.sales.model.Quotation;

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
        name = "revenue_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE revenue SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Revenue extends AuditableEntity<Integer> {

	@Id
	@GeneratedValue(generator = "revenue_id")
	private Integer receiptNo;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Quotation quotation;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal balance;
	
	@NotNull
	@Column(nullable = false)
	private LocalDateTime date;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal amountTendered;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	@Column(name = "change_amount", nullable=false, precision = 9, scale = 2)
	private BigDecimal change;

	@Override
	public Integer getId() {
		return receiptNo;
	}
}
