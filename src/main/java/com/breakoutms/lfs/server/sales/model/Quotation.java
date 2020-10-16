package com.breakoutms.lfs.server.sales.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.revenue.model.Revenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

	@ManyToOne( fetch = FetchType.LAZY, 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@ToString.Exclude
	private Customer customer;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Corpse corpse;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Sales sales;
	
	@OneToMany(mappedBy="quotation", 
			orphanRemoval = true,
			cascade = CascadeType.ALL)
	private List<SalesProduct> salesProducts;
	
	@OneToMany(mappedBy="quotation", fetch = FetchType.LAZY)
	private List<Revenue> revenues;

	public void addSalesProducts(List<SalesProduct> salesProducts) {
		if(this.salesProducts == null) {
			this.salesProducts = new ArrayList<>();
		}
		this.salesProducts.addAll(salesProducts);
	}
	
	public void setSalesProducts(List<SalesProduct> salesProducts) {
		if(this.salesProducts == null) {
			this.salesProducts = new ArrayList<>();
		}
		this.salesProducts.clear();
		if(salesProducts != null) {
			salesProducts.forEach(it -> it.setId(null));
			this.salesProducts.addAll(salesProducts);
		}
	}
	
	public void addRevenue(Revenue revenue) {
		if(this.revenues == null) {
			this.revenues = new ArrayList<>();
		}
		this.revenues.add(revenue);
	}
}
