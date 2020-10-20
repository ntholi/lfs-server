package com.breakoutms.lfs.server.preneed.policy;

import static com.breakoutms.lfs.server.common.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.breakoutms.lfs.common.enums.PolicyStatus;
import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.FuneralSchemeMother;
import com.breakoutms.lfs.server.common.motherbeans.preeneed.PolicyMother;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PolicyController.class)
public class PolicyControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_PRENEED";
	@Autowired private MockMvc mockMvc;
	@MockBean private UserDetailsServiceImpl requiredBean;
	@MockBean private PolicyRepository repo;
	@MockBean private FuneralSchemeRepository schemeRepo;
	@MockBean private BranchRepository branchRepo;
	@SpyBean private PolicyService service;
	
	private final Policy entity = persistedEntity();
	private static final String ID = "5";
	private static final int AGE = 20;
	private static final String URL = "/preneed/policies/";

	private PreneedMapper modelMapper = PreneedMapper.INSTANCE;

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
		FuneralScheme fs = entity.getFuneralScheme();

		mockMvc.perform(get(URL+ID))
				.andExpect(responseBody().hasLink("all", "/preneed/policies"))
				.andExpect(responseBody().hasLink("self", "/preneed/policies/"+entity.getId()))
				.andExpect(responseBody().hasLink("funeralScheme", "/preneed/funeral-schemes/"+fs.getId()))
				.andExpect(responseBody().hasLink("branch", "/branches/1"));
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = "122423";
		mockMvc.perform(get(URL+unkownId))
			.andExpect(responseBody().notFound(Policy.class, unkownId));

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
			.andExpect(responseBody().pagedModel("policies").contains(viewModel))
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
		var entity = newEntity();
		var dto = modelMapper.map(entity);
		var viewModel = modelMapper.map(entity);
		FuneralScheme fs = new FuneralSchemeMother().planC().build();
		Premium premium = fs.getPremiums().iterator().next();
		
		when(repo.save(any(Policy.class))).thenReturn(entity);
		when(schemeRepo.findByName(anyString())).thenReturn(Optional.of(entity.getFuneralScheme()));
		when(schemeRepo.findPremium(any(FuneralScheme.class), anyInt())).thenReturn(Optional.of(premium));
		
		post(mockMvc, URL, dto)
			.andExpect(status().isCreated())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).save(any(Policy.class), anyString());
		verify(repo).save(entity);
	}


	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		entity.setNames(null);
		var dto = modelMapper.map(entity);

		post(mockMvc, URL, dto)
			.andExpect(responseBody().containsErrorFor("names"));

		verify(service, times(0)).save(any(Policy.class), anyString());
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update() throws Exception {
		var dto = modelMapper.map(entity);
		var viewModel = modelMapper.map(entity);
		
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		when(repo.save(any(Policy.class))).thenReturn(entity);
		when(schemeRepo.findByName(dto.getFuneralScheme()))
			.thenReturn(Optional.of(entity.getFuneralScheme()));

		put(mockMvc, URL+ID, dto)
			.andExpect(status().isOk())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).update(eq(ID), any(Policy.class), anyString());
		verify(repo).save(entity);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		entity.setNames(null);
		var dto = modelMapper.map(entity);
		
		put(mockMvc, URL+ID, dto)
			.andExpect(responseBody().containsErrorFor("names"));
	}
	
	private Policy persistedEntity() {
		return policyMother()
				.build();
	}
	
	private Policy newEntity() {
		Policy policy = policyMother()
				.noBranchNoID()
				.build();
		policy.setStatus(PolicyStatus.WAITING_PERIOD);
		return policy;
	}

	private PolicyMother policyMother() {
		int age = 20;
		Premium premium = new FuneralSchemeMother().planC().getPremium(age).get();
		return new PolicyMother()
				.id(ID)
				.funeralScheme(new FuneralSchemeMother()
						.nameOnly("Some Name")
						.id(20).build())
				.age(AGE)
				.premiumAmount(premium.getPremiumAmount())
				.coverAmount(premium.getCoverAmount());
	}
}
