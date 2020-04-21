package com.breakoutms.lfs.server.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageRequestHelper {

	public static PageRequest from(String url) {
		var map = getQueryMap(url);
		String sortBy = (String) map.get("sort");
		Serializable direction = map.get("direction");
		Sort sort;
		if(direction != null) {
			sort = Sort.by(Direction.fromString((String) direction), sortBy);
		}
		else {
			sort = Sort.by(sortBy);
		}
		int pageNo = (int) map.get("page");
		int pageSize = (int) map.get("size");
		return PageRequest.of(pageNo, pageSize, sort);
	}
	
	public static Map<String, Serializable> getQueryMap(String query)  {
		if(query.contains("?")) {
			query = query.substring(query.indexOf('?')+1);
		}
		String[] params = query.split("&");  
		Map<String, Serializable> map = new HashMap<>();
		String direction = null;
		for (String param : params)  {  
			String name = param.split("=")[0];  
			Serializable value;
			if(name.equals("sort")) {
				value = param.split("=")[1];
				if(((String)value).contains(",")){
					String[] splits = ((String)value).split(",");
					value = splits[0];
					direction = splits[1];
				}
			}
			else {
				value = Integer.valueOf(param.split("=")[1]);  
			}
			map.put(name, value);
			if(direction != null) {
				map.put("direction", direction);
			}
		}  
		return map;  
	}
}
