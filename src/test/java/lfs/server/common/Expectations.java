package lfs.server.common;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.ResultActions;

import lfs.server.branch.Branch;
import lfs.server.util.FieldUtils;

public class Expectations {

	private String baseUrl;
	private Branch branch;

	public Expectations(String url, Branch branch) {
		this.baseUrl = url;
	}

	public <T> ResultActions forPage(ResultActions result, List<T> list, String listName, String url) throws Exception {
		var map = PageRequestHelper.getQueryMap(url);
		int pageNo = (int) map.get("page");
		int size = (int) map.get("size");;
		int totalElements = list.size();
		int pageSize = totalElements > size? size: totalElements;
		result.andExpect(jsonPath("page.size").value(pageSize))
				.andExpect(jsonPath("page.totalElements").value(totalElements))
				.andExpect(jsonPath("page.totalPages").value(Math.ceil(totalElements/pageSize)))
				.andExpect(jsonPath("page.number").value(pageNo));
		for (int i = 0; i < list.size(); i++) {
			T t = list.get(i);
			result = forEntity(result, t, "_embedded."+listName+"["+i+"]");
		}
		return result;
	}
	
	public <T> ResultActions forEntity(final ResultActions result, final T object, String path) throws Exception {
		result.andExpect(status().isOk());
		var values = getValues(object);
		System.out.println("Validating the following fields: ");
	    for (var item : values.entrySet()) {
	    	System.out.println(item);
	    	if(item.getValue() != null) {
	    		result.andExpect(jsonPath(path+item.getKey()).value(item.getValue()));
	    	}
	    	else {
	    		result.andExpect(jsonPath(path+item.getKey()).isEmpty());
	    	}
	    }
	    result.andExpect(jsonPath(path+"_links.self.href", endsWith(baseUrl+getId(object))));
	    result.andExpect(jsonPath(path+"_links.all.href", endsWith(baseUrl.substring(0, baseUrl.length()-1))));
	    if(branch != null) {
	    	result.andExpect(jsonPath(path+"_links.branch.href", endsWith("/branches/"+branch.getId())));
	    }
		return result;
	}

	public <T> ResultActions forEntity(final ResultActions result, final T object) throws Exception {
		return forEntity(result, object, "");
	}
	
	private <T> Object getId(T object) {
		Field field = FieldUtils.getIdField(object.getClass());
		try {
			field.setAccessible(true);
			return field.get(object);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private static <T> Map<String, Object> getValues(T object){
		Map<String, Object> map = new HashMap<>();
		var list = FieldUtils.getFields(object.getClass());
		for (Field field : list) {
			if(Serializable.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				String key = field.getName();
				try {
					Object value =  field.get(object);
					map.put(key, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					System.err.println("Error optaining value for field "+ key 
							+" of object "+object.getClass());
				}
			}
		}
		
		return map;
	}
}
