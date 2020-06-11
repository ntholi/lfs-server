package com.breakoutms.lfs.server.sales;

import static com.breakoutms.lfs.server.common.ResponseBodyMatchers.responseBody;
import static com.breakoutms.lfs.server.common.SecuredWebTest.READ;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.HypermediaWebTestClientConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.common.motherbeans.sales.SalesMother;
import com.breakoutms.lfs.server.config.GeneralConfigurations;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.mortuary.Corpse;
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductType;
import com.breakoutms.lfs.server.sales.model.BurialDetails;
import com.breakoutms.lfs.server.sales.model.Customer;
import com.breakoutms.lfs.server.sales.model.PaymentMode;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesDTO;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.sales.model.SalesViewModel;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SalesController.class)
@Import(GeneralConfigurations.class)
public class SalesControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_SALES";

	@Autowired private MockMvc mockMvc;
	@MockBean private SalesRepository repo;
	@SpyBean private SalesService service;
	@MockBean private UserDetailsServiceImpl requiredBean;
	@MockBean private BranchRepository branchRepo;

	private final Integer ID = 7;
	private final Sales entity = createEntity();
	private final String URL = "/sales/";

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_by_id() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		
		mockMvc.perform(get(URL+ID))
				.andExpect(status().isOk())
				.andExpect(responseBody().isEqualTo(salesViewModel()));

		verify(service).get(ID);
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
		
		var totalCost = entity.getTotalCost();
		mockMvc.perform(get(url))
			.andExpect(status().isOk())
//			.andExpect(jsonPath("$[*].sales").isArray())
//			.andExpect(jsonPath("$[*].sales[0].totalCost").value(totalCost.toString()))
//			.andExpect(jsonPath("$[*].sales.length()").value(1))
			.andExpect(responseBody().isPagedModel())
			.andExpect(responseBody().contains(salesViewModel()))
			.andReturn();
	}

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
//		ResultActions result = mockMvc.perform(get(url))
//				.andExpect(status().isOk());
//		expect.forPage(result, list, "Saless", url);
//
//		verify(service).all(pageRequest);
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
//		.andExpect(status().isNoContent());
//
//		verify(service).all(pageRequest);
//	}
//
//	@Test
//	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
//	void save() throws Exception {
//		when(repo.save(any(Sales.class))).thenReturn(entity);
//
//		var result = post(mockMvc, URL, entity);
//		result.andDo(print());
//		result.andExpect(status().isCreated());
//		expect.forEntity(result, entity)
//		.andExpect(jsonPath("_links.premiums.href", endsWith(URL+ID+"/premiums")))
//		.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(URL+ID+"/dependent-benefits")))
//		.andExpect(jsonPath("_links.SalesBenefits.href", endsWith(URL+ID+"/funeral-scheme-benefits")))
//		.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(URL+ID+"/penalty-deductibles")));
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
//		var entity = new Sales();
//		entity.setPayableAmount(new BigDecimal("-2"));
//
//		var result = post(mockMvc, URL, entity);
//
//		Expectations.forInvalidFields(result, exMsg);
//
//		verify(service, times(0)).save(any(Sales.class));
//	}
//
//	@Test
//	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
//	void update() throws Exception {
//		when(repo.existsById(ID)).thenReturn(true);
//		when(repo.save(any(Sales.class))).thenReturn(entity);
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
//		when(repo.save(any(Sales.class))).thenReturn(entity);
//
//		var entity = new Sales();
//		entity.setPayableAmount(new BigDecimal("-2"));
//
//		var result = put(mockMvc, URL+ID, entity);
//		Expectations.forInvalidFields(result, exMsg);
//
//		verify(service, times(0)).update(anyInt(), any(Sales.class));
//	}

	//	@Test
	//	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	//	void get_premiums() throws Exception {
	//		List<Premium> value = List.copyOf(entity.getPremiums());
	//		
	//		when(repo.getPremiums(anyInt())).thenReturn(value);
	//		
	//		mockMvc.perform(get(URL+"/"+ID+"/premiums"))
	//			.andDo(print())
	//			.andExpect(status().isOk())
	//			.andExpect(jsonPath("_embedded.premiums[0].premiumAmount").value(value.get(0).getPremiumAmount()))
	//			.andExpect(jsonPath("_embedded.premiums[2].premiumAmount").value(value.get(2).getPremiumAmount()))
	//			.andExpect(jsonPath("_embedded.premiums[0].Sales").doesNotExist())
	//			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/premiums")));
	//
	//		verify(service).getPremiums(ID); 
	//	}
	
	private Sales createEntity() {
		return SalesMother.thaboLebese();
	}

	private SalesDTO salesDTO() {
		BurialDetails burialDetails = entity.getBurialDetails();
		Corpse corpse = burialDetails.getCorpse();
		Customer customer = entity.getQuotation().getCustomer();
		return SalesDTO.builder()
				.tagNo(corpse.getId())
				.customerNames(customer.getNames())
				.phoneNumber(customer.getPhoneNumber())
				.leavingTime(burialDetails.getLeavingTime())
				.serviceTime(burialDetails.getServiceTime())
				.burialPlace(burialDetails.getBurialPlace())
				.roadStatus(burialDetails.getRoadStatus())
				.physicalAddress(burialDetails.getPhysicalAddress())
				.salesProducts(salesProducts())
				.totalCost(entity.getTotalCost())
				.payableAmount(entity.getPayableAmount())
				.topup(entity.getTopup())
				.paymentMode(PaymentMode.CASH)
				.buyingDate(entity.getBuyingDate())
				.build();
	}
	
	private SalesViewModel salesViewModel() {
		BurialDetails burialDetails = entity.getBurialDetails();
		Corpse corpse = burialDetails.getCorpse();
		Quotation quotation = entity.getQuotation();
		Customer customer = quotation.getCustomer();
		SalesViewModel viewModel = SalesViewModel.builder()
				.quotationNo(quotation.getId())
				.tagNo(corpse.getId())
				.customerNames(customer.getNames())
				.phoneNumber(customer.getPhoneNumber())
				.leavingTime(burialDetails.getLeavingTime())
				.serviceTime(burialDetails.getServiceTime())
				.burialPlace(burialDetails.getBurialPlace())
				.roadStatus(burialDetails.getRoadStatus())
				.physicalAddress(burialDetails.getPhysicalAddress())
				.totalCost(entity.getTotalCost())
				.payableAmount(entity.getPayableAmount())
				.topup(entity.getTopup())
				.paymentMode(PaymentMode.CASH)
				.buyingDate(entity.getBuyingDate())
				.build();
		
		viewModel.add(CommonLinks.addLinksWithBranch(SalesController.class, entity.getId(), null));
		
		return viewModel;
	}

	private List<SalesProduct> salesProducts() {
		SalesProduct item1 = SalesProduct.builder()
				.cost(new BigDecimal("50"))
				.product(new Product("Latter", new BigDecimal("10"), ProductType.LETTERS))
				.quantity(5).build();
		SalesProduct item2 = SalesProduct.builder()
				.cost(new BigDecimal("100"))
				.product(new Product("Coffin_One", new BigDecimal("150"), ProductType.COFFIN_CASKET))
				.quantity(1).build();
		
		return List.of(item1, item2);
	}
}
