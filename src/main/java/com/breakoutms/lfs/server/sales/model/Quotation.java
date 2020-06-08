package com.breakoutms.lfs.server.sales.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
        name = "quotation_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE quotation SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Quotation extends AuditableEntity<Integer> {

	@Id
	@GeneratedValue(generator = "quotation_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;
	
	@OneToMany(mappedBy="quotation", 
			cascade=CascadeType.ALL,
			fetch = FetchType.LAZY)
	private List<SalesProduct> salesProducts;
}
