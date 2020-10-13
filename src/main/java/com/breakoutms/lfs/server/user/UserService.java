package com.breakoutms.lfs.server.user;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.UserAlreadyExistsException;
import com.breakoutms.lfs.server.security.JwtUtils;
import com.breakoutms.lfs.server.user.model.LoginResponse;
import com.breakoutms.lfs.server.user.model.User;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtProvider;

	//TODO: MAKE SURE THAT CACHING WORKS AS INTENDED  - PLUS - add to update method once created
	@Cacheable("users")
	public Optional<User> get(Integer userId){
		return userRepo.findById(userId);
	}
	
	public Page<User> all(Pageable pageable) {
		return userRepo.findAll(pageable);
	}
	
	public Page<User> search(Specification<User> specs, Pageable pageable) {
        return userRepo.findAll(Specification.where(specs), pageable);
    }
	
	@Transactional(readOnly = true)
	public LoginResponse login(String username, String password) {
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
		var syncNo = user.getBranch().getSyncNumber();
		String token = jwtProvider.createToken(user, syncNo);
		return LoginResponse.builder()
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
		return userRepo.save(user);
	}
}