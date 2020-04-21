package com.breakoutms.lfs.server.mortuary.integ;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.Expectations;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.Corpse;
import com.breakoutms.lfs.server.mortuary.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.NextOfKin;
import com.breakoutms.lfs.server.mortuary.OtherMortuary;
import com.breakoutms.lfs.server.mortuary.OtherMortuaryRepository;
import com.google.common.collect.Lists;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CorpseControllerIntegrationTest implements ControllerUnitTest {


	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private CorpseRepository repo;
	@Autowired 
	private OtherMortuaryRepository otherMortuaryRepo;

	private Corpse corpse;
	private List<Corpse> corpseList;
	private final String URL = "/corpses/";

	private Expectations expect;

	@BeforeEach
	public void setup() {
		createCorpse();
		expect = new Expectations(URL, getBranch());
	}

	@Test
	void get_corpse() throws Exception {
		ResultActions result = mockMvc.perform(get(URL+corpse.getTagNo()));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("_links.nextOfKins.href", endsWith("/next-of-kins/"+corpse.getTagNo())));
		result.andExpect(jsonPath("_links.transferredFrom.href", endsWith("/other-mortuaries/"
				+corpse.getTransferredFrom().getId())));
		result.andDo(print());
		expect.forEntity(result, corpse);
	}

	@Test
	void get_for_non_existing_corpse() throws Exception {
		String unknownId = "unknown_id";
		String exMsg = ExceptionSupplier.notFound("Corpse",unknownId).get().getMessage();

		var result = mockMvc.perform(get(URL+unknownId))
				.andExpect(status().isNotFound());
		Expectations.forObjectNotFound(result, exMsg);
	}

	@Test
	void get_all_corpse() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";

		ResultActions result = mockMvc.perform(get(url))
				.andExpect(status().isOk());
		expect.forPage(result, corpseList, "corpseList", url);
	}

	@Test
	void get_all_corpse_with_no_content() throws Exception {
		var url = URL+"?page=0&size=20&sort=createdAt,desc";

		repo.deleteAll();

		mockMvc.perform(get(url)).andExpect(status().isNoContent());
	}

	@Test
	void succesfull_corpse_save() throws Exception {
		Corpse corpse = new Corpse();
		corpse.setNextOfKins(List.of(new NextOfKin()));
		corpse.setTransferredFrom(new OtherMortuary());
		corpse.setNames("Thabo");
		var result = post(mockMvc, URL, corpse);
		result.andExpect(status().isCreated())
		.andExpect(jsonPath("names").value("Thabo"));
		expect.forCommonLinks(result, String.valueOf(lastId()+1));
	}

	@Test
	void save_fails_because_of_invalid_field() throws Exception {
		String exMsg = "Invalid input for Names";

		Corpse corpse = new Corpse();
		corpse.setNames("N");

		var result = post(mockMvc, URL, corpse).andExpect(status().isBadRequest());

		Expectations.forInvalidFields(result, exMsg);
	}
	
	@Test
	void update_corpse() throws Exception {

		Corpse newCorpse = new Corpse();
		newCorpse.setNames("Some different name");
		var result = put(mockMvc, URL+corpse.getTagNo(), newCorpse);
		System.out.println();
		
		result.andExpect(status().isOk())
			.andExpect(jsonPath("names", is("Some different name")));
	}
	
	@Test
	void getOtherMortuaries_returns_the_correct_list_of_OtherMortuaries() throws Exception {
		List<OtherMortuary> list = List.of(new OtherMortuary("Molise"), 
				new OtherMortuary("Maputsoe"), 
				new OtherMortuary("Sentebale"));
		otherMortuaryRepo.saveAll(list);

		mockMvc.perform(get(URL+"other-mortuaries"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].name").value(corpse.getTransferredFrom().getName()))
			.andExpect(jsonPath("[1].name").value("Molise"))
			.andExpect(jsonPath("[3].name").value("Sentebale"));
	}
	
	@Test
	void test_getOtherMortuaries_returns_no_content_when_list_is_empty() throws Exception {
		repo.deleteAll();
		otherMortuaryRepo.deleteAll();

		mockMvc.perform(get(URL+"other-mortuaries"))
			.andExpect(status().isNoContent());
	}
	
	@Test
	void getTransferedFrom_returns_OtherMorthurys_name_where_corpse_is_transferedFrom() throws Exception {
		OtherMortuary mkm = corpse.getTransferredFrom();

		mockMvc.perform(get(URL+"other-mortuaries/"+mkm.getId()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value(mkm.getName()));
	}
	
	@Test
	void getNextOfKins_returns_the_correct_list() throws Exception {
		var n1 = new NextOfKin("David", "Moleko");n1.setCorpse(corpse);
		var n2 = new NextOfKin("Molise", "Molemo");n2.setCorpse(corpse);
		var n3 = new NextOfKin("Rorisang", "Motlomelo");n3.setCorpse(corpse);
		
		corpse.setNextOfKins(Lists.newArrayList(n1, n2, n3));
		
		mockMvc.perform(get(URL+"next-of-kins/"+corpse.getTagNo()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].names").value("David"))
			.andExpect(jsonPath("[2].surname").value("Motlomelo"));
	}
	
	@Test
	void test_getNextOfKins_returns_no_content_when_list_is_empty() throws Exception {
		mockMvc.perform(get(URL+"next-of-kins/some_unkonwn_tag_no"))
			.andExpect(status().isNoContent());
	}
	

	private Long lastId() {
		String tagNo = corpseList.get(corpseList.size()-1).getTagNo();
		return Long.valueOf(tagNo);
	}

	private void createCorpse() {
		corpse = new Corpse();
		corpse.setNames("Thabo");
		corpse.setSurname("Lebese");
		OtherMortuary om = new OtherMortuary("MKM");
		corpse.setTransferredFrom(om);
		corpse.setBranch(getBranch());

		Corpse corpse2 = new Corpse();
		corpse2.setNames("Nthabiseng");
		corpse2.setSurname("Lebese");
		corpse2.setBranch(getBranch());

		corpseList = Arrays.asList(corpse, corpse2);
		repo.saveAll(corpseList);
		System.out.println(corpse.getTagNo());
	}
}
