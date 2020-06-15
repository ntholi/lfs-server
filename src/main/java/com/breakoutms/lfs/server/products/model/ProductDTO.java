package com.breakoutms.lfs.server.products.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	
	@NotBlank
	@Size(min = 1, max = 35)
	@Column(nullable=false, length = 35)
	private String name;
	
	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 7, fraction = 2)
	@Column(nullable=false, precision = 9, scale = 2)
	private BigDecimal price;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(nullable = false, length = 30)
	private ProductType productType;
	
	@Column(length = 205)
	private String description;
	
	@NotBlank
	@Size(min = 1, max = 35)
	@Column(nullable=false, length = 35)
	private String category;
}
