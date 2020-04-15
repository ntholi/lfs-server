package lfs.server.preneed.pricing.integ;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import lfs.server.preneed.pricing.FuneralScheme;
import lfs.server.preneed.pricing.FuneralSchemeRepository;
import lfs.server.preneed.pricing.FuneralSchemeService;
import lfs.server.preneed.pricing.json.FuneralSchemesJSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FuneralSchemeServiceIntegrationTest {

	@Autowired FuneralSchemeRepository repo;
	@Autowired
	private FuneralSchemeService service;
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	void init() {
	}
	
	@Test
	void save() throws IOException {
		FuneralScheme entity = FuneralSchemesJSON.withDependanciesButNoIds();
		
		var savedEntity = service.save(entity);
		
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getId()).isNotNull();
	}
	
//	@Test
//	void getCorpse() {
//		Corpse corpse = new Corpse();
//		service.save(corpse); 
//		Corpse savedCorpse = service.get(corpse.getTagNo()).orElse(null);
//		
//		assertThat(corpse).isEqualTo(savedCorpse);
//	}
//	
//	@Test
//	void getAllCorpse() {
//		Corpse corpse = new Corpse();
//		service.save(corpse);
//		
//		PageRequest pagable = PageRequest.of(0, 1);
//		Page<Corpse> page = service.all(pagable);
//		
//		assertThat(page).isNotEmpty();
//		assertThat(page).hasSize(1);
//	}
//	
//	@Test
//	void should_fail_to_update_corpse_with_unkown_tagNo() {
//		var unknownId = "unknown_id";
//		
//		Throwable thrown = catchThrowable(() -> {
//			service.update(unknownId, new Corpse());
//		});
//		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
//		assertThat(thrown).hasMessageContaining("Corpse with tagNo");
//	}
//	
//	@Test
//	void verify_that_corpse_is_updated_successfully() {
//		Corpse c1 = new Corpse();
//		c1.setNames("Names 1");
//		repo.saveAndFlush(c1);
//		
//		c1.setNames("Names 2");
//		Corpse savedCorpse = service.update(c1.getTagNo(), c1);
//		assertThat(savedCorpse.getNames()).isEqualTo("Names 2");
//	}
//	
//
//	@Test
//	void successful_delete() {
//		Corpse corpse = new Corpse();
//		corpse.setNames("Thabo");
//		repo.saveAndFlush(corpse);
//		
//		var list = repo.findById(corpse.getTagNo());
//		assertThat(list).isNotEmpty();
//		
//		service.delete(corpse.getTagNo());
//		
//		var list2 = repo.findAll();
//		assertThat(list2).isEmpty();
//	}
//	
//	@Test
//	void should_fail_to_update_otherMortuary_that_does_not_exist() {
//		Corpse corpse = new Corpse();
//		Corpse savedCorpse = service.save(corpse);
//		
//		entityManager.flush();
//		entityManager.clear();
//		
//		OtherMortuary other = new OtherMortuary("MKM");
//		other.setId(123);
//		savedCorpse.setTransferredFrom(other);
//		
//		Throwable thrown = catchThrowable(() -> {
//			service.update(savedCorpse.getTagNo(), savedCorpse);
//		});
//		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
//		assertThat(thrown).hasMessageContaining("OtherMortuary object with id");
//	}
//	
//	@Test
//	void veryfy_that_when_saving_corpse_it_uses_existing_OtherMortuary_if_any() {
//		OtherMortuary other = new OtherMortuary("MKM");
//		motherMortuaryRepo.save(other);
//		
//		Corpse corpse = new Corpse();
//		Corpse savedCorpse = service.save(corpse);
//		entityManager.flush();
//		entityManager.clear();
//		savedCorpse.setTransferredFrom(new OtherMortuary("MKM"));
//		
//		Corpse c3 = service.update(savedCorpse.getTagNo(), savedCorpse);
//		
//		assertThat(c3.getTransferredFrom()).isEqualTo(other);
//	}
}
