package com.breakoutms.lfs.server.preneed.inte;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.Expectations;
import com.breakoutms.lfs.server.common.copy.DeepCopy;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.PolicyService;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PolicyControllerIntegrationTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_PRENEED";
	
	@Autowired private MockMvc mockMvc;
	@Autowired private EntityManager entityManager;
	@Autowired private UserDetailsServiceImpl requiredBean;
	@Autowired private PolicyRepository repo;
	@Autowired private PolicyService service;
	
	private Policy entity = createEntity();
	private String entityId;
	private static final String URL = "/preneed/funeral-schemes/";

	private Expectations expect;
	
	@BeforeEach
	public void setup() {
		entityId = persistAndGetCopy(entity).getId();
		expect = new Expectations(URL, getBranch());
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_by_id() throws Exception {
	    ResultActions result = mockMvc.perform(get(URL+entityId));
	    result.andExpect(status().isOk());
	    result.andExpect(jsonPath("_links.premiums.href", endsWith(entity.getId()+"/premiums")));
	    result.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(entity.getId()+"/dependent-benefits")));
	    result.andExpect(jsonPath("_links.funeralSchemeBenefits.href", endsWith(entity.getId()+"/funeral-scheme-benefits")));
	    result.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(entity.getId()+"/penalty-deductibles")));
	    result.andDo(print());
	    expect.forEntity(result, entity);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = 122423;
		String exMsg = ExceptionSupplier.notFound(Policy.class, unkownId).get().getMessage();
		
	    var result = mockMvc.perform(get(URL+unkownId))
	    		.andExpect(status().isNotFound());
	    Expectations.forObjectNotFound(result, exMsg);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_all() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";

		var list = Arrays.asList(entity);
	    ResultActions result = mockMvc.perform(get(url))
	    		.andExpect(status().isOk());
	    expect.forPage(result, list, "funeralSchemes", url);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_all_with_no_content_returns_NO_CONTENT_status() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		repo.deleteAll();
		mockMvc.perform(get(url))
	    		.andExpect(status().isNoContent());
	}
	
	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void succesfull_save() throws Exception {
		var entity = new Policy();
		entity.setNames("abc");
		var result = post(mockMvc, URL, entity);
		var id = lastId()+1;
		result.andExpect(status().isCreated())
			.andExpect(jsonPath("name").value("abc"));
		expect.forCommonLinks(result, String.valueOf(id))
			.andExpect(jsonPath("_links.premiums.href", endsWith(URL+id+"/premiums")))
			.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(URL+id+"/dependent-benefits")))
			.andExpect(jsonPath("_links.funeralSchemeBenefits.href", endsWith(URL+id+"/funeral-scheme-benefits")))
			.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(URL+id+"/penalty-deductibles")));
	}


	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		String exMsg = "Invalid input for 'Name'";
		var entity = new Policy();
		entity.setNames(" ");
		var result = post(mockMvc, URL, entity);
		Expectations.forInvalidFields(result, exMsg);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void succesfull_update() throws Exception {
		var entity = new Policy();
		var before = "A1";
		var after = "A2";
		entity.setNames(before);
		assertThat(entity.getNames()).isEqualTo(before);
		
		var copy = persistAndGetCopy(entity);
		var id = copy.getId();
		copy.setNames(after);
		
		var result = put(mockMvc, URL+id, copy);
		result.andDo(print());
		
		result.andExpect(status().isOk())
			.andExpect(jsonPath("id", is(entity.getId())))
			.andExpect(jsonPath("name", is("A2")))
			.andExpect(jsonPath("_links.premiums.href", endsWith(URL+id+"/premiums")))
			.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(URL+id+"/dependent-benefits")))
			.andExpect(jsonPath("_links.funeralSchemeBenefits.href", endsWith(URL+id+"/funeral-scheme-benefits")))
			.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(URL+id+"/penalty-deductibles")));
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		String exMsg = "Invalid input for 'Name'";
		
		var entity = new Policy();
		entity.setNames(" ");

		var result = put(mockMvc, URL+entityId, entity)
				.andExpect(status().isBadRequest());
		Expectations.forInvalidFields(result, exMsg);
	}
	
//	@Test
//	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
//	void get_premiums() throws Exception {
//		List<Premium> value = PolicysJSON
//				.getPemiums()
//				.stream()
//				.limit(3)
//				.map(p -> {
//					p.setId(null);
//					return p;
//				})
//				.collect(Collectors.toList());
//		
//		var entity = new Policy("ABC");
//		entity.setPremiums(value);
//		service.save(entity);
//		
//		mockMvc.perform(get(URL+"/"+entity.getId()+"/premiums"))
//			.andDo(print())
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("_embedded.premiums[0].premiumAmount").value(value.get(0).getPremiumAmount()))
//			.andExpect(jsonPath("_embedded.premiums[2].premiumAmount").value(value.get(2).getPremiumAmount()))
//			.andExpect(jsonPath("_embedded.premiums[0].funeralScheme").doesNotExist())
//			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/premiums")));
//	}
	
	private String lastId() {
		return entity.getId();
	}
	
	private Policy createEntity() {
		Policy entity = new Policy();
		entity.setNames("Thabo");
		entity.setSurname("Lebese");
		return entity;
	}
	
	protected Policy persistAndGetCopy(Policy entity) {
		entity.setPolicyNumber(null);
		
		service.save(entity, entity.getFuneralScheme().getName());
		entityManager.flush();
		entityManager.clear();
		
		Policy deepCopy = DeepCopy.copy(entity, Policy.class);
		return deepCopy;
	}
}
