package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import javax.persistence.EntityManager;

import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import lfs.server.exceptions.ObjectNotFoundException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
@ActiveProfiles("test")
class CorpseServiceIntegrationTest {

	@Autowired
	private CorpseService service;
	@Autowired
	private OtherMortuaryRepository motherMortuaryRepo;
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	void init() {
	}
	
	@Test
	void saving_corpse_should_return_instance_of_saved_corpse() {
		Corpse savedCorpse = service.save(new Corpse());
		
		assertThat(savedCorpse).isNotNull();
		assertThat(savedCorpse.getTagNo()).isNotNull()
			.containsOnlyDigits();
	}
	
	@Test
	void getCorpse() {
		Corpse corpse = new Corpse();
		service.save(corpse); 
		Corpse savedCorpse = service.get(corpse.getTagNo());
		
		assertThat(corpse).isEqualTo(savedCorpse);
	}
	
	@Test
	void getAllCorpse() {
		Corpse corpse = new Corpse();
		service.save(corpse);
		
		PageRequest pagable = PageRequest.of(0, 1);
		Page<Corpse> page = service.all(pagable);
		
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
	}
	
	@Test
	void should_fail_to_update_corpse_with_unkown_tagNo() {
		var unknownId = "unknown_id";
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Corpse());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining("Corpse with tagNo");
	}
	
//	@Test
//	void verify_that_corpse_is_updated_successfully() {
//		//It's better of with integration Test
//	}
	
	@Test
	void should_only_delete_existing_corpse() {
	    var unknownId = "unknown_id";
	    
		Throwable thrown = catchThrowable(() -> {
			service.delete(unknownId);
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining("Corpse with tagNo");
	}
	
	@Test
	void should_fail_to_update_otherMortuary_that_does_not_exist() {
		Corpse corpse = new Corpse();
		Corpse savedCorpse = service.save(corpse);
		
		entityManager.flush();
		entityManager.clear();
		
		OtherMortuary other = new OtherMortuary("MKM");
		other.setId(123);
		savedCorpse.setTransferredFrom(other);
		
		Throwable thrown = catchThrowable(() -> {
			service.update(savedCorpse.getTagNo(), savedCorpse);
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining("OtherMortuary object with id");
	}
	
	@Test
	void veryfy_that_when_saving_corpse_it_uses_existing_OtherMortuary_if_any() {
		OtherMortuary other = new OtherMortuary("MKM");
		motherMortuaryRepo.save(other);
		
		Corpse corpse = new Corpse();
		Corpse savedCorpse = service.save(corpse);
		entityManager.flush();
		entityManager.clear();
		savedCorpse.setTransferredFrom(new OtherMortuary("MKM"));
		
		Corpse c3 = service.update(savedCorpse.getTagNo(), savedCorpse);
		
		assertThat(c3.getTransferredFrom()).isEqualTo(other);
	}
}
