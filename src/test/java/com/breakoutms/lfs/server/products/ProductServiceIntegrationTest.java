package com.breakoutms.lfs.server.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.common.ProductType;
import com.breakoutms.lfs.server.common.motherbeans.product.ProductMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.products.model.Coffin;
import com.breakoutms.lfs.server.products.model.Product;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DBRider
@DataSet(value = {"corpse.xml", "product.xml", "sales.xml"})
class ProductServiceIntegrationTest {

	@Autowired ProductRepository repo;
	@Autowired private ProductService service;
	private ProductMapper modelMapper = ProductMapper.INSTANCE;
	private Product entity;
	
	@BeforeEach
	void init() throws IOException {
		entity = new ProductMother()
				.removeIDs()
				.build();
	}
	
	@Test
	void get_by_id() throws IOException {
		var id = 1;
		var savedEntity = service.get(id).orElse(null);
		
		assertThat(savedEntity.getId()).isEqualTo(id);
		assertThat(savedEntity.getName()).isEqualTo("Letter");
		assertThat(savedEntity.getPrice()).isEqualTo(new BigDecimal("10.00"));
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
		var entity = modelMapper.copy(repo.findById(1).get());

		var newValue = "Hello World";
		entity.setName(newValue);;
		
		var updatedEntity = service.update(entity.getId(), fromDTO(entity));
		
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getName()).isEqualTo(newValue);
		assertThat(updatedEntity).isEqualToComparingFieldByField(entity);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456;
		String exMsg = ExceptionSupplier.notFound(Product.class, unknownId).get().getMessage();
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Product());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	
	@Test
	void successful_delete() {
		var id = 1;
		assertThat(repo.findById(id)).isNotEmpty();
		
		service.delete(id);
		assertThat(repo.findById(id)).isEmpty();
	}
	
	@Test
	void save_coffin() throws IOException {
		var entity = new Coffin("ABC", new BigDecimal("20.10"), ProductType.COFFIN_CASKET);
		entity.setCategory("hello_world");
		var savedEntity = service.save(entity);
		
		assertThat(savedEntity).isInstanceOf(Coffin.class);
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
	}
	
	/**
	 * Convert entity object to DTO, then convert it back again to entity object
	 * this is done to simulate that data comes from the controller
	 * @param entity
	 * @return
	 */
	private Product fromDTO(Product entity) {
		var dto = modelMapper.toDTO(entity);
		return modelMapper.map(dto);
	}
}
