package com.breakoutms.lfs.server.products;

import static com.breakoutms.lfs.server.common.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.common.motherbeans.product.ProductMother;
import com.breakoutms.lfs.server.config.GeneralConfigurations;
import com.breakoutms.lfs.server.preneed.pricing.model.CoffinViewModel;
import com.breakoutms.lfs.server.products.model.Coffin;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductType;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
@Import(GeneralConfigurations.class)
public class ProductControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_PRODUCTS";

	@Autowired private MockMvc mockMvc;
	@MockBean private ProductRepository repo;
	@SpyBean private ProductService service;
	@MockBean private UserDetailsServiceImpl requiredBean;
	private ProductMapper modelMapper = ProductMapper.INSTANCE;

	private final Integer ID = 7;
	private Product entity = persistedEntity();
	private final String URL = "/products/";

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_by_id() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		
		var viewModel = modelMapper.map(entity);
		
		mockMvc.perform(get(URL+ID))
				.andExpect(status().isOk())
				.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).get(ID);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void verify_links() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));

		mockMvc.perform(get(URL+ID))
				.andExpect(responseBody().hasLink("all", "/products"))
				.andExpect(responseBody().hasLink("self", "/products/"+ID))
				.andExpect(responseBody().hasLink("branch", "/branches/1"));
	}

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = 122423;
		mockMvc.perform(get(URL+unkownId))
			.andExpect(responseBody().notFound(Product.class, unkownId));

		verify(service).get(unkownId);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_all() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		var list = Arrays.asList(entity);
		var pageRequest = PageRequestHelper.from(url);
		
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
		var viewModel = modelMapper.map(entity);
		
		mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(responseBody().isPagedModel())
			.andExpect(responseBody().pageSize().isEqualTo(1))
			.andExpect(responseBody().pagedModel("products").contains(viewModel))
			.andReturn();
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_all_with_no_content_returns_NO_CONTENT_status() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";

		var pageRequest = PageRequestHelper.from(url);
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));

		mockMvc.perform(get(url))
			.andExpect(status().isNoContent());

		verify(service).all(pageRequest);
	}

	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save() throws Exception {
		entity = newEntity();
		when(repo.save(any(Product.class))).thenReturn(entity);

		var dto = modelMapper.toDTO(entity);
		var viewModel = modelMapper.map(entity);
		
		post(mockMvc, URL, dto)
			.andExpect(status().isCreated())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(repo).save(entity);
	}
	
	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		entity.setName(null);

		post(mockMvc, URL, entity)
			.andExpect(responseBody().containsErrorFor("name"));

		verify(service, times(0)).save(any(Product.class));
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update() throws Exception {
		var dto = modelMapper.toDTO(entity);
		var viewModel = modelMapper.map(entity);
		
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		when(repo.save(any(Product.class))).thenReturn(entity);

		put(mockMvc, URL+ID, dto)
			.andExpect(status().isOk())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).update(eq(ID), any(Product.class));
		verify(repo).save(entity);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		entity.setName(null);
		
		put(mockMvc, URL+ID, entity)
			.andExpect(responseBody().containsErrorFor("name"));
	}
	
	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void fail_to_save_invalid_corpse() throws Exception {
		var entity = new Coffin("ABC", new BigDecimal("20.10"), ProductType.COFFIN_CASKET);
		entity.setCategory(null);

		post(mockMvc, URL, entity)
			.andExpect(responseBody().containsErrorFor("category"));

		verify(service, times(0)).save(any(Product.class));
	}

	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_coffin() throws Exception {
		Coffin entity = new Coffin("ABC", new BigDecimal("20.10"), ProductType.COFFIN_CASKET);
		entity.setId(2);
		entity.setCategory("something");
		
		when(repo.save(any(Product.class))).thenReturn(entity);
		
		var dto = modelMapper.toDTO(entity);
		CoffinViewModel viewModel = modelMapper.mapCoffin(entity);
		
		post(mockMvc, URL, dto)
			.andExpect(status().isCreated())
			.andExpect(responseBody().isEqualTo(viewModel));
		
		verify(service).save(any(Coffin.class));
	}

	private Product persistedEntity() {
		return new ProductMother()
				.id(ID)
				.build();
	}
	
	private Product newEntity() {
		return new ProductMother()
				.noBranchNoID()
				.build();
	}
}
