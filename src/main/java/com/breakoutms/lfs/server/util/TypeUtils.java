package com.breakoutms.lfs.server.util;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.math.NumberUtils;

public class TypeUtils {

	public static boolean isString(Class<?> type) {
		return String.class.equals(type);
	}
	
	public static boolean isLocalDate(Field field){
		return isLocalDate(field.getType());
	}
	
	public static boolean isLocalDate(Class<?> type){
		return LocalDate.class.isAssignableFrom(type);
	}
	
	public static boolean isLocalDateTime(Field field){
		return isLocalDateTime(field.getType());
	}
	
	public static boolean isLocalDateTime(Class<?> type){
		return LocalDateTime.class.isAssignableFrom(type);
	}
	
	public static boolean isEnum(Field field){
		return isEnum(field.getType());
	}
	
	public static boolean isEnum(Class<?> type){
		return type.isEnum();
	}
	
	public static boolean isBoolean(Field field) {
		return isBoolean(field.getType());
	}
	
	public static boolean isBoolean(Class<?> type) {
		return Boolean.class.equals(type) 
				|| boolean.class.equals(type);
	}
	
	public static boolean isNumeric(Field field){
		return isNumeric(field.getType());
	}
	
	public static boolean isNumeric(Class<?> type){
		return Byte.class.equals(type)
				|| byte.class.equals(type)
				|| Short.class.equals(type) 
				|| short.class.equals(type)
				|| Integer.class.equals(type) 
				|| int.class.equals(type)
				|| Long.class.equals(type) 
				|| long.class.equals(type)
				|| Float.class.equals(type) 
				|| float.class.equals(type)
				|| Double.class.equals(type) 
				|| double.class.equals(type);
	}
	
	public static Object castType(Object value, Class<?> type) {
		Object o = null;
		try {
			if(type == String.class){
				o = String.valueOf(value);
			}
			else if(type == Double.class || type  ==  Double.TYPE){
				o = numericCast(value, type, 0.0);
				
			}
			else if (type == Float.class ||type ==  Float.TYPE){
				o = numericCast(value, type, 0.0);
			}
			else if(type == Integer.class || type == Integer.TYPE){
				o = Integer.valueOf(String.valueOf(value));
			}
			else if(type == Long.class || type == Long.TYPE){
				o = Long.valueOf(String.valueOf(value));
			}
			else if(type == Boolean.class || type == Boolean.TYPE){
				o = Boolean.valueOf(String.valueOf(value));
			}
			else{
				o = type.cast(value);
			}
		} 
		catch (NumberFormatException e) {
			o = 0;
		}
		catch(ClassCastException e) {
			e.printStackTrace();
		}

		return o;
	}

	private static Object numericCast(Object value, Class<?> type, Object defaultValue) {
		Object o;
		if(Double.class.equals(value.getClass())){
			o = value;
		}
		else if(String.class.equals(value.getClass())){
			String str = (String) value;
			if(NumberUtils.isCreatable(str)){
				o = Double.valueOf(str);
			}
			else{
				o = defaultValue;
			}
		}
		else{
			o = type.cast(value);
		}
		return o;
	}
}
