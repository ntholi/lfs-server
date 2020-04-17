package lfs.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication implements CommandLineRunner{

//	@Autowired
//	CorpseRepository repo;
			
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		NextOfKin n1 = new NextOfKin(null, "NextOfKin1 names", "NextOfKin1 surname", 
//				"father", "+2665781736", "Ha Seoli", null);
//		NextOfKin n2 = new NextOfKin(null, "NextOfKin2 names", "NextOfKin2 surname", 
//				"mother", "+2665638282", "Marabeng", null);
//		Corpse corpse = new Corpse(null, "corpseName", "corpseSurname", Gender.MALE, 
//				LocalDate.of(1953, 10, 1), "Ha Moho, Thabana Morena", 
//				District.Mafeteng, "Ntate Rasehloho", List.of(n1, n2), 
//				LocalDate.now(), LocalDateTime.now(), "Natural Causes", 
//				"At Home", "F3", "BNG-26", "Molise", new Transport(), 
//				"There are no special requirements", "No other notes", false, 
//				new OtherMortuary("MKM"));
//		n1.setCorpse(corpse);
//		n2.setCorpse(corpse);
//		repo.save(corpse);
	}

}
