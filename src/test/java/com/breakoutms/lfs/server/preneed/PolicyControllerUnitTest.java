package com.breakoutms.lfs.server.preneed;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.ResultActions;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.Expectations;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PolicyController;
import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.PolicyService;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

import lombok.val;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PolicyController.class)
public class PolicyControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_PRENEED";
	@Autowired private MockMvc mockMvc;
	@MockBean private UserDetailsServiceImpl requiredBean;
	@MockBean private PolicyRepository repo;
	@MockBean private FuneralSchemeRepository funeralSchemeRepo;
	@MockBean private BranchRepository branchRepo;
	@SpyBean private PolicyService service;
	
	private final Policy entity = createEntity();
	private static final String ID = "5";
	private static final String URL = "/preneed/policies/";

	private Expectations expect;
	
	@BeforeEach
	public void setup() {
		expect = new Expectations(URL, getBranch());
	}
	

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_by_id() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
	    ResultActions result = mockMvc.perform(get(URL+ID))
	    		.andExpect(status().isOk())
	    		.andDo(print())
	    		.andExpect(jsonPath("_links.funeralScheme.href", 
	    				endsWith("/funeral-schemes/"+entity.getFuneralScheme().getId())));
	    
	    expect.forEntity(result, entity);
	    verify(service).get(ID);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = "122423";
		String exMsg = ExceptionSupplier.notFound(Policy.class, unkownId).get().getMessage();
		
		when(repo.findById(anyString())).thenReturn(Optional.ofNullable(null));
		
	    var result = mockMvc.perform(get(URL+unkownId))
	    		.andExpect(status().isNotFound());
	    Expectations.forObjectNotFound(result, exMsg);
	    verify(service).get(unkownId);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_all() throws Exception {

		List<Policy> list = Arrays.asList(entity);
		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		
		var pageRequest = PageRequestHelper.from(url);
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
		
	    ResultActions result = mockMvc.perform(get(url))
	    		.andExpect(status().isOk());
	    expect.forPage(result, list, "funeralSchemes", url);
	    verify(service).all(pageRequest);
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
	void succesfull_save() throws Exception {
		val funeralSchem = entity.getFuneralScheme();
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.of(entity.getFuneralScheme()));
		when(funeralSchemeRepo.findPremium(any(FuneralScheme.class), anyInt()))
			.thenReturn(Optional.of(new Premium()));
		when(repo.save(any(Policy.class))).thenReturn(entity);

		PolicyDTO policyDTO = PolicyDTO.builder()
				.names(entity.getNames())
				.surname(entity.getSurname())
				.funeralScheme(funeralSchem.getName())
				.registrationDate(entity.getRegistrationDate())
				.dateOfBirth(entity.getDateOfBirth())
				.build();
				
		var result = post(mockMvc, URL, policyDTO);
		result.andDo(print());
		result.andExpect(status().isCreated());
		expect.forEntity(result, entity)
			.andExpect(jsonPath("_links.funeralScheme.href", 
				endsWith("/funeral-schemes/"+entity.getFuneralScheme().getId())));
		
		verify(service).save(any(Policy.class), anyString());
	}


	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		var entity = new Policy();
		entity.setDateOfBirth(LocalDate.now());
		entity.setNames(" ");
		
		post(mockMvc, URL, entity)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath(Expectations.STATUS).value(400))
			.andExpect(jsonPath(Expectations.MESSAGE, containsString("Registration Date")))
			.andExpect(jsonPath(Expectations.MESSAGE, containsString("Funeral Scheme")))
			.andExpect(jsonPath(Expectations.MESSAGE, containsString("Surname")))
			.andExpect(jsonPath(Expectations.MESSAGE, containsString("Names")));
	   
	   verify(service, times(0)).save(any(Policy.class), anyString());
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void succesfull_update() throws Exception {
		val funeralSchem = entity.getFuneralScheme();
		
		when(repo.existsById(ID)).thenReturn(true);
		when(repo.save(any(Policy.class))).thenReturn(entity);
		when(funeralSchemeRepo.findByName(anyString())).thenReturn(Optional.of(entity.getFuneralScheme()));

		PolicyDTO policyDTO = PolicyDTO.builder()
				.policyNumber(entity.getPolicyNumber())
				.names(entity.getNames())
				.surname(entity.getSurname())
				.funeralScheme(funeralSchem.getName())
				.registrationDate(entity.getRegistrationDate())
				.dateOfBirth(entity.getDateOfBirth())
				.build();
		
		var result = put(mockMvc, URL+ID, policyDTO);
		
		result.andExpect(status().isOk());
		expect.forEntity(result, entity);
		
		verify(service).update(ID, entity, funeralSchem.getName());
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		when(repo.existsById(ID)).thenReturn(true);
		when(repo.save(any(Policy.class))).thenReturn(entity);
		
		var entity = new Policy();
		entity.setDateOfBirth(LocalDate.now());
		entity.setNames(" ");

		put(mockMvc, URL+ID, entity)
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath(Expectations.STATUS).value(400))
		.andExpect(jsonPath(Expectations.MESSAGE, containsString("Registration Date")))
		.andExpect(jsonPath(Expectations.MESSAGE, containsString("Funeral Scheme")))
		.andExpect(jsonPath(Expectations.MESSAGE, containsString("Surname")))
		.andExpect(jsonPath(Expectations.MESSAGE, containsString("Names")));

		verify(service, times(0)).update(anyString(), any(Policy.class), anyString());
	}
	
	private Policy createEntity() {
		Policy entity = new Policy();
		entity.setPolicyNumber(ID);
		entity.setNames("Thabo");
		entity.setSurname("Lebese");
		entity.setFuneralScheme(createFuneralScheme());
		entity.setRegistrationDate(LocalDate.now());
		entity.setDateOfBirth(LocalDate.now().minusYears(20));
		return entity;
	}
	
	private FuneralScheme createFuneralScheme() {
		FuneralScheme funeralScheme = new FuneralScheme("Plan A");
		funeralScheme.setId(3);
		return funeralScheme;
	}
}
