package com.breakoutms.lfs.server;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
public class MainApplication implements CommandLineRunner{

//	@Autowired
//	CorpseRepository repo;
	
//	@Autowired
//	UserService userService;
//	@Autowired
//	BranchRepository branchRepository;
//	private User createTestUser() {
//	User user = new User();
//	user.setFirstName("Thabo");
//	user.setLastName("Lebese");
//	user.setUsername("thabo");
//	user.setPassword("111111");
//	
//	Role role = new Role();
//	role.setName(Domain.PRENEED);
//	role.setPrivileges(List.of(new Privilege(PrivilegeType.READ), 
//			new Privilege(PrivilegeType.WRITE)));
//	user.setRoles(List.of(role));
//	setBranch(user);
//	return user;
//}
//
//private void setBranch(User user) {
//	Branch branch = branchRepository.findAll().iterator().next();
//	user.setBranch(branch);
//}
//	private User createAdminTestUser() {
//	User admin = new User();
//	admin.setUsername("admin");
//	admin.setPassword("111111");
//	admin.setFirstName("Administrator");
//	Role role = new Role();
//	role.setName(Domain.ADMIN);
//	role.setPrivileges(List.of(
//			new Privilege(PrivilegeType.READ), 
//			new Privilege(PrivilegeType.WRITE),
//			new Privilege(PrivilegeType.UPDATE),
//			new Privilege(PrivilegeType.DELETE)
//	));
//	setBranch(admin);
//	admin.setRoles(List.of(role));
//	return admin;
//}

	@Override
	public void run(String... args) throws Exception {
		
//		userService.register(createTestUser());
//		userService.register(createAdminTestUser());
		
		
		
		
		
		
		
		
		
		
		
		
		
		
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
	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "LS"));
		SpringApplication.run(MainApplication.class, args);
	}
}
