package com.breakoutms.lfs.server.revenue;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseRepository;
import com.breakoutms.lfs.server.preneed.deceased.DeceasedClientRepository;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueInquiry;
import com.breakoutms.lfs.server.sales.SalesRepository;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RevenueService {

	private final RevenueRepository repo;
	private final DeceasedClientRepository deceasedRepo;
	private final CorpseRepository corpseRepo;
	private final SalesRepository salesRepo;

	public Optional<Revenue> get(Integer id) {
		return repo.findById(id);
	}

	public Page<Revenue> all(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Transactional
	public Revenue save(final Revenue revenue) {
		return repo.save(revenue);
	}

	@Transactional
	public Revenue update(Integer id, Revenue updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Revenue").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Revenue", id));

		RevenueMapper.INSTANCE.update(updatedEntity, entity);

		return repo.save(entity);
	}
	
	public RevenueInquiry revenueInquiry(Integer quotationNo) {
		var sales = salesRepo.findByQuotationId(quotationNo);
//		Optional<DeceasedClient> client = deceasedRepo.findByCorpseTagNo(tagNo);

		return null;
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}

	public List<SalesProduct> getRevenueProducts(Integer quotationNo) {
		return repo.getRevenueProducts(quotationNo);
	}
}
