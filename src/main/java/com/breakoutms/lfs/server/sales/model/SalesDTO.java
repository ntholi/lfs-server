package com.breakoutms.lfs.server.sales.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.breakoutms.lfs.common.enums.PaymentMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesDTO {
	
	private String tagNo;
	
	@NotBlank
	@Size(min = 2, max = 60)
	@Column(length = 60)
	private String customerNames;
	
	@Nullable
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String phoneNumber;
	
	private LocalDateTime leavingTime;
	
	private LocalDateTime serviceTime;
	
	@Nullable
	@Size(min = 2, max = 150)
	private String burialPlace;
	
	@Nullable
	@Size(min = 2, max = 150)
	private String roadStatus;
	
	@Nullable
	@Size(min = 2, max = 150)
	private String physicalAddress;
	
	private List<SalesProduct> salesProducts;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	private BigDecimal totalCost;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	private BigDecimal payableAmount;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	private BigDecimal topup;
	
	private PaymentMode paymentMode;
	
	private LocalDate buyingDate;
}
