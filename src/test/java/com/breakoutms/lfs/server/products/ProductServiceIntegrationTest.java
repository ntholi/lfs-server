package com.breakoutms.lfs.server.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;

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
import com.breakoutms.lfs.server.common.motherbeans.product.ProductMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.products.model.Product;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class ProductServiceIntegrationTest {

	@Autowired ProductRepository repo;
	@Autowired
	private ProductService service;
	@Autowired
	private EntityManager entityManager;
	private Product entity;
	
	
	@BeforeEach
	void init() throws IOException {
		entity = new ProductMother()
				.removeIDs()
				.build();
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
	}
	
	@Test
	void update() {
		var entity = new Product();
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
		String exMsg = ExceptionSupplier.notFound(Product.class, unknownId).get().getMessage();
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Product());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	
	@Test
	void successful_delete() {
		var entity = new Product();
		entity.setName("123");
		repo.save(entity);
		var id = entity.getId();
		assertThat(repo.findById(id)).isNotEmpty();
		
		entityManager.flush();
		entityManager.clear();
		
		service.delete(id);
		assertThat(repo.findById(id)).isEmpty();
	}
	
	protected Product persistAndGetCopy(Product entity) {		
		service.save(entity);
		entityManager.flush();
		entityManager.clear();
		
		return DeepCopy.copy(entity, Product.class);
	}
}
