package com.breakoutms.lfs.server.sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.preneed.deceased.DeceasedClientRepository;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.sales.model.BurialDetails;
import com.breakoutms.lfs.server.sales.model.Customer;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesInquiry;
import com.breakoutms.lfs.server.sales.model.SalesInquiry.SalesInquiryBuilder;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesService {

	private final SalesRepository repo;
	private final DeceasedClientRepository deceasedRepo;
	private final CorpseRepository corpseRepo;

	public Optional<Sales> get(Integer id) {
		return repo.findById(id);
	}

	public Page<Sales> all(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Transactional
	public Sales save(final Sales sales) {
		setAssociations(sales);
		return repo.save(sales);
	}

	@Transactional
	public Sales update(Integer id, Sales updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Sales").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Sales", id));

		setAssociations(updatedEntity);
		SalesMapper.INSTANCE.update(updatedEntity, entity);

		return repo.save(entity);
	}

	protected void setAssociations(final Sales sales) {
		String tagNo = sales.getBurialDetails().getCorpse().getTagNo();
		Corpse corpse = null;
		if(tagNo != null) {
			corpse = corpseRepo.findById(tagNo)
					.orElseThrow(ExceptionSupplier.corpseNoteFound(tagNo));
		}
		
		BurialDetails bd = sales.getBurialDetails();
		if (bd.getId() == null 
				&& bd.getBurialPlace() == null
				&& bd.getLeavingTime() == null
				&& bd.getPhysicalAddress() == null
				&& bd.getRoadStatus() == null
				&& bd.getServiceTime() == null) {
			sales.setBurialDetails(null);
		}
		
		Quotation quotation = sales.getQuotation();
		Customer customer = quotation.getCustomer();
		List<SalesProduct> salesProducts = quotation.getSalesProducts();
		
		if(customer.getId() == null 
				&& customer.getNames() == null
				&& customer.getPhoneNumber() == null) {
			customer = null;
		}
	
		if(corpse != null) {
			quotation = corpse.getQuotation();
			quotation.addSalesProducts(salesProducts);
		}
		quotation.setCustomer(customer);
		sales.setQuotation(quotation);
		if(salesProducts != null) {
			for (SalesProduct salesProduct : salesProducts) {
				salesProduct.setQuotation(quotation);
			}
		}
	}
	
	public SalesInquiry salesInquiry(String tagNo) {
		Optional<DeceasedClient> client = deceasedRepo.findByCorpseTagNo(tagNo);
		SalesInquiryBuilder response = SalesInquiry.builder();
		Corpse corpse = null;
		if(client.isPresent()) {
			var obj = client.get();
			response.payout(obj.getPayout())
				.tagNo(obj.getCorpse().getTagNo())
				.name(obj.getCorpse().getFullName());
			if(obj.getPolicy() != null) {
				response.policyNumber(obj.getPolicy().getPolicyNumber());
			}
			if(obj.getDependent() != null) {
				response.dependentId(obj.getDependent().getId());
			}
			corpse = obj.getCorpse();
		}
		else {
			corpse = corpseRepo.findById(tagNo)
					.orElseThrow(ExceptionSupplier.corpseNoteFound(tagNo));
			response.tagNo(corpse.getTagNo()).name(corpse.getFullName());
		}
		Quotation quot = corpse.getQuotation();
		if(quot != null) {
			List<SalesProduct> salesProducts = quot.getSalesProducts();
			response.salesProducts(SalesMapper.INSTANCE.map(salesProducts));
		}
		SalesInquiry inquiry = response.build();
		if(inquiry.getTagNo() != null) {
			return inquiry;
		}
		return null;
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<SalesProduct> getSalesProducts(Integer quotationNo) {
		return repo.getSalesProducts(quotationNo);
	}
}
