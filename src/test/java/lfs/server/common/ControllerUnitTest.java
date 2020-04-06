package lfs.server.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import lfs.server.branch.Branch;

public interface ControllerUnitTest {

	public default Branch getBranch() {
		Branch branch = new Branch();
		branch.setId(1);
		branch.setName("Maseru");
		branch.setSyncNumber((short)256);
		return branch;
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
