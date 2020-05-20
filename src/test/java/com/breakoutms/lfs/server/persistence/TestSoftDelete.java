package com.breakoutms.lfs.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.google.common.base.CaseFormat;

class TestSoftDelete {

	private final String deleteFormat = "UPDATE %s SET deleted=true WHERE id=?";
	
	@Test
	void verify_that_soft_delete_is_wired_correctly() {
		Reflections reflections = new Reflections("com.breakoutms.lfs.server");
		List<Class<?>> types = reflections
				.getSubTypesOf(AuditableEntity.class)
				.stream().collect(Collectors.toList());
		
		int size = types.size();
		for (int i = 0; i < size; i++) {
			Class<?> type = types.get(i);
			System.out.println((i+1)+"/"+size+") Checking soft delete wiring in -> "+ type.getName());
			
			assertThat(type.getAnnotation(Where.class)).isNotNull();
			assertThat(type.getAnnotation(SQLDelete.class)).isNotNull();
			assertThat(type.getAnnotation(SQLDelete.class).sql()).isEqualTo(sqlDelete(type));
			assertThat(type.getAnnotation(Where.class).clause()).isEqualTo(AuditableEntity.CLAUSE);
		}
	}

	private String sqlDelete(Class<?> type) {
		String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, type.getSimpleName());
		return String.format(deleteFormat, name);
	}

}
