package com.breakoutms.lfs.server.mortuary;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProductSales {

	@Id
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="corpse_id")
	private Corpse corpse;
}
