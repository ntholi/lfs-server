package com.breakoutms.lfs.server.revenue.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "revenues")
public class RevenueDTO extends RepresentationModel<RevenueDTO>{

	private Integer receiptNo;

	private Integer quotationNo;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	private BigDecimal balance;
	
	@NotNull
	private LocalDateTime date;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	private BigDecimal amountTendered;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	private BigDecimal change;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	private BigDecimal amountPaid;
}
