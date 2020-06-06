package com.breakoutms.lfs.server.products.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
        name = "product_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@Table(indexes = {
        @Index(columnList = "name", name = "unique_product_name", unique=true),
        @Index(columnList = "productType", name = "index_product_type")
})
@SQLDelete(sql = "UPDATE policy_payment SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
@Inheritance(strategy = InheritanceType.JOINED)
public class Product extends AuditableEntity<Integer>{

	@Id
	@GeneratedValue(generator = "product_id")
	private Integer id;
	
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
}
