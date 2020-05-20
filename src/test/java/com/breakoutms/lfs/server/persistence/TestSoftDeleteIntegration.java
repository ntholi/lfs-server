package com.breakoutms.lfs.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.google.common.base.CaseFormat;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TestSoftDeleteIntegration {
	
	@Autowired
	private FuneralSchemeRepository funeralSchemeRepo;
	
	@Test
	void test_soft_delete_with_FuneralScheme() {
		String name = "ANC";
		
		FuneralScheme fs = new FuneralScheme(name);
		funeralSchemeRepo.save(fs);
		assertThat(funeralSchemeRepo.findAll()).hasSize(1);
		
		funeralSchemeRepo.delete(fs);
		assertThat(funeralSchemeRepo.findByName(name)).isEmpty();
	}
	
}
