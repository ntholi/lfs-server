package com.breakoutms.lfs.server.preneed.pricing.unit;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.Expectations;
import com.breakoutms.lfs.server.preneed.pricing.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeController;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeService;
import com.breakoutms.lfs.server.preneed.pricing.json.FuneralSchemesJSON;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FuneralSchemeController.class)
public class FuneralSchemeControllerUnitTest implements ControllerUnitTest {


	private MockMvc mockMvc;
	@MockBean private UserDetailsServiceImpl requiredBean;
	@MockBean private FuneralSchemeRepository repo;
	@MockBean private BranchRepository branchRepo;
	@SpyBean private FuneralSchemeService service;
	
	private static final List<FuneralScheme> list = FuneralSchemesJSON.all();
	private FuneralScheme entity = list.get(0);
	private static final Integer ID = 5;
	private static final String URL = "/preneed/funeral-schemes/";

	private Expectations expect;
	
    @Autowired
    private WebApplicationContext context;
	
	@BeforeEach
	public void setup() {
		expect = new Expectations(URL, getBranch());
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) 
                .build();
	}
	
	@Test
	@WithMockUser(authorities = {"READ", "ROLE_PRENEED"})
	void get_funeralScheme() throws Exception {
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
	    ResultActions result = mockMvc.perform(get(URL+ID));
	    result.andExpect(status().isOk());
	    result.andExpect(jsonPath("_links.premiums.href", endsWith(entity.getId()+"/premiums")));
	    result.andExpect(jsonPath("_links.dependentBenefits.href", endsWith(entity.getId()+"/dependent-benefits")));
	    result.andExpect(jsonPath("_links.funeralSchemeBenefit.href", endsWith(entity.getId()+"/funeral-scheme-benefit")));
	    result.andExpect(jsonPath("_links.penaltyDeductibles.href", endsWith(entity.getId()+"/penalty-deductibles")));
	    result.andDo(print());
	    expect.forEntity(result, entity);
	    verify(service).get(ID);
	}
	
//	@Test
//	void get_for_non_existing_corpse() throws Exception {
//		String exMsg = ExceptionSupplier.corpseNotFound(ID).get().getMessage();
//		
//		when(repo.findById(anyString())).thenReturn(Optional.ofNullable(null));
//		
//	    var result = mockMvc.perform(get(URL+ID))
//	    		.andExpect(status().isNotFound());
//	    Expectations.forObjectNotFound(result, exMsg);
//	    verify(service).get(ID);
//	}
//	
//	@Test
//	void get_all_corpse() throws Exception {
//
//		var url = URL+"?page=0&size=20&sort=createdAt,desc";
//		
//		var pageRequest = PageRequestHelper.from(url);
//		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(list));
//		
//	    ResultActions result = mockMvc.perform(get(url))
//	    		.andExpect(status().isOk());
//	    expect.forPage(result, list, "corpseList", url);
//	    verify(service).all(pageRequest);
//	}
//	
//	@Test
//	void get_all_corpse_with_no_content() throws Exception {
//
//		var url = URL+"?page=0&size=20&sort=createdAt,desc";
//		
//		var pageRequest = PageRequestHelper.from(url);
//		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));
//		
//		mockMvc.perform(get(url))
//	    		.andExpect(status().isNoContent());
//		verify(service).all(pageRequest);
//	}
//	
//	@Test
//	void succesfull_corpse_save() throws Exception {
//		when(repo.save(any(Corpse.class))).thenReturn(entity);
//		
//		var result = post(mockMvc, URL, entity);
//		result.andExpect(status().isCreated());
//		result.andDo(print());
//		expect.forEntity(result, entity);
//		
//		verify(service).save(any(Corpse.class));
//	}
//
//
//	@Test
//	void save_fails_because_of_invalid_field() throws Exception {
//		String exMsg = "Invalid input for Names";
//		
//		Corpse corpse = new Corpse();
//		corpse.setNames("N");
//		
//		var result = post(mockMvc, URL, corpse);
//		
//	   Expectations.forInvalidFields(result, exMsg);
//	   
//	   verify(service, times(0)).save(any(Corpse.class));
//	}
//	
//	@Test
//	void update_corpse() throws Exception {
//		when(repo.existsById(ID)).thenReturn(true);
//		when(repo.save(any(Corpse.class))).thenReturn(entity);
//		when(otherMortuaryRepo.existsById(anyInt())).thenReturn(true);
//
//		var result = put(mockMvc, URL+ID, entity);
//		System.out.println();
//		
//		result.andExpect(status().isOk());
//		expect.forEntity(result, entity);
//		
//		verify(service).update(anyString(), any(Corpse.class));
//	}
//	
//	@Test
//	void getOtherMortuaries_returns_the_correct_list_of_OtherMortuaries() throws Exception {
//		List<OtherMortuary> list = List.of(new OtherMortuary("MKM"), 
//				new OtherMortuary("Maputsoe"), 
//				new OtherMortuary("Sentebale"));
//		when(otherMortuaryRepo.findAll()).thenReturn(list);
//
//		mockMvc.perform(get(URL+"other-mortuaries"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("[0].name").value("MKM"))
//			.andExpect(jsonPath("[2].name").value("Sentebale"));
//
//		verify(service).getOtherMortuaries(); 
//	}
//	
//	@Test
//	void test_getOtherMortuaries_returns_no_content_when_list_is_empty() throws Exception {
//		List<OtherMortuary> list = new ArrayList<>();
//		when(otherMortuaryRepo.findAll()).thenReturn(list);
//
//		mockMvc.perform(get(URL+"other-mortuaries"))
//			.andExpect(status().isNoContent());
//
//		verify(service).getOtherMortuaries(); 
//	}
//	
//	@Test
//	void getTransferedFrom_returns_OtherMorthurys_name_where_corpse_is_transferedFrom() throws Exception {
//		OtherMortuary mkm = entity.getTransferredFrom();
//		when(otherMortuaryRepo.findById(anyInt())).thenReturn(Optional.of(mkm));
//
//		mockMvc.perform(get(URL+"other-mortuaries/"+mkm.getId()))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("name").value(mkm.getName()));
//
//		verify(service).getTransforedFrom(mkm.getId());
//	}
//	
//	@Test
//	void getNextOfKins_returns_the_correct_list() throws Exception {
//		List<NextOfKin> list = List.of(new NextOfKin("David", "Moleko"), 
//				new NextOfKin("Molise", "Molemo"), 
//				new NextOfKin("Rorisang", "Motlomelo"));
//		when(repo.findNextOfKins(ID)).thenReturn(list);
//
//		mockMvc.perform(get(URL+"next-of-kins/"+ID))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("[0].names").value("David"))
//			.andExpect(jsonPath("[2].surname").value("Motlomelo"));
//
//		verify(service).getNextOfKins(ID); 
//	}
//	
//	@Test
//	void test_getNextOfKins_returns_no_content_when_list_is_empty() throws Exception {
//		when(repo.findNextOfKins(anyString())).thenReturn(new ArrayList<>());
//
//		mockMvc.perform(get(URL+"next-of-kins/"+ID))
//			.andExpect(status().isNoContent());
//
//		verify(service).getNextOfKins(ID); 
//	}
}
