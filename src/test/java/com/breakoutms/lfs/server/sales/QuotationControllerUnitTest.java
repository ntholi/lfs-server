package com.breakoutms.lfs.server.sales;

import static com.breakoutms.lfs.server.common.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.motherbeans.sales.SalesMother;
import com.breakoutms.lfs.server.config.ObjectMapperConfig;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(QuotationController.class)
@Import(ObjectMapperConfig.class)
public class QuotationControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_SALES";

	@Autowired private MockMvc mockMvc;
	@MockBean private SalesRepository repo;
	@SpyBean private SalesService service;
	@MockBean private UserDetailsServiceImpl requiredBean;

	private Sales entity = createEntity();
	private final String URL = "/sales/quotations/";
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void getSalesProducts() throws Exception {
		var quotNo = entity.getQuotation().getId();
		var url = URL+quotNo+"/sales-products";
		var list = entity.getQuotation().getSalesProducts();
		
		when(repo.getSalesProducts(anyInt())).thenReturn(list);
		
		var viewModel = SalesMapper.INSTANCE.map(list);
		
		mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(responseBody().pagedModel("salesProducts").contains(viewModel.get(0)))
			.andReturn();
	}
	
	private Sales createEntity() {
		return SalesMother.thaboLebese().build();
	}
}
