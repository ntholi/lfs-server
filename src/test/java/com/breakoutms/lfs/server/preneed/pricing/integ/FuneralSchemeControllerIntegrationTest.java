package com.breakoutms.lfs.server.preneed.pricing.integ;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeService;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class FuneralSchemeControllerIntegrationTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_PRENEED";
	
	@Autowired private MockMvc mockMvc;
	@Autowired private EntityManager entityManager;
	@Autowired private UserDetailsServiceImpl requiredBean;
	@Autowired private FuneralSchemeRepository repo;
	@Autowired private FuneralSchemeService service;
	
	private static final List<FuneralScheme> list = FuneralSchemesJSON.all();
	private FuneralScheme entity = list.get(0);
	private static Integer entityId;
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
		String exMsg = ExceptionSupplier.notFound(FuneralScheme.class, unkownId).get().getMessage();
		
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
		var entity = new FuneralScheme();
		entity.setName("abc");
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
		var entity = new FuneralScheme();
		entity.setName(" ");
		var result = post(mockMvc, URL, entity);
		Expectations.forInvalidFields(result, exMsg);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void succesfull_update() throws Exception {
		var entity = new FuneralScheme();
		var before = "A1";
		var after = "A2";
		entity.setName(before);
		assertThat(entity.getName()).isEqualTo(before);
		
		var copy = persistAndGetCopy(entity);
		var id = copy.getId();
		copy.setName(after);
		
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
		
		var entity = new FuneralScheme();
		entity.setName(" ");

		var result = put(mockMvc, URL+entityId, entity)
				.andExpect(status().isBadRequest());
		Expectations.forInvalidFields(result, exMsg);
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_premiums() throws Exception {
		List<Premium> value = FuneralSchemesJSON
				.getPemiums()
				.stream()
				.limit(3)
				.map(p -> {
					p.setId(null);
					return p;
				})
				.collect(Collectors.toList());
		
		var entity = new FuneralScheme("ABC");
		entity.setPremiums(value);
		service.save(entity);
		
		mockMvc.perform(get(URL+"/"+entity.getId()+"/premiums"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("_embedded.premiums[0].premiumAmount").value(value.get(0).getPremiumAmount()))
			.andExpect(jsonPath("_embedded.premiums[2].premiumAmount").value(value.get(2).getPremiumAmount()))
			.andExpect(jsonPath("_embedded.premiums[0].funeralScheme").doesNotExist())
			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/premiums")));
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_dependentBenefit() throws Exception {
		List<DependentBenefit> value = FuneralSchemesJSON
				.getDependentBenefit()
				.stream()
				.limit(3)
				.map(p -> {
					p.setId(null);
					return p;
				})
				.collect(Collectors.toList());
		
		var entity = new FuneralScheme("ABC");
		entity.setDependentBenefits(value);
		service.save(entity);
		
		mockMvc.perform(get(URL+"/"+entity.getId()+"/dependent-benefits"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("_embedded.dependentBenefits[0].minmumAge").value(value.get(0).getMinmumAge()))
			.andExpect(jsonPath("_embedded.dependentBenefits[2].maximumAge").value(value.get(2).getMaximumAge()))
			.andExpect(jsonPath("_embedded.dependentBenefits[0].funeralScheme").doesNotExist())
			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/dependent-benefits")));
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_funeralSchemeBenefits() throws Exception {
		List<FuneralSchemeBenefit> value = FuneralSchemesJSON
				.getFuneralSchemeBenefit()
				.stream()
				.limit(3)
				.map(p -> {
					p.setId(null);
					return p;
				})
				.collect(Collectors.toList());
		
		var entity = new FuneralScheme("ABC");
		entity.setBenefits(value);
		service.save(entity);

		mockMvc.perform(get(URL+"/"+entity.getId()+"/funeral-scheme-benefits"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("_embedded.funeralSchemeBenefits[0].deductable").value(value.get(0).getDeductable().name()))
			.andExpect(jsonPath("_embedded.funeralSchemeBenefits[2].discount").value(value.get(2).getDiscount()))
			.andExpect(jsonPath("_embedded.funeralSchemeBenefits[0].funeralScheme").doesNotExist())
			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/funeral-scheme-benefits"))); 
	}
	
	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_penaltyDeductibles() throws Exception {
		List<PenaltyDeductible> value = FuneralSchemesJSON
				.getPenaltyDeductable()
				.stream()
				.limit(3)
				.map(p -> {
					p.setId(null);
					return p;
				})
				.collect(Collectors.toList());
		
		var entity = new FuneralScheme("ABC");
		entity.setPenaltyDeductibles(value);
		service.save(entity);
		
		mockMvc.perform(get(URL+"/"+entity.getId()+"/penalty-deductibles"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("_embedded.penaltyDeductibles[0].months").value(value.get(0).getMonths()))
			.andExpect(jsonPath("_embedded.penaltyDeductibles[2].amount").value(value.get(2).getAmount()))
			.andExpect(jsonPath("_embedded.penaltyDeductibles[0].funeralScheme").doesNotExist())
			.andExpect(jsonPath("_links.self.href", endsWith(entity.getId()+"/penalty-deductibles")));
	}
	
	private Integer lastId() {
		return entity.getId();
	}
	
	protected FuneralScheme persistAndGetCopy(FuneralScheme entity) {
		entity.setId(null);
		if(entity.getBenefits() != null) {
			entity.getBenefits().forEach(item -> item.setId(null));
		}
		if(entity.getDependentBenefits() != null) {
			entity.getDependentBenefits().forEach(item -> item.setId(null));
		}
		if(entity.getPenaltyDeductibles() != null) {
			entity.getPenaltyDeductibles().forEach(item -> item.setId(null));
		}
		if(entity.getPremiums() != null) {
			entity.getPremiums().forEach(item -> item.setId(null));
		}
		
		service.save(entity);
		entityManager.flush();
		entityManager.clear();
		
		FuneralScheme deepCopy = DeepCopy.copy(entity);
		return deepCopy;
	}
}
