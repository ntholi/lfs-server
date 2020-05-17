package com.breakoutms.lfs.server.common;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.test.web.servlet.ResultActions;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.branch.Branch;
import com.breakoutms.lfs.server.util.FieldUtils;

public class Expectations {

	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
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
		result
//				.andDo(print())
				.andExpect(jsonPath("page.totalElements").value(totalElements))
				.andExpect(jsonPath("page.totalPages").value(Math.ceil(totalElements/pageSize)))
				.andExpect(jsonPath("page.number").value(pageNo))
				.andExpect(jsonPath("page.size", either(is(size)).or(is(pageSize))));
		System.out.println("Before Sort: "+ list);
		list.sort((T o1, T o2) ->{
				if(o1 instanceof AuditableEntity && o2 instanceof AuditableEntity) {
					AuditableEntity a1 = (AuditableEntity) o1; 
					AuditableEntity a2 = (AuditableEntity) o2;
					if(a1.getCreatedAt() != null && a2.getCreatedAt() != null) {
						return a1.getCreatedAt().compareTo(a2.getCreatedAt());
					}
				}
				return 0;
		});
		System.err.println("After Sort: "+ list);
		for (int i = 0; i < list.size(); i++) {
			T t = list.get(i);
			result = forEntity(result, t, "_embedded."+listName+"["+i+"]");
		}
		return result;
	}
	
	public <T> ResultActions forEntity(final ResultActions result, final T object, String path) throws Exception {
		Map<String, Object> values = getValues(object);
		System.out.println("Validating the following fields: ");
	    for (var item : values.entrySet()) {
	    	if(item.getKey().equals("createdAt")
	    			|| item.getKey().equals("createdBy")
	    			|| item.getKey().equals(FieldUtils.getIdField(object.getClass()).getName())) {
	    		//skip createdAt, and id field because with integration tests, their value will be assigned after
	    		//the object is saved in the database, while the expected value will still be null because 
	    		//the provided object did not assign their values
	    		System.err.println("Skipping "+ item);
	    		continue;
	    	}
	    	System.out.println(item);
	    	if(item.getValue() != null) {
	    		Object value = item.getValue();
	    		if(value instanceof Temporal) {
	    			String strVal = value.toString();
	    			value = removeTrailingZeros(strVal);
	    		}
	    		result.andExpect(jsonPath(path+item.getKey()).value(value));
	    	}
	    	else {
	    		result.andExpect(jsonPath(path+item.getKey()).isEmpty());
	    	}
	    	fieldsThatShouldNotHaveAPath(object).forEach(it ->{
	    		try {
					result.andExpect(jsonPath(it).doesNotExist());
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	});
	    }
	    forCommonLinks(result, getId(object), path);
		return result;
	}

	public <T> ResultActions forCommonLinks(final ResultActions result, final Object id) throws Exception {
		return forCommonLinks(result, id, "");
	}
	
	private <T> ResultActions forCommonLinks(final ResultActions result, final Object id, String path) throws Exception {
		result.andExpect(jsonPath(path+"_links.self.href", endsWith(baseUrl+id)));
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
	
	public static ResultActions forObjectNotFound(ResultActions result, String message) throws Exception {
		return result.andExpect(status().isNotFound())
				.andExpect(jsonPath(STATUS).value(404))
				.andExpect(jsonPath(MESSAGE, is(message)));
	}
	
	public static ResultActions forInvalidFields(ResultActions result, String message) throws Exception {
		return result.andExpect(status().isBadRequest())
				.andExpect(jsonPath(STATUS).value(400))
				.andExpect(jsonPath(MESSAGE, is(message)));
	}

	private static <T> Map<String, Object> getValues(T object){
		Map<String, Object> map = new HashMap<>();
		var list = FieldUtils.getFields(object.getClass());
		for (Field field : list) {
			if(Serializable.class.isAssignableFrom(field.getType())
					&& !Collection.class.isAssignableFrom(field.getType())) {
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
	
	private <T> List<String> fieldsThatShouldNotHaveAPath(T object) {
		List<String> result = new ArrayList<>();
		for (Field field : FieldUtils.getFields(object.getClass())) {
			if(Collection.class.isAssignableFrom(field.getType())) {
				result.add(field.getName());
			}	
		}
		return result;
	}
	
	protected String removeTrailingZeros(String strVal) {
		var chArray = strVal.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean endOfTailingZeros = false;
		for(int i = chArray.length-1; i >= 0; i--) {
			if(chArray[i] != '0') {
				endOfTailingZeros = true;
			}
			if(chArray[i] == '0' && endOfTailingZeros == false) {
				continue;
			}
			sb.append(chArray[i]);
		}
		return sb.reverse().toString();
	}
}
