package com.breakoutms.lfs.server.preneed.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyPaymentMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;

@ExtendWith(DBUnitExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DBUnit(allowEmptyFields = true) 
@DataSet({"funeral_scheme.xml", "policy.xml"}) //TODO: figure out how this works
public class PolicyPaymentServiceIntegrationTest {


	@Autowired BranchRepository branchRepo;
	@Autowired FuneralSchemeRepository funeralSchemeRepo;
	@Autowired PolicyRepository policyRepository;
	@Autowired PolicyPaymentService service;
	@Autowired PolicyPaymentRepository repo;
	@Autowired EntityManager entityManager;
	
	private PolicyPayment entity;
	
	@BeforeEach
	void beforeEach() throws Exception {
		entity = createPolicyPayment(getPolicy());
	}
	
	@Test
	void get_by_id() throws Exception {
		
		repo.save(entity);
		PolicyPayment response = service.get(entity.getId()).orElse(null);
		assertThat(response).isEqualTo(entity);
	}
	
	@Test
	void all() {
		PageRequest pagable = PageRequest.of(0, 1);
	
		repo.save(entity);
		
		Page<PolicyPayment> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
		assertThat(page.get()).first().isEqualTo(entity);
	}

	@Test
	void save() throws Exception {
		PolicyPayment entity = new PolicyPaymentMother(getPolicy())
				.removeId()
				.withPremiumForCurrentMonth()
				.build();
		
		PolicyPayment response = service.save(entity, getPolicy().getId());
		
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void update() throws Exception {

		var before = new BigDecimal("300");
		var after = new BigDecimal("400");
		
		entity.setAmountTendered(before);
		assertThat(entity.getAmountTendered()).isEqualTo(before);
		Entry<Long, PolicyPayment> result = persistAndGetCopy(entity);
		var copy = result.getValue();
		var id = result.getKey();
		
		copy.setAmountTendered(after);
		var updatedEntity = service.update(id, copy);
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getAmountTendered()).isEqualTo(after);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456L;
		String exMsg = ExceptionSupplier.notFound(PolicyPayment.class, unknownId)
				.get().getMessage();

		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new PolicyPayment());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	@Test
	void delete() {
		repo.save(entity);
		
		var id = entity.getId();
		service.delete(id);
		
		assertThat(repo.findAll()).isEmpty();
	}
	
	@Test
	void premiumId_is_generated_correctly() throws Exception {
		Policy policy = getPolicy();
		
		LocalDate today = LocalDate.now();
		String premiumId = policy.getPolicyNumber()
				+ String.valueOf(today.getYear()).substring(2)
				+ today.getMonthValue();

		PolicyPayment entity = service.save(new PolicyPaymentMother(policy)
				.removeId()
				.withPremiumForCurrentMonth()
				.build(), policy.getId());

		assertThat(entity).isNotNull();
		assertThat(entity.getId()).isNotNull();
		assertThat(entity.getPolicyPaymentDetails()
				.iterator()
				.next()
				.getPremiumPaymentId()).isEqualTo(premiumId);
	}

	protected Entry<Long, PolicyPayment> persistAndGetCopy(PolicyPayment entity) {		
		service.save(entity, getPolicy().getId());
		entityManager.flush();
		entityManager.clear();
		
		Long id = entity.getId();
		
		entity.setId(null);
//		return DeepCopy.copy(entity);
		return Map.entry(id, entity);
	}
	
	protected PolicyPayment createPolicyPayment(Policy policy) throws Exception {
		return new PolicyPaymentMother(policy)
				.removeId()
				.payment(null)
				.withPremiumForCurrentMonth()
				.build();
	}
	
	private Policy getPolicy() {
		return policyRepository.findById("256070816").get();
	}
}
