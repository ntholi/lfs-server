package lfs.server.mortuary.unit;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lfs.server.mortuary.CorpseController;
import lfs.server.mortuary.CorpseService;

@ExtendWith(MockitoExtension.class)
class CorpseControllerTest {

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
	void getRecievedBy_returnsCorrectResults() throws Exception {
		List<String> list = List.of("Thabo", "Lebese", "Molapo");
		when(service.getReceivedBy()).thenReturn(list);

		mockMvc.perform(get("/corpses/received-by"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0]").value("Thabo"))
			.andExpect(jsonPath("[2]").value("Molapo"));
		
		verify(service).getReceivedBy();
	}
}
