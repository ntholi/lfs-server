package com.breakoutms.lfs.server.undertaker.tombstone;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.sales.model.Quotation;

@Entity
public class TombstoneSales {

	private Long id;
	@OneToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="corpse")
	private Corpse corpse;

	@OneToOne
	private Product product;
	
	@ManyToOne
	@Cascade(CascadeType.ALL)
	private Quotation quotation;
	
	private LocalDate erectionDate;
	
	private String erectionType;
	
	private String erectionPlace;
	
	private double totalCost;
}
