package com.breakoutms.lfs.server.revenue.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "revenues")
public class RevenueViewModel extends RepresentationModel<RevenueViewModel>{

	private Integer receiptNo;

	private Integer quotationNo;
	
	private BigDecimal balance;

	private LocalDateTime date;

	private BigDecimal amountTendered;

	private BigDecimal change;
}
