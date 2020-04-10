package lfs.server.common;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import lfs.server.branch.Branch;

public interface ControllerUnitTest extends UnitTest {
	
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
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	} 
	
}
