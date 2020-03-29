package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.branch.CurrentBranch;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class CorpseIntegrationTest {

	private static final String CORPSE_NAME = "Thabo David";
	
	@Autowired
	private TestRestTemplate rest;
	
	@Autowired
	private CurrentBranch currentBranch;
	

	@Test @Order(1) 
	void testAddCorpseSuccess() throws Exception {
		Corpse corpse =  transferredFrom("MKM");
		System.err.println("Request Body: "+ corpse);
		ResponseEntity<Corpse> postResponse = rest.postForEntity("/corpses", 
				corpse, 
				Corpse.class);
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		System.err.println("Response: "+ postResponse.getBody());
		assertThat(postResponse.getBody()).isNotNull();
		assertThat(postResponse.getBody().getNames()).isEqualTo(CORPSE_NAME);
		assertThat(postResponse.getBody().getTransferredFrom()).isNull();
	}
	
	@Test @Order(2) 
	void testGetTransferredFrom() {
		var tagNo = currentBranch.get().getSyncNumber()+"000001";
		ResponseEntity<OtherMortuary> response = rest.getForEntity("/corpses/"+tagNo+"/transferred-from", 
				OtherMortuary.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	public static Corpse transferredFrom(String name) {
		Corpse corpse = new Corpse();
		corpse.setNames(CORPSE_NAME);
		corpse.setTransferredFrom(new OtherMortuary(name));
		return corpse;
	}

}
