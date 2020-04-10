package lfs.server.preneed.pricing.unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lfs.server.common.UnitTest;
import lfs.server.preneed.pricing.FuneralScheme;
import lfs.server.preneed.pricing.FuneralSchemeRepository;
import lfs.server.preneed.pricing.FuneralSchemeService;
import lfs.server.preneed.pricing.json.FuneralSchemesJSON;

@ExtendWith(MockitoExtension.class)
class FuneralSchemeControllerUnitTest implements UnitTest {

	@Mock
	private FuneralSchemeRepository repo;
	
	@InjectMocks
	private FuneralSchemeService service;
	
	private FuneralScheme scheme;
	
	@Test
	void init() {
//		scheme = FuneralSchemesJSON.any();
		FuneralSchemesJSON.all().forEach(System.out::println);
	}
	
//	@Test
//	void saving_funeral_scheme() {
//		when(repo.save(any(FuneralScheme.class))).thenReturn(scheme);
//		
//		FuneralScheme response = service.save(new FuneralScheme());
//		
//		assertThat(response)
//			.isNotNull()
//			.isEqualTo(response);
//	}
}
