package com.breakoutms.lfs.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.google.common.base.CaseFormat;

class IdGeneratorTest {

	@Test
	void checkIf_IdsAregeneratedCorrectly() throws Exception {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forJavaClassPath()));
		List<Class<?>> types = reflections
				.getTypesAnnotatedWith(GenericGenerator.class)
				.stream().collect(Collectors.toList());
		
		int size = types.size();
		for (int i = 0; i < size; i++) {
			Class<?> type = types.get(i);
			System.out.println((i+1)+"/"+size+") Checking ids for "+ type);
			String idTable = getIdTable(type);
			assertThat(getDefinedIdTypeParam(type)).isEqualTo(getIdType(type));
			assertThat(getClassDeclaredGeneratedValue(type)).isEqualTo(idTable);
			assertThat(getIDGeneratorValue(type)).isEqualTo(idTable);
		}
	}

	private String getIdTable(Class<?> type) {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, type.getSimpleName())+"_id";
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Serializable> getDefinedIdTypeParam(Class<?> type) throws ClassNotFoundException {
		if (type.isAnnotationPresent(GenericGenerator.class)) {
			final GenericGenerator annotation = type.getAnnotation(GenericGenerator.class);
			for (Parameter param : annotation.parameters()) {
				if(param.name().equals(IdGenerator.ID_TYPE_PARAM)) {
					return (Class<? extends Serializable>) Class.forName(param.value());
				}
			}
		}
		return null;
	}

	public String getIDGeneratorValue(Class<?> type) throws ClassNotFoundException {
		for(Field field: getFields(type)) {
			if(field.isAnnotationPresent(GeneratedValue.class)) {
				final GeneratedValue annotation = field.getAnnotation(GeneratedValue.class);
				return annotation.generator();
			}
		}

		return null;
	}

	public String getClassDeclaredGeneratedValue(Class<?> type) throws ClassNotFoundException {
		if(type.isAnnotationPresent(GenericGenerator.class)) {
			final GenericGenerator annotation = type.getAnnotation(GenericGenerator.class);
			return annotation.name();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends Serializable> getIdType(Class<?> type) {
		Field field = null;
		for(Field item: getFields(type)){
			if(item.getAnnotation(Id.class) != null){
				field = item;
			}
		}
		return (Class<? extends Serializable>) field.getType();
	}

	private static <T> List<Field> getFields(Class<T> type) {
		List<Field> fields = new ArrayList<>();
		for(Class<?> c = type; c != null; c = c.getSuperclass()){
			fields.addAll(0, Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}
}
