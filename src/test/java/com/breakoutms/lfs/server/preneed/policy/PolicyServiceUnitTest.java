package com.breakoutms.lfs.server.preneed.policy;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.breakoutms.lfs.server.common.motherbeans.preeneed.FuneralSchemeMother;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceUnitTest {

	@Mock private PolicyRepository repo;
	@Mock private FuneralSchemeRepository funeralSchemeRepo;

	@InjectMocks
	private PolicyService service;
	
	private Policy entity = createEntity();

	@Test
	void get_by_id() throws Exception {
		when(repo.findById(entity.getId())).thenReturn(Optional.of(entity));
		Policy response = service.get(entity.getId()).orElse(null);
		assertThat(response).isEqualTo(entity);
	}

	@Test
	void all() {
		PageRequest pagable = PageRequest.of(0, 1);
		when(repo.findAll(pagable)).thenReturn(new PageImpl<Policy>(List.of(entity), pagable, 1));
		
		Page<Policy> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
		assertThat(page.get()).first().isEqualTo(entity);
	}

	@Test
	void save() throws Exception {
		var fs = new FuneralScheme("ABC");
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.of(fs));
		when(funeralSchemeRepo.findPremium(any(FuneralScheme.class), anyInt()))
			.thenReturn(Optional.of(new Premium()));
		when(repo.save(any(Policy.class))).thenReturn(entity);
		Policy response = service.save(Policy.builder()
				.dateOfBirth(LocalDate.now())
				.build(), "");
		assertThat(response)
			.isNotNull()
			.isEqualTo(response);
	}
	
	@Test
	void calculates_premium_and_coverAmount_when_saving_Policy() throws Exception {
		int age = 55;
		FuneralScheme fs = entity.getFuneralScheme();
		entity.setAge(age);
		
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.of(fs));
		when(funeralSchemeRepo.findPremium(any(FuneralScheme.class), anyInt()))
			.thenReturn(FuneralSchemeMother.getPremium(fs, age));
		when(repo.save(entity)).thenReturn(entity);
		
		var saved = service.save(entity, "PLAN D");
		
		assertThat(saved.getFuneralScheme()).isNotNull();
		assertThat(entity.getPremiumAmount()).isEqualTo(new BigDecimal("40.0"));
		assertThat(entity.getCoverAmount()).isEqualTo(new BigDecimal("5000.0"));
	}
	
	@Test
	void expect_notFound_exception_when_Policy_contains_unkown_FuneralScheme_name() throws Exception {
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.empty());
		Throwable thrown = catchThrowable(() -> {
			service.save(new Policy(), "unkown_name");
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
	}
	
	@Test
	void throw_Exception_when_unable_to_get_premium_from_FuneralScheme_with_given_age() 
			throws Exception {
		int age = 67;
		FuneralScheme fs = entity.getFuneralScheme();
		entity.setAge(age);
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.of(fs));
		when(funeralSchemeRepo.findPremium(any(FuneralScheme.class), anyInt()))
			.thenReturn(Optional.empty());
		
		Throwable thrown = catchThrowable(() -> {
			service.save(entity, "PLAN D");
		});
		System.out.println(thrown.getMessage());
		assertThat(thrown).isInstanceOf(InvalidOperationException.class);
	}
	
	@Test
	void update() throws Exception {
		var id = entity.getId();
		FuneralScheme fs = entity.getFuneralScheme();
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.of(fs));
		when(repo.existsById(id)).thenReturn(true);
		when(repo.save(any(Policy.class))).thenReturn(entity);

		Policy response = service.update(id, new Policy(), "");
		assertThat(response)
			.isNotNull()
			.isEqualTo(response);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = "123456";
		String exMsg = ExceptionSupplier.policyNotFound(unknownId).get().getMessage();
		
		when(repo.existsById(unknownId)).thenReturn(false);

		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Policy(), "");
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	@Test
	void delete() {
		var id = entity.getId();
		service.delete(id);
		verify(repo).deleteById(id);
	}
	
	private Policy createEntity() {
		Policy entity = new PolicyMother()
				.funeralScheme(new FuneralSchemeMother()
						.planC()
						.id(20).build())
				.build();
		return entity;
	}
}
