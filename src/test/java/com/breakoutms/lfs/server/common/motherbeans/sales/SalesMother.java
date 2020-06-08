package com.breakoutms.lfs.server.common.motherbeans.sales;

import java.util.List;

import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.sales.model.Customer;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

public class SalesMother extends AuditableMother<Sales, Integer> {

	@Override
	public SalesMother removeIDs() {
		entity.setId(null);
		entity.getBurialDetails().setId(null);
		entity.getBurialDetails().getCorpse().setTagNo(null);
		entity.getQuotation().setId(null);
		entity.getQuotation().getCustomer().setId(null);
		entity.getQuotation().getSalesProducts().forEach(it -> it.setId(null));
		return this;
	}

	public static Sales thaboLebese() {
		return new SalesMother()
				.customer(new Customer(1, "Thabo Lebese", "58123"))
				.build();
	}

	public SalesMother salesProduct(List<SalesProduct> salesProducts) {
		Quotation quotation = entity.getQuotation();
		if(quotation == null) {
			quotation = new Quotation();
		}
		quotation.setSalesProducts(salesProducts);
		entity.setQuotation(quotation);
		return this;
	}
	
	public SalesMother customer(Customer customer) {
		Quotation quotation = entity.getQuotation();
		if(quotation == null) {
			quotation = new Quotation();
		}
		quotation.setCustomer(customer);
		entity.setQuotation(quotation);
		return this;
	}
}
