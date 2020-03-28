package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CorpseIntegrationTest {

	private static final String CORPSE_NAME = "Thabo David";
	@Autowired
	private TestRestTemplate rest;
	
	@Test
	void corpsePost_savesCorpseButDoesNotReturnOtherMortuary() throws Exception {
		ResponseEntity<Corpse> postResponse = rest.postForEntity("/corpses", 
				transferredFrom("MKM"), 
				Corpse.class);
		assertThat(postResponse).isNotNull();
		assertThat(postResponse.getBody()).isNotNull();
		assertThat(postResponse.getBody().getNames()).isEqualTo(CORPSE_NAME);
		assertThat(postResponse.getBody().getTransferredFrom()).isNull();
	}
	
	
	public static Corpse transferredFrom(String name) {
		Corpse corpse = new Corpse();
		corpse.setNames(CORPSE_NAME);
		corpse.setTransferredFrom(new OtherMortuary(name));
		return corpse;
	}

}
