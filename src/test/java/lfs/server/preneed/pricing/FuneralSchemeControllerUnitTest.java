package lfs.server.preneed.pricing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lfs.server.mortuary.CorpseRepository;
import lfs.server.mortuary.CorpseService;
import lfs.server.mortuary.OtherMortuaryRepository;

@ExtendWith(MockitoExtension.class)
class FuneralSchemeControllerUnitTest {

	@Mock
	private FuneralSchemeRepository corpseRepo;
	@InjectMocks
	private FuneralSchemeService service;
	
	@Test
	void test() {
	}

}
