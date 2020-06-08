package com.breakoutms.lfs.server.sales;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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
import com.breakoutms.lfs.server.common.motherbeans.sales.SalesMother;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.ObjectNotFoundException;
import com.breakoutms.lfs.server.products.ProductRepository;
import com.breakoutms.lfs.server.sales.model.BurialDetails;
import com.breakoutms.lfs.server.sales.model.Customer;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.github.database.rider.core.api.dataset.DataSet;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataSet({"corpse.xml", "product.xml", "sales.xml"})
public class SalesServiceIntegrationTest {

	@Autowired SalesRepository repo;
	@Autowired ProductRepository productRepo;
	@Autowired private SalesService service;
	@Autowired private EntityManager entityManager;
	
	private Sales entity;
	
	
	@BeforeEach
	void init() throws IOException {
		entity = new SalesMother()
				.removeIDs()
				.build();
		SalesProduct s1 = entity.getQuotation().getSalesProducts().get(1);
		SalesProduct s2 = entity.getQuotation().getSalesProducts().get(2);
		s1.setProduct(productRepo.getOne(1));
		s2.setProduct(productRepo.getOne(2));
		entity.getQuotation().setSalesProducts(List.of(s1, s2));
	}
	
	@Test
	void get_by_id() throws IOException {
		var id = 1;
		var savedEntity = service.get(id).orElse(null);
		
		assertThat(savedEntity.getId()).isEqualTo(id);
		assertThat(savedEntity.getTotalCost()).isEqualTo(new BigDecimal("150.00"));
	}
	
	@Test
	void all() {
		
		PageRequest pagable = PageRequest.of(0, 1);
		var page = service.all(pagable);
		
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
	}
	
	@Test
	void save() throws IOException {
		
		List<SalesProduct> salesProducts = entity.getQuotation().getSalesProducts();
		Customer customer = entity.getQuotation().getCustomer();
		BurialDetails burialDetails = entity.getBurialDetails();
		Sales savedEntity = service.save(new Sales(), salesProducts, customer, burialDetails);
		
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
	}
	
	@Test
	void update() {
		var before = new BigDecimal("20");
		var after = new BigDecimal("21");
		
		entity.setPayableAmount(before);
		assertThat(entity.getPayableAmount()).isEqualTo(before);
		var copy = persistAndGetCopy(entity);
		
		copy.setPayableAmount(after);
		var updatedEntity = service.update(copy.getId(), copy);
		assertThat(updatedEntity.getId()).isEqualTo(entity.getId());
		assertThat(updatedEntity.getPayableAmount()).isEqualTo(after);
	}
	
	@Test
	void failt_to_update_with_unknownId() {
		var unknownId = 123456;
		String exMsg = ExceptionSupplier.notFound(Sales.class, unknownId).get().getMessage();
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Sales());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining(exMsg);
	}
	
	
	@Test
	void successful_delete() {
		var id = 1;
		
		assertThat(repo.findById(id)).isNotEmpty();
		
		entityManager.flush();
		entityManager.clear();
		
		service.delete(id);
		assertThat(repo.findById(id)).isEmpty();
	}
	
	protected Sales persistAndGetCopy(Sales entity) {		
		repo.save(entity);
		entityManager.flush();
		entityManager.clear();
		
		return DeepCopy.copy(entity, Sales.class);
	}
}
