package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.sales.model.Sales.PaymentMode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "sales")
public class SalesViewModel extends RepresentationModel<SalesViewModel> {

	private Integer quotationNo;
	
	private String tagNo;
	
	private String customerNames;

	private String phoneNumber;
	
	private LocalDateTime leavingTime;
	
	private LocalDateTime serviceTime;
	
	private String burialPlace;
	
	private String roadStatus;
	
	private String physicalAddress;

	private BigDecimal totalCost;

	private BigDecimal payableAmount;
	
	private BigDecimal topup;
	
	private PaymentMode paymentMode;
	
	private LocalDate buyingDate;
}
