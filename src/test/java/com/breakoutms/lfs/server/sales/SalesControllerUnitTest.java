package com.breakoutms.lfs.server.sales;

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
import com.breakoutms.lfs.server.common.motherbeans.sales.SalesMother;
import com.breakoutms.lfs.server.config.GeneralConfigurations;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesDTO;
import com.breakoutms.lfs.server.sales.model.SalesViewModel;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest({SalesController.class})
@Import(GeneralConfigurations.class)
public class SalesControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_SALES";

	@Autowired private MockMvc mockMvc;
	@MockBean private SalesRepository repo;
	@SpyBean private SalesService service;
	@MockBean private UserDetailsServiceImpl requiredBean;

	private final Integer ID = 7;
	private Sales entity = createEntity();
	private final String URL = "/sales/";

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_by_id() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		
		var viewModel = SalesMapper.INSTANCE.map(entity);
		
		mockMvc.perform(get(URL+ID))
				.andExpect(status().isOk())
				.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).get(ID);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void verify_links() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));

		int quotNo = entity.getQuotation().getId();
		mockMvc.perform(get(URL+ID))
				.andExpect(responseBody().hasLink("all", "/sales"))
				.andExpect(responseBody().hasLink("self", "/sales/"+ID))
				.andExpect(responseBody().hasLink("branch", "/branches/1"))
				.andExpect(responseBody().hasLink("salesProducts", "/sales/quotations/"+quotNo+"/sales-products"));
	}

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = 122423;
		mockMvc.perform(get(URL+unkownId))
			.andExpect(responseBody().notFound(Sales.class, unkownId));

		verify(service).get(unkownId);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_all() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		var list = Arrays.asList(entity);
		var pageRequest = PageRequestHelper.from(url);
		
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
		var viewModel = SalesMapper.INSTANCE.map(entity);
		
		mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(responseBody().isPagedModel())
			.andExpect(responseBody().pageSize().isEqualTo(1))
			.andExpect(responseBody().pagedModel("sales").contains(viewModel))
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
		entity = entityWithoutIds();
		when(repo.save(any(Sales.class))).thenReturn(entity);

		SalesDTO dto = SalesMapper.INSTANCE.toDTO(entity);
		SalesViewModel viewModel = SalesMapper.INSTANCE.map(entity);
		
		post(mockMvc, URL, dto)
			.andExpect(status().isCreated())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(repo).save(entity);
	}

	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		entity.setPayableAmount(new BigDecimal("-2"));

		post(mockMvc, URL, entity)
			.andExpect(responseBody().containsErrorFor("payableAmount"));

		verify(service, times(0)).save(any(Sales.class));
	}

	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update() throws Exception {
		SalesDTO dto = SalesMapper.INSTANCE.toDTO(entity);
		SalesViewModel viewModel = SalesMapper.INSTANCE.map(entity);
		
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		when(repo.save(any(Sales.class))).thenReturn(entity);

		put(mockMvc, URL+ID, dto)
			.andExpect(status().isOk())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).update(eq(ID), any(Sales.class));
		verify(repo).save(entity);
	}

	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		entity.setPayableAmount(new BigDecimal("-2"));
		
		put(mockMvc, URL+ID, entity)
			.andExpect(responseBody().containsErrorFor("payableAmount"));
	}
	
	private Sales createEntity() {
		return SalesMother.thaboLebese().build();
	}
	
	private Sales entityWithoutIds() {
		return SalesMother.thaboLebese().removeIDs().build();
	}
}
