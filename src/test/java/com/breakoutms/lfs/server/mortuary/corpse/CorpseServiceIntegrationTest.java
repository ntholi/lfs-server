package com.breakoutms.lfs.server.mortuary.corpse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.common.motherbeans.mortuary.CorpseMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DBRider
@DataSet(value = {"corpse.xml"})
class CorpseServiceIntegrationTest {

	@Autowired CorpseRepository repo;
	@Autowired private CorpseService service;
	private CorpseMapper modelMapper = CorpseMapper.INSTANCE;
	private Corpse entity;
	
	@BeforeEach
	void init() throws IOException {
		entity = new CorpseMother()
				.removeIDs()
				.build();
	}
	
	@Test
	void get_by_id() throws IOException {
		var id = "1";
		var savedEntity = service.get(id).orElse(null);
		
		assertThat(savedEntity.getId()).isEqualTo(id);
		assertThat(savedEntity.getNames()).isEqualTo("Letter");
	}
	
	@Test
	void all() {
		PageRequest pagable = PageRequest.of(0, 10);
		var page = service.all(pagable);
		
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(2);
	}
	
	@Test
	void save() throws IOException {
		var savedEntity = service.save(entity);
		
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
	}
	
	@Test
	void update() {
		var entity = modelMapper.copy(repo.findById("1").get());

		var newValue = "Hello World";
		entity.setNames(newValue);
		
		var updatedEntity = service.update(entity.getId(), fromDTO(entity));
		
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getNames()).isEqualTo(newValue);
		assertThat(updatedEntity).isEqualToComparingFieldByField(entity);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = "123456";
		String exMsg = ExceptionSupplier.notFound(Corpse.class, unknownId).get().getMessage();
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Corpse());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	
	@Test
	void successful_delete() {
		var id = "1";
		assertThat(repo.findById(id)).isNotEmpty();
		
		service.delete(id);
		assertThat(repo.findById(id)).isEmpty();
	}
	
	/**
	 * Convert entity object to DTO, then convert it back again to entity object
	 * this is done to simulate that data comes from the controller
	 * @param entity
	 * @return
	 */
	private Corpse fromDTO(Corpse entity) {
		var dto = modelMapper.map(entity);
		return modelMapper.map(dto);
	}
}
