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
import com.breakoutms.lfs.server.user.model.LoginResponseDto;
import com.breakoutms.lfs.server.user.model.Privilege;
import com.breakoutms.lfs.server.user.model.Role;
import com.breakoutms.lfs.server.user.model.User;
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
			String error = "unable to find user with username: "+ username;
			// log this error here because exception is going to be changed in the @ControllerAdvice
			// for security reasons
			log.error(error);
			throw new UsernameNotFoundException(error);
		}
		User user = userOp.get();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		String token = jwtProvider.createToken(user);
		return LoginResponseDto.builder()
				.accessToken(token)
				.tokenType(JwtUtils.BEARER)
				.build();
	}

	@Transactional
	public User register(User user) {
		log.info("Registering new user with username: "+user.getUsername());

		if(userRepo.findByUsername(user.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException("Username '"+user.getUsername()+"' already exists");
		}
		String password = user.getPassword();
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles(getRoles(user));
		
		return userRepo.save(user);
	}
	
	protected List<Role> getRoles(User user) {
		List<Role> roles = new ArrayList<>();
	
		List<Role> savedRoles = roleRepo.findAll();
		List<Privilege> savedPrivileges = privilegeRepo.findAll();
		
		List<Role> requestedRoles = user.getRoles();
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