package com.breakoutms.lfs.server.preneed.payment;

import static com.breakoutms.lfs.server.common.ResponseBodyMatchers.responseBody;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother.PlanType;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyPaymentMother;
import com.breakoutms.lfs.server.config.GeneralConfigurations;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PolicyPaymentController.class)
@Import(GeneralConfigurations.class)
public class PolicyPaymentControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_PRENEED";
	
	@Autowired private MockMvc mockMvc;
	@MockBean private PolicyPaymentRepository repo;
	@SpyBean private PolicyPaymentService service;
	@MockBean private PolicyRepository policyRepo;
	@MockBean private UserDetailsServiceImpl requiredBean;
	@MockBean private BranchRepository branchRepo;
	@MockBean private UnpaidPolicyPaymentRepository unpaidRepo;
	@MockBean private PolicyPaymentDetailsRepository paymentDetailsRepo;
	
	private PreneedMapper modelMapper = PreneedMapper.INSTANCE;
	
	private PolicyPayment entity = persistedEntity();
	private final long ID = 5L;
	private String policyNumber;
	private final String URL;
	
	public PolicyPaymentControllerUnitTest() {
		policyNumber = entity.getPolicy().getPolicyNumber();
		URL = "/preneed/policies/"+policyNumber+"/payments/";
	}

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

		String policyNum = entity.getPolicy().getId();
		mockMvc.perform(get(URL+ID))
				.andExpect(responseBody().hasLink("self", "/preneed/policies/"+policyNum+"/payments/"+ID))
				.andExpect(responseBody().hasLink("all", "/preneed/policies/"+policyNum+"/payments"))
				.andExpect(responseBody().hasLink("paymentDetails", "/preneed/policies/"+policyNum
						+"/payments/"+ID+"/payment-details"))
				.andExpect(responseBody().hasLink("policy", "/preneed/policies/"+policyNum))
				.andExpect(responseBody().hasLink("branch", "/branches/1"))
				.andExpect(jsonPath("policyPaymentDetails").doesNotHaveJsonPath());
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = 122423L;
		mockMvc.perform(get(URL+unkownId))
			.andExpect(responseBody().notFound(PolicyPayment.class, unkownId));

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
			.andExpect(responseBody().pagedModel("policyPayments").contains(viewModel))
			.andReturn();
	    
		verify(service).all(pageRequest);
		verify(repo).findAll(pageRequest);
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
		Policy policy = entity.getPolicy();
		when(policyRepo.findById(policy.getId())).thenReturn(Optional.of(policy));
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);

		var dto = modelMapper.toDTO(entity);
		var viewModel = modelMapper.map(entity);
		
		post(mockMvc, URL, dto)
			.andExpect(status().isCreated())
			.andExpect(responseBody().isEqualTo(viewModel));

//		verify(repo).save(entity); TODO
	}


	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		entity.setAmountTendered(new BigDecimal("-2"));
		String policyId = entity.getPolicy().getId();

		post(mockMvc, URL, entity)
			.andExpect(responseBody().containsErrorFor("amountTendered"));

		verify(service, times(0)).save(any(PolicyPayment.class), policyId);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update() throws Exception {
		var dto = modelMapper.toDTO(entity);
		var viewModel = modelMapper.map(entity);
		
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		when(repo.save(any(PolicyPayment.class))).thenReturn(entity);

		put(mockMvc, URL+ID, dto)
			.andExpect(status().isOk())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).update(eq(ID), any(PolicyPayment.class));
		verify(repo).save(entity);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		entity.setAmountTendered(new BigDecimal("-2"));
		String policyId = entity.getPolicy().getId();
		
		put(mockMvc, URL+ID, entity)
			.andExpect(responseBody().containsErrorFor("amountTendered"));
		
		verify(service, times(0)).save(any(PolicyPayment.class), eq(policyId));
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_policyPaymentInfo() throws Exception {
		BigDecimal premium = entity.getPolicy().getPremiumAmount();
		List<PolicyPaymentDetails> value = List.of(
			new PolicyPaymentDetails(1L, PolicyPaymentDetails.Type.PREMIUM, 
					Period.of(LocalDate.now()), premium, entity, false, null, null),
			new PolicyPaymentDetails(1L, PolicyPaymentDetails.Type.PREMIUM, 
					Period.of(LocalDate.now().minusMonths(1)), premium, entity, false, null, null)
		);
		when(repo.getPaymentDetails(anyLong())).thenReturn(value);
		
		mockMvc.perform(get(URL+"/"+ID+"/payment-details"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("_embedded.policyPaymentDetails[0].amount").value(value.get(0).getAmount()))
			.andExpect(jsonPath("_embedded.policyPaymentDetails[1].period.month").value(value.get(1).getPeriod().getMonth().name()))
			.andExpect(jsonPath("_embedded.policyPaymentDetails[1].period.year").value(value.get(1).getPeriod().getYear()))
			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/payment-details")))
			.andExpect(jsonPath("_embedded.policyPaymentDetails[0].policyPayment").doesNotHaveJsonPath());

		verify(service).getPaymentDetails(ID); 
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void test_getPolicyPaymentInquiry() throws Exception {
		Policy policy = entity.getPolicy();
		String policyNumber = policy.getPolicyNumber();
		Period lastPaidPeriod = Period.of(2020, Month.JANUARY);
		Period currentPeriod = Period.of(2020, Month.APRIL);
		FuneralScheme funeralScheme = policy.getFuneralScheme();
		
		when(paymentDetailsRepo.getLastPayedPeriod(policy.getId())).thenReturn(Optional.of(lastPaidPeriod));
		when(policyRepo.findById(anyString())).thenReturn(Optional.of(policy));
		when(unpaidRepo.findByPolicy(policy)).thenReturn(List.of());
		
		PolicyPaymentDetails penalty = PolicyPaymentDetails.penaltyOf(funeralScheme.getPenaltyFee());
		
		mockMvc.perform(get(URL+"/inquire?period="+currentPeriod.ordinal()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("policyNumber").value(policyNumber))
			.andExpect(jsonPath("policyHolder").value(policy.getFullName()))
			.andExpect(jsonPath("premium").value(policy.getPremiumAmount()))
			.andExpect(jsonPath("lastPayedPeriod").value(lastPaidPeriod))
			.andExpect(jsonPath("penaltyDue").value(penalty.getAmount()))
			.andExpect(jsonPath("premiumDue").value(policy.getPremiumAmount()
					.multiply(new BigDecimal("3"))));
	}
	
	
	private PolicyPayment persistedEntity() {
		return policyPaymentMother()
				.build();
	}
	
	private PolicyPayment newEntity() {
		return policyPaymentMother()
				.noBranchNoID()
				.build();
	}
	
	private PolicyPaymentMother policyPaymentMother() {
		return new PolicyPaymentMother(PolicyMother.of(PlanType.Plan_C, 43))
				.id(ID)
				.withPremiumForCurrentMonth();
	}
}
