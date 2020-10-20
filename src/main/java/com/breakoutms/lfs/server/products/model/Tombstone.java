package com.breakoutms.lfs.server.products.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.ProductType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "product_id")
public class Tombstone extends Product{
	
	@NotBlank
	@Size(min = 1, max = 35)
	@Column(nullable=false, length = 35)
	private String category;

	public Tombstone(String name, BigDecimal price, ProductType productType) {
		super(name, price, productType);
	}
}
