package com.breakoutms.lfs.server.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.UserAlreadyExistsException;
import com.breakoutms.lfs.server.security.JwtUtils;
import com.breakoutms.lfs.server.user.dto.LoginResponseDto;
import com.breakoutms.lfs.server.user.dto.UserDto;
import com.breakoutms.lfs.server.user.repo.PrivilegeRepository;
import com.breakoutms.lfs.server.user.repo.RoleRepository;
import com.breakoutms.lfs.server.user.repo.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PrivilegeRepository privilegeRepo;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtProvider;


	public List<User> getAll() {
		return userRepo.findAll();
	}
	
	public LoginResponseDto login(String username, String password) {
		log.info("Attempting to login user, with username: "+ username);
		Optional<User> userOp = userRepo.findByUsername(username);
		if(userOp.isEmpty()) {
			throw new UsernameNotFoundException("unable to find user with username: "+ username);
		}
		User user = userOp.get();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		String token = jwtProvider.createToken(username, user.getRoles());
		return LoginResponseDto.builder()
				.accessToken(token)
				.tokenType(JwtUtils.BEARER)
				.build();
	}

	@Transactional
	public User register(UserDto userDto) {
		log.info("Registering new user with username: "+userDto.getUsername());

		if(userRepo.findByUsername(userDto.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException("Username '"+userDto.getUsername()+"' already exists");
		}
		
		User.UserBuilder user = User.builder();
		
		List<Role> roles = getRoles(userDto);
		
		user.username(userDto.getUsername())
			.password(passwordEncoder.encode(userDto.getPassword()))
			.firstName(userDto.getFirstName())
			.lastName(userDto.getLastName())
			.roles(roles);
		
		return userRepo.save(user.build());
	}
	
	protected List<Role> getRoles(UserDto userDto) {
		List<Role> roles = new ArrayList<>();
	
		List<Role> savedRoles = roleRepo.findAll();
		List<Privilege> savedPrivileges = privilegeRepo.findAll();
		
		List<Role> requestedRoles = userDto.getRoles();
		for (int i = 0; i < requestedRoles.size(); i++) {
			Role requestedRole = requestedRoles.get(i);
			Role role = roleFromDb(savedRoles, requestedRole);
			List<Privilege> requestedPrivileges = role.getPrivileges(); 
			List<Privilege> privileges = new ArrayList<>();
			if(requestedPrivileges != null) {
				for (int x = 0; x < requestedPrivileges.size(); x++) {
					privileges.add(privilegeFromDb(savedPrivileges, requestedPrivileges.get(x)));
				}
			}
			role.setPrivileges(privileges);
			roles.add(role);
		}
		return roles;
	}

	private Role roleFromDb(List<Role> savedRoles, Role role) {
		for (Role savedRole : savedRoles) {
			if(role.getName() == savedRole.getName()) {
				return savedRole;
			}
		}
		return role;
	}

	private Privilege privilegeFromDb(List<Privilege> savedPrivileges, Privilege privilege) {
		for (Privilege savedPrivilege: savedPrivileges) {
			if(privilege.getType() == savedPrivilege.getType()) {
				return savedPrivilege;
			}
		}
		return privilege;
	}
}