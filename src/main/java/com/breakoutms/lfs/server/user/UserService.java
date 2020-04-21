package com.breakoutms.lfs.server.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.security.JwtUtils;
import com.breakoutms.lfs.server.user.dto.LoginResponseDto;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtProvider;


	/**
	 * Sign in a user into the application, with JWT-enabled authentication
	 *
	 * @param username  username
	 * @param password  password
	 * @return Optional of the Java Web Token, empty otherwise
	 */
	public LoginResponseDto login(String username, String password) {
		log.info("Attempting to login user, with username: "+ username);
		String token = null;
		Optional<User> userOp = userRepository.findByUsername(username);
		if(userOp.isEmpty()) {
			throw new UsernameNotFoundException("unable to find user with username: "+ username);
		}
		User user = userOp.get();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		token = jwtProvider.createToken(username, user.getRoles());
		return LoginResponseDto.builder()
				.accessToken(token)
				.username(username)
				.build();
	}

	@Transactional
	public User register(User user) {
		log.info("Registering new user with username"+user.getUsername());
		if (!userRepository.findByUsername(user.getUsername()).isPresent()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userRepository.save(user);
		}
		return null; //TODO: throw exception if findByUsername is not in database new HttpServerErrorException(HttpStatus.BAD_REQUEST,"User already exists")
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}
}