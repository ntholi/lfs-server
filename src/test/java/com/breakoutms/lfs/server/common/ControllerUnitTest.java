package com.breakoutms.lfs.server.common;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.breakoutms.lfs.server.branch.Branch;
import com.breakoutms.lfs.server.core.entity.District;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public interface ControllerUnitTest extends SecuredWebTest {

	public default ResultActions post(MockMvc mockMvc, String url, Object obj) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.post(url)
				.content(asJsonString(obj))
				.characterEncoding("utf8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
	}

	public default ResultActions put(MockMvc mockMvc, String url, Object obj) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.put(url)
				.content(asJsonString(obj))
				.characterEncoding("utf8")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
	}

	public default String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public default Optional<Branch> branch() {
		Branch branch = new Branch();
		branch.setId(1);
		branch.setDistrict(District.Maseru);
		branch.setName("Maseru");
		branch.setSyncNumber((short)256);
		return Optional.of(branch);
	}
}
