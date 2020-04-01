package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import lfs.server.exceptions.ObjectNotFoundException;

@ExtendWith(MockitoExtension.class)
class CorpseServiceUnitTest {

	private String tagNo = "256000001";
	
	@Mock
	private CorpseRepository corpseRepo;
	@Mock
	private OtherMortuaryRepository otherMortuaryRepo;
	@InjectMocks
	private CorpseService service;
	
	@Test
	void saving_corpse_should_return_instance_of_saved_corpse() {
		when(corpseRepo.save(any(Corpse.class))).thenReturn(savedCorpse());
		
		Corpse corpse = service.save(new Corpse());
		
		assertThat(corpse).isNotNull();
		assertThat(corpse.getTagNo()).isEqualTo(tagNo);
	}
	
	@Test
	void getCorpse() {
		when(corpseRepo.findById(tagNo)).thenReturn(Optional.of(savedCorpse()));
		Corpse corpse = service.get(tagNo);
		assertThat(corpse).isEqualTo(savedCorpse());
	}
	
	@Test
	void getAllCorpse() {
		PageRequest pagable = PageRequest.of(0, 1);

		when(corpseRepo.findAll(pagable)).thenReturn(new PageImpl<Corpse>(List.of(savedCorpse()), pagable, 1));
		
		Page<Corpse> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
	}
	
	@Test
	void should_fail_to_update_corpse_with_unkown_tagNo() {
		var unknownId = "unknown_id";
		when(corpseRepo.existsById(unknownId)).thenReturn(false);
		
		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, new Corpse());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining("Corpse with tagNo");
	}
	
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
		OtherMortuary other = new OtherMortuary("MKM");
		other.setId(123);
		corpse.setTransferredFrom(other);
		
		when(corpseRepo.existsById(anyString())).thenReturn(true);
		when(otherMortuaryRepo.existsById(anyInt())).thenReturn(false);
		
		Throwable thrown = catchThrowable(() -> {
			service.update(tagNo, corpse);
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining("OtherMortuary object with id");
	}
	
	@Test
	void veryfy_that_when_saving_corpse_it_uses_existing_OtherMortuary_if_any() {
		final String mortuaryName = "MKM";
		OtherMortuary other = new OtherMortuary(mortuaryName);
		
		when(corpseRepo.existsById(tagNo)).thenReturn(true);
		when(corpseRepo.save(any(Corpse.class))).thenReturn(savedCorpse());
		
		Corpse savedCorpse = service.save(new Corpse());
		savedCorpse.setTransferredFrom(new OtherMortuary(mortuaryName));
		
		Corpse c3 = service.update(savedCorpse.getTagNo(), savedCorpse);
		
		assertThat(c3.getTransferredFrom()).isEqualTo(other);
	}
	
	public Corpse savedCorpse() {
		Corpse corpse = new Corpse();
		corpse.setTagNo(tagNo);
		return corpse;
	}
}
