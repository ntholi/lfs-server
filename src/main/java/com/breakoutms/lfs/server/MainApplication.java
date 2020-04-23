package com.breakoutms.lfs.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.breakoutms.lfs.server.user.Privilege;
import com.breakoutms.lfs.server.user.PrivilegeType;
import com.breakoutms.lfs.server.user.Role;
import com.breakoutms.lfs.server.user.RoleName;
import com.breakoutms.lfs.server.user.UserService;
import com.breakoutms.lfs.server.user.dto.UserDto;

@SpringBootApplication
public class MainApplication implements CommandLineRunner{

//	@Autowired
//	CorpseRepository repo;
	
	@Autowired
	UserService userService;
			
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		userService.register(createTestUser());
		userService.register(createAdminTestUser());
		
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

	private UserDto createTestUser() {
		UserDto user = new UserDto();
		user.setFirstName("Thabo");
		user.setLastName("Lebese");
		user.setUsername("thabo");
		user.setPassword("111111");
		
		Role role = new Role();
		role.setName(RoleName.PRENEED);
		role.setPrivileges(List.of(new Privilege(PrivilegeType.READ), 
				new Privilege(PrivilegeType.WRITE)));
		user.setRoles(List.of(role));
		return user;
	}
	
	private UserDto createAdminTestUser() {
		UserDto admin = new UserDto();
		admin.setUsername("admin");
		admin.setPassword("111111");
		admin.setFirstName("Administrator");
		Role role = new Role();
		role.setName(RoleName.ADMIN);
		role.setPrivileges(List.of(
				new Privilege(PrivilegeType.READ), 
				new Privilege(PrivilegeType.WRITE),
				new Privilege(PrivilegeType.UPDATE),
				new Privilege(PrivilegeType.DELETE)
		));
		admin.setRoles(List.of(role));
		return admin;
	}

}
