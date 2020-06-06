//package com.breakoutms.lfs.server.products;
//
//import static org.hamcrest.CoreMatchers.endsWith;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import com.breakoutms.lfs.server.branch.BranchRepository;
//import com.breakoutms.lfs.server.common.ControllerUnitTest;
//import com.breakoutms.lfs.server.common.Expectations;
//import com.breakoutms.lfs.server.common.PageRequestHelper;
//import com.breakoutms.lfs.server.common.motherbeans.product.ProductMother;
//import com.breakoutms.lfs.server.config.GeneralConfigurations;
//import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
//import com.breakoutms.lfs.server.products.model.Product;
//import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(ProductController.class)
//@Import(GeneralConfigurations.class)
//public class ProductControllerUnitTest implements ControllerUnitTest {
//
//	private static final String DEFAULT_ROLE = "ROLE_PRENEED";
//	
//	@Autowired private MockMvc mockMvc;
//	@MockBean private ProductRepository repo;
//	@SpyBean private ProductService service;
//	@MockBean private UserDetailsServiceImpl requiredBean;
//	@MockBean private BranchRepository branchRepo;
//	
//	private final Integer ID = 7;
//	private final Product entity = createEntity();
//	private final String URL = "/preneed/funeral-schemes/";
//
//	private Expectations expect;
//	
//	@BeforeEach
//	public void setup() {
//		expect = new Expectations(URL, entity.getBranch());
//	}
//
//	@Test
//	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
//	void get_by_id() throws Exception {
//		when(repo.findById(ID)).thenReturn(Optional.of(entity));
//	    ResultActions result = mockMvc.perform(get(URL+ID));
//	    result.andExpect(status().isOk());
//	    result.andExpect(jsonPath("_links.premiums.href", endsWith(entity.getId()+"/premiums")));
//	    result.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(entity.getId()+"/dependent-benefits")));
//	    result.andExpect(jsonPath("_links.funeralSchemeBenefits.href", endsWith(entity.getId()+"/funeral-scheme-benefits")));
//	    result.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(entity.getId()+"/penalty-deductibles")));
//	    result.andDo(print());
//	    expect.forEntity(result, entity);
//	    
//	    verify(service).get(ID);
//	}
//	
//	@Test
//	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
//	void get_with_unkown_id_throws_notFound() throws Exception {
//		var unkownId = 122423;
//		String exMsg = ExceptionSupplier.notFound(Product.class, unkownId).get().getMessage();
//		
//		when(repo.findById(anyInt())).thenReturn(Optional.ofNullable(null));
//		
//	    var result = mockMvc.perform(get(URL+unkownId))
//	    		.andExpect(status().isNotFound());
//	    Expectations.forObjectNotFound(result, exMsg);
//	    
//	    verify(service).get(unkownId);
//	}
//	
//	@Test
//	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
//	void get_all() throws Exception {
//
//		var url = URL+"?page=0&size=20&sort=createdAt,desc";
//		
//		var list = Arrays.asList(entity);
//		var pageRequest = PageRequestHelper.from(url);
//		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
//		
//	    ResultActions result = mockMvc.perform(get(url))
//	    		.andExpect(status().isOk());
//	    expect.forPage(result, list, "funeralSchemes", url);
//	    
//	    verify(service).all(pageRequest);
//	}
//	
//	@Test
//	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
//	void get_all_with_no_content_returns_NO_CONTENT_status() throws Exception {
//		var url = URL+"?page=0&size=20&sort=createdAt,desc";
//		
//		var pageRequest = PageRequestHelper.from(url);
//		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));
//		
//		mockMvc.perform(get(url))
//	    		.andExpect(status().isNoContent());
//		
//		verify(service).all(pageRequest);
//	}
//	
//	@Test
//	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
//	void save() throws Exception {
//		when(repo.save(any(Product.class))).thenReturn(entity);
//
//		var result = post(mockMvc, URL, entity);
//		result.andDo(print());
//		result.andExpect(status().isCreated());
//		expect.forEntity(result, entity)
//			.andExpect(jsonPath("_links.premiums.href", endsWith(URL+ID+"/premiums")))
//			.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(URL+ID+"/dependent-benefits")))
//			.andExpect(jsonPath("_links.funeralSchemeBenefits.href", endsWith(URL+ID+"/funeral-scheme-benefits")))
//			.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(URL+ID+"/penalty-deductibles")));
//		
//		verify(service).save(entity);
//	}
//
//
//	@Test
//	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
//	void save_fails_because_of_invalid_field() throws Exception {
//		String exMsg = "Invalid input for 'Name'";
//		
//		var entity = new Product();
//		entity.setName(" ");
//		
//		var result = post(mockMvc, URL, entity);
//		
//	   Expectations.forInvalidFields(result, exMsg);
//	   
//	   verify(service, times(0)).save(any(Product.class));
//	}
//	
//	@Test
//	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
//	void update() throws Exception {
//		when(repo.existsById(ID)).thenReturn(true);
//		when(repo.save(any(Product.class))).thenReturn(entity);
//
//		var result = put(mockMvc, URL+ID, entity);
//		result.andDo(print());
//		
//		result.andExpect(status().isOk());
//		expect.forEntity(result, entity);
//		
//		verify(service).update(ID, entity);
//	}
//	
//	@Test
//	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
//	void update_fails_if_any_field_is_invalid() throws Exception {
//		String exMsg = "Invalid input for 'Name'";
//		when(repo.existsById(ID)).thenReturn(true);
//		when(repo.save(any(Product.class))).thenReturn(entity);
//		
//		var entity = new Product();
//		entity.setName(" ");
//
//		var result = put(mockMvc, URL+ID, entity);
//		Expectations.forInvalidFields(result, exMsg);
//
//		verify(service, times(0)).update(anyInt(), any(Product.class));
//	}
//	
//	private Product createEntity() {
//		return new ProductMother()
//				.planC()
//				.build();
//	}
//}
