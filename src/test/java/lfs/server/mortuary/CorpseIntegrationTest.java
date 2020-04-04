package lfs.server.mortuary;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lfs.server.branch.CurrentBranch;
import lfs.server.core.RestResponsePage;

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
	void test_adding_corpse() throws Exception {
		Corpse corpse =  transferredFrom("MKM");
		ResponseEntity<Corpse> postResponse = rest.postForEntity("/corpses", 
				corpse, 
				Corpse.class);
		
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(postResponse.getBody()).isNotNull();
		assertThat(postResponse.getBody().getNames()).isEqualTo(CORPSE_NAME);
	}
	
	@Test @Order(2) 
	void test_getTransferedFrom() {
		var tagNo = currentBranch.get().getSyncNumber()+"000001";
		ResponseEntity<OtherMortuary> response = rest.getForEntity("/corpses/"+tagNo+"/transferred-from", 
				OtherMortuary.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test @Order(3)
	void test_get_all_corpse() {
		ParameterizedTypeReference<RestResponsePage<Corpse>> responseType = new ParameterizedTypeReference<RestResponsePage<Corpse>>() { };
		ResponseEntity<RestResponsePage<Corpse>> result = rest.exchange("/corpses", HttpMethod.GET, null/*httpEntity*/, responseType);
		List<Corpse> searchResult = result.getBody().getContent();
		
		System.err.println("************ size: "+searchResult.size());
		for (Corpse corpse : searchResult) {
			System.err.println(corpse);
		}
		
	}
	
	public static Corpse transferredFrom(String name) {
		Corpse corpse = new Corpse();
		corpse.setNames(CORPSE_NAME);
		corpse.setTransferredFrom(new OtherMortuary(name));
		return corpse;
	}

}
