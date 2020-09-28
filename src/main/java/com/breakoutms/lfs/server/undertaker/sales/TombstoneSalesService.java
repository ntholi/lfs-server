package com.breakoutms.lfs.server.undertaker.sales;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.undertaker.sales.model.TombstoneSales;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TombstoneSalesService {

	private final TombstoneSalesRepository repo;
	private final CorpseRepository corpseRepo;

	public Optional<TombstoneSales> get(Integer id) {
		return repo.findById(id);
	}

	public Page<TombstoneSales> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public Page<TombstoneSales> search(Specification<TombstoneSales> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }

	@Transactional
	public TombstoneSales save(final TombstoneSales sales) {
		setAssociations(sales);
		
		return repo.save(sales);
	}

	@Transactional
	public TombstoneSales update(Integer id, TombstoneSales updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("TombstoneSales").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("TombstoneSales", id));

		setAssociations(updatedEntity);
		TombstoneSalesMapper.INSTANCE.update(updatedEntity, entity);

		return repo.save(entity);
	}

	protected void setAssociations(final TombstoneSales sales) {
		String tagNo = sales.getCorpse().getTagNo();
		Corpse corpse = null;
		if(tagNo != null) {
			corpse = corpseRepo.findById(tagNo)
					.orElseThrow(ExceptionSupplier.corpseNoteFound(tagNo));
		}
		Quotation quotation = sales.getQuotation();
		List<SalesProduct> salesProducts = quotation.getSalesProducts();

		if(corpse != null) {
			quotation = corpse.getQuotation();
			if(quotation == null) {
				quotation = new Quotation();
				corpse.setQuotation(quotation);
			}
		}
		sales.setQuotation(quotation);
		if(salesProducts != null) {
			for (SalesProduct salesProduct : salesProducts) {
				salesProduct.setQuotation(quotation);
			}
		}
		quotation.setSalesProducts(salesProducts);
	}

	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
