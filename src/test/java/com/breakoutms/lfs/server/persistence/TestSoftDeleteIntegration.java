package com.breakoutms.lfs.server.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeRepository;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

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
