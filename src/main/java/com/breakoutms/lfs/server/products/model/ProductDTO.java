package com.breakoutms.lfs.server.products.model;

import java.math.BigDecimal;

import com.breakoutms.lfs.common.enums.EmbalmingType;
import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.common.enums.TransportType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
	
	private String name;

	private BigDecimal price;
	
	private ProductType productType;
	
	private String description;
	
	private String category;
	
	private TransportType transportType;
	
	private String from;
	
	private String to;
	
	private EmbalmingType embalmingType;
}
