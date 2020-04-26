package com.breakoutms.lfs.server.preneed.pricing.integ;

import static com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit.Deductable.DEDUCTABLE;
import static com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit.Deductable.FREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.common.copy.DeepCopy;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeService;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FuneralSchemeServiceIntegrationTest {

	@Autowired FuneralSchemeRepository repo;
	@Autowired
	private FuneralSchemeService service;
	@Autowired
	private EntityManager entityManager;
	private FuneralScheme entity;
	
	
	@BeforeEach
	void init() throws IOException {
		entity = FuneralSchemesJSON.withDependanciesButNoIds();
	}
	
	@Test
	void get_by_id() throws IOException {
		var id = service.save(entity).getId();
		var savedEntity = service.get(id).orElse(null);
		
		assertThat(savedEntity.getId()).isEqualTo(id);
		assertThat(savedEntity.getName()).isEqualTo(entity.getName());
	}
	
	@Test
	void all() {
		service.save(entity);
		
		PageRequest pagable = PageRequest.of(0, 1);
		var page = service.all(pagable);
		
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
	}
	
	@Test
	void save() throws IOException {
		var savedEntity = service.save(entity);
		
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
		assertThat(savedEntity.getBenefits()).isNotEmpty();
	}
	
	@Test
	void succesfull_update() {
		var entity = new FuneralScheme();
		var before = "A1";
		var after = "A2";
		
		entity.setName(before);
		assertThat(entity.getName()).isEqualTo(before);
		var copy = persistAndGetCopy(entity);
		
		copy.setName(after);
		var updatedEntity = service.update(copy.getId(), copy);
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getName()).isEqualTo(after);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456;
		String exMsg = ExceptionSupplier.notFound(FuneralScheme.class, unknownId).get().getMessage();
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new FuneralScheme());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	
	@Test
	void successful_delete() {
		var entity = new FuneralScheme();
		entity.setName("123");
		repo.save(entity);
		var id = entity.getId();
		assertThat(repo.findById(id)).isNotEmpty();
		
		entityManager.flush();
		entityManager.clear();
		
		service.delete(id);
		assertThat(repo.findById(id)).isEmpty();
	}
	
	@Test
	void update_premium() throws Exception {
		var copy = persistAndGetCopy(entity);
		var premium = copy.getPremiums().get(0);
		int size = copy.getPremiums().size();
		var id = premium.getId();
		
		var newVal = premium.getCoverAmount().subtract(new BigDecimal("10"));
		premium.setCoverAmount(newVal);
		
		var updatedEntity = service.update(copy.getId(), copy);
		Premium update = null;
		for (Premium p : updatedEntity.getPremiums()) {
			if(p.getId().equals(id)) {
				update = p;
				break;
			}
		}
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(update).isNotNull();
		assertThat(update.getId()).isEqualTo(premium.getId());
		assertThat(update.getCoverAmount()).isEqualTo(newVal);
		assertThat(updatedEntity.getPremiums().size()).isEqualTo(size);
	}
	
	@Test
	void update_dependentBenefits() throws Exception {
		var copy = persistAndGetCopy(entity);
		DependentBenefit dependentBenefit = copy.getDependentBenefits().get(0);
		int size = copy.getDependentBenefits().size();
		var id = dependentBenefit.getId();
		
		var newVal = dependentBenefit.getCoverAmount().subtract(new BigDecimal("10"));
		dependentBenefit.setCoverAmount(newVal);
		
		var updatedEntity = service.update(copy.getId(), copy);
		DependentBenefit update = null;
		for (DependentBenefit p : updatedEntity.getDependentBenefits()) {
			if(p.getId().equals(id)) {
				update = p;
				break;
			}
		}
		
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(update).isNotNull();
		assertThat(update.getId()).isEqualTo(dependentBenefit.getId());
		assertThat(update.getCoverAmount()).isEqualTo(newVal);
		assertThat(updatedEntity.getDependentBenefits().size()).isEqualTo(size);
	}

	@Test
	void update_funeralSchemeBenefit() throws Exception {
		var copy = persistAndGetCopy(entity);
		var benefit = copy.getBenefits().get(0);
		int size = copy.getBenefits().size();
		var id = benefit.getId();
		
		var newVal = benefit.getDeductable() == FREE? DEDUCTABLE : FREE;
		benefit.setDeductable(newVal);
		
		var updatedEntity = service.update(copy.getId(), copy);
		FuneralSchemeBenefit update = null;
		for (var p : updatedEntity.getBenefits()) {
			if(p.getId().equals(id)) {
				update = p;
				break;
			}
		}
		
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(update).isNotNull();
		assertThat(update.getId()).isEqualTo(benefit.getId());
		assertThat(update.getDeductable()).isEqualTo(newVal);
		assertThat(updatedEntity.getBenefits().size()).isEqualTo(size);
	}
	
	@Test
	void update_penaltyDeductible() throws Exception {
		var copy = persistAndGetCopy(entity);
		var deductable = copy.getPenaltyDeductibles().get(0);
		int size = copy.getPenaltyDeductibles().size();
		var id = deductable.getId();
		
		var newVal = deductable.getAmount().subtract(new BigDecimal("10"));
		deductable.setAmount(newVal);
		
		var updatedEntity = service.update(copy.getId(), copy);
		PenaltyDeductible update = null;
		for (var p : updatedEntity.getPenaltyDeductibles()) {
			if(p.getId().equals(id)) {
				update = p;
				break;
			}
		}
		
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(update.getId()).isEqualTo(deductable.getId());
		assertThat(update.getAmount()).isEqualTo(newVal);
		assertThat(updatedEntity.getPenaltyDeductibles().size()).isEqualTo(size);
	}
	
	
	protected FuneralScheme persistAndGetCopy(FuneralScheme entity) {		
		service.save(entity);
		entityManager.flush();
		entityManager.clear();
		
		FuneralScheme deepCopy = DeepCopy.copy(entity);
		return deepCopy;
	}
}
