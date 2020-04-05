package lfs.server.mortuary;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import lfs.server.branch.BranchRepository;
import lfs.server.common.ControllerUnitTest;
import lfs.server.common.Expectations;
import lfs.server.common.PageRequestHelper;
import lfs.server.exceptions.ExceptionSupplier;

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
		expect = new Expectations(URL, getBranch(), corpse.getTagNo());
	}

	@Test
	void verify_successful_getCorpse() throws Exception {
		when(repo.findById(TAG_NO)).thenReturn(Optional.of(corpse));
	    ResultActions result = mockMvc.perform(get(URL+TAG_NO));
	    result.andExpect(jsonPath("_links.nextOfKins.href", endsWith("/next-of-kins/"+corpse.getTagNo())));
	    expect.forEntity(result, corpse);
	}
	
	@Test
	void failed_getCorpse() throws Exception {
		String exMsg = ExceptionSupplier.corpseNotFound(TAG_NO).get().getMessage();
		
		when(repo.findById(anyString())).thenReturn(Optional.ofNullable(null));
		
	    mockMvc.perform(get(URL+TAG_NO))
	    		.andExpect(status().isNotFound())
	    		.andExpect(jsonPath("status").value(404))
	    		.andExpect(jsonPath("error", is(exMsg)));
	}
	
	@Test
	void successful_getAllCorpse() throws Exception {

		var url = URL+"?page=0&size=20&sort=createdAt,desc";
		
		var pageRequest = PageRequestHelper.from(url);
		when(repo.findAll(pageRequest)).thenReturn(new PageImpl<>(corpseList));
		
	    ResultActions result = mockMvc.perform(get(url))
	    		.andExpect(status().isOk());
	    expect.forPage(result, corpseList, url);
	}
	
	@Test
	void getOtherMortuaries_returns_the_correct_list_of_OtherMortuaries() throws Exception {
		List<OtherMortuary> list = List.of(new OtherMortuary("MKM"), 
				new OtherMortuary("Maputsoe"), 
				new OtherMortuary("Sentebale"));
		when(service.getOtherMortuaries()).thenReturn(list);

		mockMvc.perform(get(URL+"other-mortuaries"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].name").value("MKM"))
			.andExpect(jsonPath("[2].name").value("Sentebale"));

		verify(service).getOtherMortuaries(); 
	}

	private void verifyCorpse(final ResultActions action) throws Exception {
		int branchId = corpse.getBranch().getId();
		String tagNo = corpse.getTagNo();
		action.andExpect(jsonPath("tagNo").value(tagNo))
			.andExpect(jsonPath("names").value(corpse.getNames()))
			.andExpect(jsonPath("transferredFrom").doesNotExist())
			.andExpect(jsonPath("nextOfKins").doesNotExist())
			.andExpect(jsonPath("_links.self.href", endsWith(URL+tagNo)))
			.andExpect(jsonPath("_links.all.href", endsWith(URL.substring(0, URL.length()-1))))
			.andExpect(jsonPath("_links.branch.href", endsWith("/branches/"+branchId)))
			.andExpect(jsonPath("_links.nextOfKins.href", endsWith("/next-of-kins/"+tagNo)));
//		.andDo(print());
	}
	
	
//	@Test
//	void getTransferedFrom_returns_OtherMorthurys_name_where_corpse_is_transferedFrom() throws Exception {
//		String tagNo = "256000001";
//		OtherMortuary mkm = new OtherMortuary("MKM");
//		when(service.getTransforedFrom(anyString())).thenReturn(mkm);
//
//		mockMvc.perform(get("/corpses/"+tagNo+"/transferred-from"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("name").value("MKM"));
//
//		verify(service).getTransforedFrom(tagNo);
//	}
	
	private void createCorpse() {
		corpse = new Corpse();
		corpse.setTagNo("101");
		corpse.setNames("Thabo");
		corpse.setSurname("Lebese");
		corpse.setTransferredFrom(new OtherMortuary("MKM"));
		corpse.setBranch(getBranch());
		
		Corpse corpse2 = new Corpse();
		corpse2.setTagNo("102");
		corpse2.setNames("Nthabiseng");
		corpse2.setSurname("Lebese");
		corpse2.setBranch(getBranch());
		
		corpseList = List.of(corpse, corpse2);
	}
}
