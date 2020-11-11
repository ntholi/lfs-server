package com.breakoutms.lfs.server.products.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.EmbalmingType;

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
public class EmbalmingPrice extends Product{
	
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('LOCAL', 'RSA', 'RSA_TO_OTHER', 'INTERNATIONAL', 'OTHER')")
	private EmbalmingType embalmingType;
}
