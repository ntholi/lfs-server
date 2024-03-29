package com.breakoutms.lfs.server.preneed.pricing;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

@ExtendWith(MockitoExtension.class)
public class FuneralSchemeServiceUnitTest {

	@Mock private FuneralSchemeRepository repo;
	@InjectMocks private FuneralSchemeService service;
	private final FuneralScheme entity = createEntity();

	@Test
	void get_by_id() throws Exception {
		when(repo.findById(entity.getId())).thenReturn(Optional.of(entity));
		FuneralScheme response = service.get(entity.getId()).orElse(null);
		assertThat(response).isEqualTo(entity);
	}

	@Test
	void all() {
		PageRequest pagable = PageRequest.of(0, 1);
		when(repo.findAll(pagable)).thenReturn(new PageImpl<FuneralScheme>(List.of(entity), pagable, 1));
		
		Page<FuneralScheme> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
		assertThat(page.get()).first().isEqualTo(entity);
	}

	@Test
	void save() throws Exception {
		when(repo.save(any(FuneralScheme.class))).thenReturn(entity);
		FuneralScheme response = service.save(new FuneralScheme());
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void update() throws Exception {
		var id = entity.getId();
		when(repo.findById(id)).thenReturn(Optional.of(entity));
		when(repo.save(any(FuneralScheme.class))).thenReturn(entity);

		FuneralScheme response = service.update(id, new FuneralScheme());
		assertThat(response)
			.isNotNull()
			.isEqualTo(entity);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456;
		String exMsg = ExceptionSupplier.notFound(FuneralScheme.class, unknownId).get().getMessage();
		
		when(repo.findById(unknownId)).thenReturn(Optional.empty());

		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new FuneralScheme());
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
	
	private FuneralScheme createEntity() {
		return new FuneralSchemeMother()
				.build();
	}
}
