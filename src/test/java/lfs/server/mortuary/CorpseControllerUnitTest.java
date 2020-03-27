package lfs.server.mortuary;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lfs.server.mortuary.CorpseController;
import lfs.server.mortuary.CorpseService;
import lfs.server.mortuary.OtherMortuary;

@ExtendWith(MockitoExtension.class)
class CorpseControllerUnitTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CorpseController controller;

	@Mock
	private CorpseService service;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void getOtherMortuaries_returnsCorrectResults() throws Exception {
		List<OtherMortuary> list = List.of(new OtherMortuary("MKM"), 
				new OtherMortuary("Maputsoe"), 
				new OtherMortuary("Sentebale"));
		when(service.getOtherMortuaries()).thenReturn(list);

		mockMvc.perform(get("/corpses/other-mortuaries"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].name").value("MKM"))
			.andExpect(jsonPath("[2].name").value("Sentebale"));
		
		verify(service).getOtherMortuaries();
	}
	
	@Test
	void getTransferedFrom_returnsCorrectResults() throws Exception {
		String tagNo = "256000001";
		OtherMortuary mkm = new OtherMortuary("MKM");
		when(service.getOtherMortuaries(anyString())).thenReturn(List.of(mkm));

		mockMvc.perform(get("/corpses/"+tagNo+"/transferred-from"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("MKM"));
		
		verify(service).getOtherMortuaries(tagNo);
	}
}
