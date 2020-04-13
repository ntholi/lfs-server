package lfs.server.preneed.pricing.unit;


import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lfs.server.common.ServiceUnitTest;
import lfs.server.common.UnitTest;
import lfs.server.preneed.pricing.FuneralScheme;
import lfs.server.preneed.pricing.FuneralSchemeRepository;
import lfs.server.preneed.pricing.FuneralSchemeService;
import lfs.server.preneed.pricing.json.FuneralSchemesJSON;

@ExtendWith(MockitoExtension.class)
class FuneralSchemeServiceUnitTest implements UnitTest {

	@Mock
	private FuneralSchemeRepository repo;

	@InjectMocks
	private FuneralSchemeService service;

	private final ServiceUnitTest<FuneralScheme, Integer> serviceTests = new ServiceUnitTest<>() {
		@Override
		protected List<FuneralScheme> initializeList() {
			return FuneralSchemesJSON.all();
		}
	};

	@Test
	void common_tests() throws Exception {
		assertTrue(serviceTests.save());
		assertTrue(serviceTests.get());
		assertTrue(serviceTests.all());
		assertTrue(serviceTests.update());
		assertTrue(serviceTests.failtWithUnknownId(129));
		assertTrue(serviceTests.delete());
	}

}
