package com.breakoutms.lfs.server.mortuary.corpse;

import static com.breakoutms.lfs.server.common.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.breakoutms.lfs.server.common.ControllerUnitTest;
import com.breakoutms.lfs.server.common.PageRequestHelper;
import com.breakoutms.lfs.server.common.motherbeans.mortuary.CorpseMother;
import com.breakoutms.lfs.server.config.GeneralConfigurations;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;


@ExtendWith(SpringExtension.class)
@WebMvcTest(CorpseController.class)
@Import(GeneralConfigurations.class)
public class CorpseControllerUnitTest implements ControllerUnitTest {

	private static final String DEFAULT_ROLE = "ROLE_MORTUARY";

	@Autowired private MockMvc mockMvc;
	@MockBean private CorpseRepository repo;
	@MockBean private OtherMortuaryRepository otherMortuaryRepo;
	@SpyBean private CorpseService service;
	@MockBean private UserDetailsServiceImpl requiredBean;
	private CorpseMapper modelMapper = CorpseMapper.INSTANCE;

	private final String ID = "7";
	private Corpse entity = persistedEntity();
	private final String URL = "/mortuary/corpses/";

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

		mockMvc.perform(get(URL+ID))
				.andExpect(responseBody().hasLink("all", "/corpses"))
				.andExpect(responseBody().hasLink("nextOfKin", "/nextOfKin"))
				.andExpect(responseBody().hasLink("transferredFrom", "/transferredFrom"))
				.andExpect(responseBody().hasLink("transportx", "/transport"))
				.andExpect(responseBody().hasLink("self", "/corpses/"+ID))
				.andExpect(responseBody().hasLink("branch", "/corpses/1"));
	}

	@Test
	@WithMockUser(authorities = {READ, DEFAULT_ROLE})
	void get_with_unkown_id_throws_notFound() throws Exception {
		var unkownId = "122423";
		mockMvc.perform(get(URL+unkownId))
			.andExpect(responseBody().notFound(Corpse.class, unkownId));

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
			.andExpect(responseBody().pagedModel("products").contains(viewModel))
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
		when(repo.save(any(Corpse.class))).thenReturn(entity);

		var dto = modelMapper.map(entity);
		var viewModel = modelMapper.map(entity);
		
		post(mockMvc, URL, dto)
			.andExpect(status().isCreated())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(repo).save(entity);
	}
	
	@Test
	@WithMockUser(authorities = {WRITE, DEFAULT_ROLE})
	void save_fails_because_of_invalid_field() throws Exception {
		entity.setNames("1");

		post(mockMvc, URL, entity)
			.andExpect(responseBody().containsErrorFor("name"));

		verify(service, times(0)).save(any(Corpse.class));
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update() throws Exception {
		var dto = modelMapper.map(entity);
		var viewModel = modelMapper.map(entity);
		
		when(repo.findById(ID)).thenReturn(Optional.of(entity));
		when(repo.save(any(Corpse.class))).thenReturn(entity);

		put(mockMvc, URL+ID, dto)
			.andExpect(status().isOk())
			.andExpect(responseBody().isEqualTo(viewModel));

		verify(service).update(eq(ID), any(Corpse.class));
		verify(repo).save(entity);
	}
	
	@Test
	@WithMockUser(authorities = {UPDATE, DEFAULT_ROLE})
	void update_fails_if_any_field_is_invalid() throws Exception {
		entity.setNames("1");
		
		put(mockMvc, URL+ID, entity)
			.andExpect(responseBody().containsErrorFor("name"));
	}

	private Corpse persistedEntity() {
		return new CorpseMother()
				.id(ID)
				.build();
	}
	
	private Corpse newEntity() {
		return new CorpseMother()
				.noBranchNoID()
				.build();
	}
}
