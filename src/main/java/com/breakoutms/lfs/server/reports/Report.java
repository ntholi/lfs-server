package com.breakoutms.lfs.server.reports;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class Report<T>{

	private Map<String, Object> content = new HashMap<>();
	
	public Report() {}
	
	public Report(Iterable<T> data) {
		setData(data);
	}

	public void setData(Iterable<T> data) {
		content.put("data", data);
	}
	
	public void add(String key, Object value) {
		content.put(key, value);
	}
}
