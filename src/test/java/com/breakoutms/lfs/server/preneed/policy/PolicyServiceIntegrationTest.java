package com.breakoutms.lfs.server.preneed.policy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.breakoutms.lfs.server.common.motherbeans.preeneed.FuneralSchemeMother;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeService;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DBRider
class PolicyServiceIntegrationTest {

	private static final int AGE = 20;
	private final String ID = "256070816";
	@Autowired private FuneralSchemeService funeralSchemeService;
	@Autowired private FuneralSchemeRepository funeralSchemeRepo;
	@Autowired private PolicyRepository repo;
	@Autowired private PolicyService service;
	@Autowired private EntityManager entityManager;
	private Policy entity;
	private String funeralSchemeName;
	
	
	@BeforeEach
	void init() throws Exception {
		entity = createEntity();
	}

	@Test
	@DataSet(value = {"policy.xml"}, disableConstraints = true)
	void get_by_id() throws IOException {
		var savedEntity = service.get(ID).orElse(null);
		
		assertThat(savedEntity.getId()).isEqualTo(ID);
		assertThat(savedEntity.getNames()).isEqualTo("LIMAKATSO");
	}
	
	@Test
	@DataSet(value = {"policy.xml"}, disableConstraints = true)
	void all() {
		PageRequest pagable = PageRequest.of(0, 20);
		var page = service.all(pagable);
		
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(16);
	}
	
	@Test
	void save() throws IOException {
		var savedEntity = service.save(entity, funeralSchemeName);
		
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
		assertThat(savedEntity.getNames()).isEqualTo(entity.getNames());
		assertThat(savedEntity.getFuneralScheme().getName()).isEqualTo(funeralSchemeName);
	}
	
	@Test
	void calculates_premium_and_coverAmount_when_saving_Policy() throws Exception {
		deleteAllFuneralSchemes();
		int age1 = 55;
		int age2 = 18;
		String planC = "planC";
		String planAPlus = "planAPlus";
		FuneralScheme f1 = new FuneralSchemeMother().planC().name(planC).build();
		FuneralScheme f2 = new FuneralSchemeMother().planAPlus().name(planAPlus).build();
		funeralSchemeService.save(f1);
		funeralSchemeService.save(f2);
		
		Policy policy1 = Policy.builder()
				.dateOfBirth(LocalDate.now().minusYears(age1))
				.registrationDate(LocalDate.now())
				.build();
		
		Policy policy2 = Policy.builder()
				.dateOfBirth(LocalDate.now().minusYears(age2))
				.registrationDate(LocalDate.now())
				.build();
		
		var saved1 = service.save(policy1, planC);
		var saved2 = service.save(policy2, planAPlus);
		
		assertThat(saved1.getFuneralScheme()).isNotNull();
		assertThat(saved1.getFuneralScheme().getName()).isEqualTo(planC);
		assertThat(policy1.getPremiumAmount()).isEqualTo(new BigDecimal("40.0"));
		assertThat(policy1.getCoverAmount()).isEqualTo(new BigDecimal("5000.0"));

		assertThat(saved2.getAge()).isEqualTo(age2);
		assertThat(saved2.getFuneralScheme().getName()).isEqualTo(planAPlus);
		assertThat(policy2.getPremiumAmount()).isEqualTo(new BigDecimal("200.0"));
		assertThat(policy2.getCoverAmount()).isEqualTo(new BigDecimal("15000.0"));
	}
	
	
	@Test
	void expect_notFound_exception_when_Policy_contains_unkown_FuneralScheme_name() throws Exception {
		Throwable thrown = catchThrowable(() -> {
			service.save(new Policy(), "unkown_name");
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
	}
	
	@Test
	void throw_Exception_when_unable_to_get_premium_from_FuneralScheme_with_given_age() 
			throws Exception {
		int age = 76;
		
		entity.setAge(age);
		
		Throwable thrown = catchThrowable(() -> {
			service.save(entity, "PLAN A+");
		});
		System.out.println(thrown.getMessage());
		assertThat(thrown).isInstanceOf(InvalidOperationException.class);
	}
	
	@Test
	void update() {
		entity = repo.save(entity);
		var before = "A1";
		var after = "A2";
		
		entity.setNames(before);
		assertThat(entity.getNames()).isEqualTo(before);
		var copy = persistAndGetCopy(entity);
		
		copy.setNames(after);
		var updatedEntity = service.update(copy.getId(), copy, entity.getFuneralScheme().getName());
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getNames()).isEqualTo(after);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = "123456";
		String exMsg = ExceptionSupplier.policyNotFound(unknownId).get().getMessage();
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Policy(), "");
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	
	@Test
	void successful_delete() {
		entity.setCoverAmount(new BigDecimal("123"));
		entity.setPremiumAmount(new BigDecimal("123"));
		repo.save(entity);
		var id = entity.getId();
		assertThat(repo.findById(id)).isNotEmpty();
		
		entityManager.flush();
		entityManager.clear();
		
		service.delete(id);
		assertThat(repo.findById(id)).isEmpty();
	}
	
//	@Test
//	void update_premium() throws Exception {
//		var copy = persistAndGetCopy(entity);
//		var premium = copy.getPremiums().get(0);
//		int size = copy.getPremiums().size();
//		var id = premium.getId();
//		
//		var newVal = premium.getCoverAmount().subtract(new BigDecimal("10"));
//		premium.setCoverAmount(newVal);
//		
//		var updatedEntity = service.update(copy.getId(), copy);
//		Premium update = null;
//		for (Premium p : updatedEntity.getPremiums()) {
//			if(p.getId().equals(id)) {
//				update = p;
//				break;
//			}
//		}
//		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
//		assertThat(update).isNotNull();
//		assertThat(update.getId()).isEqualTo(premium.getId());
//		assertThat(update.getCoverAmount()).isEqualTo(newVal);
//		assertThat(updatedEntity.getPremiums().size()).isEqualTo(size);
//	}
	
	
	private Policy createEntity() {
		FuneralScheme fs = new FuneralSchemeMother()
				.planAPlus()
				.removeIDs().build();
		funeralSchemeRepo.save(fs);
		funeralSchemeName = fs.getName();
		Policy entity = new PolicyMother()
				.removeIDs()
				.funeralScheme(fs)
				.age(AGE)
				.build();
		return entity;
	}
	
	private void deleteAllFuneralSchemes() {
		funeralSchemeRepo.deleteAll();
		entityManager.flush();
		entityManager.clear();
	}
	
	protected Policy persistAndGetCopy(Policy entity) {
		var funeralSchemeName = entity.getFuneralScheme().getName();
		service.save(entity, funeralSchemeName);
		entityManager.flush();
		entityManager.clear();
		
		Policy deepCopy = DeepCopy.copy(entity);
		return deepCopy;
	}
}
