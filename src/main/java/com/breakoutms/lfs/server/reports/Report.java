package com.breakoutms.lfs.server.reports;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Report<T>{

	public static final String DATA_KEY = "data";
	private Map<String, Object> content = new HashMap<>();
	
	public Report() {}
	
	public Report(String key, Object value) {
		content.put(key, value);
	}
	
	public Report(Iterable<T> data) {
		setData(data);
	}

	public void setData(Iterable<T> data) {
		content.put(DATA_KEY, data);
	}
	
	public void add(String key, Object value) {
		content.put(key, value);
	}

	public Object get(String key) {
		return content.get(key);
	}
}
