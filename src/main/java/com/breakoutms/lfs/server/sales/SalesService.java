package com.breakoutms.lfs.server.sales;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.common.enums.ReleasedCorpseStatus;
import com.breakoutms.lfs.common.enums.TransportType;
import com.breakoutms.lfs.common.enums.VehicleOwner;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.embalming.EmbalmingRepository;
import com.breakoutms.lfs.server.mortuary.postmortem.PostmortemRepository;
import com.breakoutms.lfs.server.mortuary.released.ReleasedCorpseRepository;
import com.breakoutms.lfs.server.mortuary.released.model.ReleasedCorpse;
import com.breakoutms.lfs.server.preneed.deceased.DeceasedClientRepository;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.products.ProductRepository;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.sales.model.BurialDetails;
import com.breakoutms.lfs.server.sales.model.Customer;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesInquiry;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalesService {

	private final SalesRepository repo;
	private final EmbalmingRepository embalmingRepo;
	private final PostmortemRepository postmortemRepo;
	private final DeceasedClientRepository deceasedRepo;
	private final CorpseRepository corpseRepo;
	private final ProductRepository productRepo;
	private final ReleasedCorpseRepository releasedCorpseRepository;

	public Optional<Sales> get(Integer id) {
		return repo.findById(id);
	}

	public Page<Sales> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public Page<Sales> search(Specification<Sales> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }

	@Transactional
	public Sales save(final Sales sales) {
		setAssociations(sales);
		
		BurialDetails burial = sales.getBurialDetails();
		if(burial != null && burial.getLeavingTime() != null) {
			Corpse corpse = sales.getCorpse();
			var leavingTime = burial.getLeavingTime();
			if(leavingTime.isAfter(LocalDate.now().atStartOfDay())) {
				ReleasedCorpse releasedCorpse = new ReleasedCorpse();
				releasedCorpse.setCorpse(corpse);
				releasedCorpse.setBurialDetails(burial);
				releasedCorpse.setStatus(ReleasedCorpseStatus.PENDING);
				releasedCorpseRepository.save(releasedCorpse);
			}
		}
		
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
		String tagNo = sales.getCorpse().getTagNo();
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
			if(quotation == null) {
				quotation = new Quotation();
				corpse.setQuotation(quotation);
			}
			quotation.addSalesProducts(salesProducts);
		}
		quotation.setCustomer(customer);
		sales.setQuotation(quotation);
		if(salesProducts != null) {
			for (SalesProduct salesProduct : salesProducts) {
				salesProduct.setQuotation(quotation);
			}
		}
		quotation.setSalesProducts(salesProducts);
	}
	
	@Transactional(readOnly = true)
	public SalesInquiry salesInquiry(String tagNo) {
		Optional<DeceasedClient> client = deceasedRepo.findByCorpseTagNo(tagNo);
		SalesInquiry response = new SalesInquiry();
		Corpse corpse = null;
		if(client.isPresent()) {
			var obj = client.get();
			response.setPayout(obj.getPayout());
			response.setTagNo(obj.getCorpse().getTagNo());
			response.setName(obj.getCorpse().getFullName());
			if(obj.getPolicy() != null) {
				response.setPolicyNumber(obj.getPolicy().getPolicyNumber());
			}
			if(obj.getDependent() != null) {
				response.setDependentId(obj.getDependent().getId());
			}
			corpse = obj.getCorpse();
		}
		else {
			corpse = corpseRepo.findById(tagNo)
					.orElseThrow(ExceptionSupplier.corpseNoteFound(tagNo));
			response.setTagNo(corpse.getTagNo());
			response.setName(corpse.getFullName());
		}
		
		Quotation quot = corpse.getQuotation();
		List<SalesProduct> salesProducts = quot.getSalesProducts();
		if(salesProducts != null) {
			salesProducts.addAll(getServices(corpse));
		}
		response.setSalesProducts(SalesMapper.INSTANCE.map(salesProducts));

		if(response.getTagNo() != null) {
			return response;
		}
		return null;
	}

	private List<SalesProduct> getServices(Corpse corpse) {
		List<SalesProduct> list = new ArrayList<>();

		embalmingRepo.findAllByCorpse(corpse).forEach(it ->
			productRepo.findByEmbalmingType(it.getEmbalmingType())
				.ifPresent(product -> list.add(new SalesProduct(product, 1))));
		
		postmortemRepo.findAllByCorpse(corpse).forEach(postmortem -> {
			VehicleOwner going = postmortem.getTransport() != null? 
					postmortem.getTransport().getVehicleOwner() : null;
			VehicleOwner returning = postmortem.getReturnTransport() != null?
					postmortem.getReturnTransport().getVehicleOwner() : null;
			if(going == VehicleOwner.LFS ||
					returning == VehicleOwner.LFS){
				var postmortemRequest = postmortem.getPostmortemRequest().getLocation();
				productRepo.findByTransportByType(TransportType.POSTMORTEM, postmortemRequest)
					.ifPresent(product -> list.add(new SalesProduct(product, 1)));
			}
		});
		
		return list;
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<SalesProduct> getSalesProducts(Integer quotationNo) {
		return repo.getSalesProducts(quotationNo);
	}

	public SalesProduct refrigerationPrice(String tagNo, LocalDateTime leavingDate) {
		Corpse corpse = corpseRepo.findById(tagNo)
				.orElseThrow(ExceptionSupplier.corpseNoteFound(tagNo));
		Product product = productRepo.findByName("Refrigeration")
				.orElseThrow(ExceptionSupplier.notFound("Refrigeration Price Not Found"));
		
		LocalDate start = corpse.getArrivalDate() != null? 
				corpse.getArrivalDate().toLocalDate():
					corpse.getCreatedAt().toLocalDate();
		int days = (int) ChronoUnit.DAYS.between(start, leavingDate);
		days = days > 0? days: 1;
		
		return new SalesProduct(product, days);
	}
}
