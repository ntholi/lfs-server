package com.breakoutms.lfs.server.sales.model;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
        name = "customer_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE customer SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Customer extends AuditableEntity<Integer> {

	@Id
	@GeneratedValue(generator = "customer_id")
	private Integer id;
	
	@Nullable
	@Size(min = 2, max = 60)
	@Column(length = 60)
	private String names;
	
	@Nullable
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String phoneNumber;
}
