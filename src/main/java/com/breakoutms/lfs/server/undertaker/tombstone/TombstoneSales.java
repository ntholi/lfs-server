package com.breakoutms.lfs.server.undertaker.tombstone;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.sales.model.Quotation;

public class TombstoneSales {

	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, 
			cascade = CascadeType.PERSIST)
	private Corpse corpse;

	@OneToOne
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY, 
			cascade = CascadeType.ALL)
	private Quotation quotation;
	
	private LocalDate erectionDate;
	
	private String erectionType;
	
	private String erectionPlace;
	
	private double totalCost;
}
