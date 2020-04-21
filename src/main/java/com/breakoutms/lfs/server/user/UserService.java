package com.breakoutms.lfs.server.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.security.JwtUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtProvider;


	/**
	 * Sign in a user into the application, with JWT-enabled authentication
	 *
	 * @param username  username
	 * @param password  password
	 * @return Optional of the Java Web Token, empty otherwise
	 */
	public String login(String username, String password) {
		log.info("Attempting to login user, with username: "+ username);
		String token = null;
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			token = jwtProvider.createToken(username, user.get().getRoles());
		}
		return token;
	}

	/**
	 * Create a new user in the database.
	 *
	 * @param username username
	 * @param password password
	 * @param firstName first name
	 * @param lastName last name
	 * @return Optional of user, empty if the user already exists.
	 */
	public User register(String username, String password, String firstName, String lastName) {
		log.info("Registering new user with username"+username);
		if (!userRepository.findByUsername(username).isPresent()) {
			Optional<Role> role = roleRepository.findByRoleName("ROLE_USER");


			User.UserBuilder sb = User.builder();

			return userRepository.save(sb.username(username)
					.password(passwordEncoder.encode(password))
					.firstName(firstName)
					.lastName(lastName)
					.roles(Arrays.asList(role.get())).build());
		}
		return null; //TODO: throw exception if findByUsername is not in database new HttpServerErrorException(HttpStatus.BAD_REQUEST,"User already exists")
	}

	public List<User> getAll() {
		return userRepository.findAll();
	}
}