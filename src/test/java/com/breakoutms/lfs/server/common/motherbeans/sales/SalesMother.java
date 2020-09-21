package com.breakoutms.lfs.server.common.motherbeans.sales;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.breakoutms.lfs.common.enums.ProductType;
import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.sales.model.BurialDetails;
import com.breakoutms.lfs.server.sales.model.Customer;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.google.common.collect.Lists;

public class SalesMother extends AuditableMother<Sales, Integer> {

	@Override
	public SalesMother removeIDs() {
		entity.setId(null);
		entity.getBurialDetails().setId(null);
		entity.getCorpse().setTagNo(null);
		entity.getQuotation().setId(null);
		entity.getQuotation().getCustomer().setId(null);
		entity.getQuotation().getSalesProducts().forEach(it -> {
			it.setId(null);
			it.getQuotation().setId(null);
		});
		return this;
	}

	public static SalesMother thaboLebese() {
		return new SalesMother()
				.id(7)
				.tagNo("101")
				.customer(new Customer(1, "Thabo Lebese", "58123456"))
				.leavingTime(LocalDateTime.now())
				.serviceTime(LocalDateTime.now())
				.totalCost(new BigDecimal("150"))
				.payableAmount(new BigDecimal("150"))
				.topup(new BigDecimal(0))
				.salesProduct(getSalesProducts())
				.linkSalesProductsToRightQuotation()
				.buyingDate(LocalDate.now());
	}
	
	private static List<SalesProduct> getSalesProducts() {
		SalesProduct item1 = SalesProduct.builder()
				.cost(new BigDecimal("50"))
				.product(new Product("Latter", new BigDecimal("10"), ProductType.LETTERS))
				.quantity(5).build();
		SalesProduct item2 = SalesProduct.builder()
				.cost(new BigDecimal("100"))
				.product(new Product("Coffin_One", new BigDecimal("150"), ProductType.COFFIN_CASKET))
				.quantity(1).build();
		
		return Lists.newArrayList(item1, item2);
	}

	private SalesMother linkSalesProductsToRightQuotation() {
		Quotation quotation = entity.getQuotation();
		if(quotation == null) {
			quotation = new Quotation();
		}
		List<SalesProduct> salesProducts = entity.getQuotation().getSalesProducts();
		if(salesProducts != null) {
			for (SalesProduct item : salesProducts) {
				item.setQuotation(quotation);
			}
		}
		return this;
	}

	@Override
	public SalesMother id(Integer id) {
		entity.setId(id);
		return this;
	}

	public SalesMother tagNo(String tagNo) {
		BurialDetails burialDetails = entity.getBurialDetails();
		if(burialDetails == null) {
			burialDetails = new BurialDetails();
			entity.setBurialDetails(burialDetails);
		}
		Corpse corpse  = new Corpse();
		entity.setCorpse(corpse);
		corpse.setTagNo(tagNo);
		return this;
	}

	public SalesMother buyingDate(LocalDate buyingDate) {
		entity.setBuyingDate(buyingDate);
		return this;
	}

	public SalesMother topup(BigDecimal payableAmount) {
		entity.setPayableAmount(payableAmount);
		return this;
	}

	public SalesMother payableAmount(BigDecimal payableAmount) {
		entity.setPayableAmount(payableAmount);
		return this;
	}

	public SalesMother totalCost(BigDecimal totalCost) {
		entity.setTotalCost(totalCost);
		return this;
	}

	public SalesMother serviceTime(LocalDateTime time) {
		BurialDetails burialDetails = entity.getBurialDetails();
		if(burialDetails == null) {
			burialDetails = new BurialDetails();
			entity.setBurialDetails(burialDetails);
		}
		burialDetails.setServiceTime(time);
		return this;
	}

	public SalesMother leavingTime(LocalDateTime time) {
		BurialDetails burialDetails = entity.getBurialDetails();
		if(burialDetails == null) {
			burialDetails = new BurialDetails();
			entity.setBurialDetails(burialDetails);
		}
		burialDetails.setLeavingTime(time);
		return this;
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
