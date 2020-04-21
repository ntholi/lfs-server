package com.breakoutms.lfs.server.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

public class FieldUtils {

	private FieldUtils() {}

	/**
	 * returns all fields inside fields variable including fields from the super class
	 * @param type
	 * @param fields
	 */
	public static <T> List<Field> getFields(Class<T> type) {
		List<Field> fields = new ArrayList<>();
		for(Class<?> c = type; c != null; c = c.getSuperclass()){
			fields.addAll(0, Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}


	/**
	 * Returns a {@link Field} with a specified name
	 * @param type
	 * @param name
	 * @return
	 */
	public static <T> Field getField(Class<T> type, String fieldName) {
		Field result = null;
		for(Field field: getFields(type)){
			if(field.getName().equals(fieldName)){
				result = field;
			}
		}
		return result;
	}

	/**
	 * @param type
	 * @param string
	 * @return
	 */
	public static Field getIdField(Class<?> type) {
		Field idField = null;
		for(Field field: getFields(type)){
			if(field.getAnnotation(Id.class) != null){
				idField = field;
			}
		}
		return idField;
	}
	
	public static boolean isIdField(Field field) {
		return field.getAnnotation(Id.class) != null;
	}
}
