package com.breakoutms.lfs.server.mortuary.unit;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.Expectations;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.Corpse;
import com.breakoutms.lfs.server.mortuary.CorpseModelAssembler;
import com.breakoutms.lfs.server.mortuary.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.CorpseService;
import com.breakoutms.lfs.server.mortuary.NextOfKin;
import com.breakoutms.lfs.server.mortuary.OtherMortuary;
import com.breakoutms.lfs.server.mortuary.OtherMortuaryRepository;

@WebMvcTest
class CorpseControllerUnitTest implements ControllerUnitTest {


	@Autowired
	private MockMvc mockMvc;
	@MockBean private CorpseRepository repo;
	@MockBean private BranchRepository branchRepo;
	@MockBean private OtherMortuaryRepository otherMortuaryRepo;
	@SpyBean private CorpseService service;
	@SpyBean private CorpseModelAssembler assembler;
	
	private Corpse corpse;
	private List<Corpse> corpseList;
	private final String TAG_NO = "256000001";
	private final String URL = "/corpses/";

	private Expectations expect;
	
	@BeforeEach
	public void setup() {
		createCorpse();
		expect = new Expectations(URL, corpse.getBranch());
	}

	@Test
	void get_corpse() throws Exception {
		when(repo.findById(TAG_NO)).thenReturn(Optional.of(corpse));
	    ResultActions result = mockMvc.perform(get(URL+corpse.getTagNo()));
	    result.andExpect(status().isOk());
	    result.andExpect(jsonPath("_links.nextOfKins.href", endsWith("/next-of-kins/"+corpse.getTagNo())));
	    result.andExpect(jsonPath("_links.transferredFrom.href", endsWith("/other-mortuaries/"
	    		+corpse.getTransferredFrom().getId())));
	    result.andDo(print());
	    expect.forEntity(result, corpse);
	    verify(service).get(TAG_NO);
	}
	
	@Test
	void get_for_non_existing_corpse() throws Exception {
		String exMsg = ExceptionSupplier.notFound("Corpse", TAG_NO).get().getMessage();
		
		when(repo.findById(anyString())).thenReturn(Optional.ofNullable(null));
		
	    var result = mockMvc.perform(get(URL+TAG_NO))
	    		.andExpect(status().isNotFound());
	    Expectations.forObjectNotFound(result, exMsg);
	    verify(service).get(TAG_NO);
	}
	
	@Test
	void get_all_corpse() throws Exception {

		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		
		var pageRequest = PageRequestHelper.from(url);
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(corpseList));
		
	    ResultActions result = mockMvc.perform(get(url))
	    		.andExpect(status().isOk());
	    expect.forPage(result, corpseList, "corpseList", url);
	    verify(service).all(pageRequest);
	}
	
	@Test
	void get_all_corpse_with_no_content() throws Exception {

		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		
		var pageRequest = PageRequestHelper.from(url);
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(new ArrayList<>()));
		
		mockMvc.perform(get(url))
	    		.andExpect(status().isNoContent());
		verify(service).all(pageRequest);
	}
	
	@Test
	void succesfull_corpse_save() throws Exception {
		when(repo.save(any(Corpse.class))).thenReturn(corpse);
		
		var result = post(mockMvc, URL, corpse);
		result.andExpect(status().isCreated());
		result.andDo(print());
		expect.forEntity(result, corpse);
		
		verify(service).save(any(Corpse.class));
	}


	@Test
	void save_fails_because_of_invalid_field() throws Exception {
		String exMsg = "Invalid input for Names";
		
		Corpse corpse = new Corpse();
		corpse.setNames("N");
		
		var result = post(mockMvc, URL, corpse);
		
	   Expectations.forInvalidFields(result, exMsg);
	   
	   verify(service, times(0)).save(any(Corpse.class));
	}
	
	@Test
	void update_corpse() throws Exception {
		when(repo.existsById(TAG_NO)).thenReturn(true);
		when(repo.save(any(Corpse.class))).thenReturn(corpse);
		when(otherMortuaryRepo.existsById(anyInt())).thenReturn(true);

		var result = put(mockMvc, URL+TAG_NO, corpse);
		System.out.println();
		
		result.andExpect(status().isOk());
		expect.forEntity(result, corpse);
		
		verify(service).update(anyString(), any(Corpse.class));
	}
	
	@Test
	void getOtherMortuaries_returns_the_correct_list_of_OtherMortuaries() throws Exception {
		List<OtherMortuary> list = List.of(new OtherMortuary("MKM"), 
				new OtherMortuary("Maputsoe"), 
				new OtherMortuary("Sentebale"));
		when(otherMortuaryRepo.findAll()).thenReturn(list);

		mockMvc.perform(get(URL+"other-mortuaries"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].name").value("MKM"))
			.andExpect(jsonPath("[2].name").value("Sentebale"));

		verify(service).getOtherMortuaries(); 
	}
	
	@Test
	void test_getOtherMortuaries_returns_no_content_when_list_is_empty() throws Exception {
		List<OtherMortuary> list = new ArrayList<>();
		when(otherMortuaryRepo.findAll()).thenReturn(list);

		mockMvc.perform(get(URL+"other-mortuaries"))
			.andExpect(status().isNoContent());

		verify(service).getOtherMortuaries(); 
	}
	
	@Test
	void getTransferedFrom_returns_OtherMorthurys_name_where_corpse_is_transferedFrom() throws Exception {
		OtherMortuary mkm = corpse.getTransferredFrom();
		when(otherMortuaryRepo.findById(anyInt())).thenReturn(Optional.of(mkm));

		mockMvc.perform(get(URL+"other-mortuaries/"+mkm.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(mkm.getName()));

		verify(service).getTransforedFrom(mkm.getId());
	}
	
	@Test
	void getNextOfKins_returns_the_correct_list() throws Exception {
		List<NextOfKin> list = List.of(new NextOfKin("David", "Moleko"), 
				new NextOfKin("Molise", "Molemo"), 
				new NextOfKin("Rorisang", "Motlomelo"));
		when(repo.findNextOfKins(TAG_NO)).thenReturn(list);

		mockMvc.perform(get(URL+"next-of-kins/"+TAG_NO))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].names").value("David"))
			.andExpect(jsonPath("[2].surname").value("Motlomelo"));

		verify(service).getNextOfKins(TAG_NO); 
	}
	
	@Test
	void test_getNextOfKins_returns_no_content_when_list_is_empty() throws Exception {
		when(repo.findNextOfKins(anyString())).thenReturn(new ArrayList<>());

		mockMvc.perform(get(URL+"next-of-kins/"+TAG_NO))
			.andExpect(status().isNoContent());

		verify(service).getNextOfKins(TAG_NO); 
	}
	
	private void createCorpse() {
		corpse = new Corpse();
		corpse.setNames("Thabo");
		corpse.setSurname("Lebese");
		OtherMortuary om = new OtherMortuary("MKM");
		corpse.setTransferredFrom(om);
		
		Corpse corpse2 = new Corpse();
		corpse2.setNames("Nthabiseng");
		corpse2.setSurname("Lebese");
		
		//set IDs
		corpse.setTagNo(TAG_NO);
		corpse2.setTagNo("102");
		om.setId(101);
		
		corpseList = Arrays.asList(corpse, corpse2);
	}
}
