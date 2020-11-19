package com.breakoutms.lfs.server.user;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.breakoutms.lfs.server.branch.BranchRepository;
import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.UserAlreadyExistsException;
import com.breakoutms.lfs.server.security.JwtUtils;
import com.breakoutms.lfs.server.user.model.LoginResponse;
import com.breakoutms.lfs.server.user.model.UpdatableBean;
import com.breakoutms.lfs.server.user.model.User;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

	private final UserRepository repo;
	private final BranchRepository branchRepo;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtProvider;

	//TODO: MAKE SURE THAT CACHING WORKS AS INTENDED  - PLUS - add to update method once created
	@Cacheable("users")
	public Optional<User> get(Integer userId){
		return repo.findById(userId);
	}
	
	public Page<User> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	public Page<User> search(Specification<User> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	@Transactional(readOnly = true)
	public LoginResponse login(String username, String password, Integer branchId) {
		log.info("Attempting to login user, with username: "+ username);
		Optional<User> userOp = repo.findByUsername(username);
		if(userOp.isEmpty()) {
			String error = "unable to find user with username: "+ username;
			// log this error here because exception is going to be changed in the @ControllerAdvice
			// for security reasons
			log.error(error);
			throw new UsernameNotFoundException(error);
		}
		User user = userOp.get();
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		String token = jwtProvider.createToken(user, branchId);
		var response = LoginResponse.builder()
				.accessToken(token)
				.tokenType(JwtUtils.BEARER)
				.resetPassword(user.isResetPassword())
				.build();
		
		if(user.getUpdatableBeans() != null) {
			var updatableBeans = user.getUpdatableBeans()
					.stream()
					.map(UpdatableBean::getField)
					.collect(Collectors.toList());
			response.setUpdatableBeans(updatableBeans);
		}
		return response;
	}
	
	@Transactional
	public User register(User user) {
		log.info("Registering new user with username: "+user.getUsername());

		if(repo.findByUsername(user.getUsername()).isPresent()) {
			throw new UserAlreadyExistsException("Username '"+user.getUsername()+"' already exists");
		}
		Branch branch = branchRepo.findByName(user.getBranch().getName())
				.orElseThrow(ExceptionSupplier.notFound("Branch", user.getBranch()));
		user.setBranch(branch);
		String password = user.getPassword();
		user.setPassword(passwordEncoder.encode(password));
		return repo.save(user);
	}

	@Transactional
	public User update(Integer id, User updateUser) {
		Branch branch = branchRepo.findByName(updateUser.getBranch().getName())
				.orElseThrow(ExceptionSupplier.notFound("Branch", updateUser.getBranch()));
		User user = repo.findById(id).orElseThrow(ExceptionSupplier.notFound("User", id));
		
		user.setUsername(updateUser.getUsername());
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		user.setBranch(branch);
		user.setRoles(updateUser.getRoles());
		user.setUpdatableBeans(updateUser.getUpdatableBeans());
		return repo.save(user);
	}

	public User changePassword(Integer id, String oldPassword, String password) {
		User user = repo.findById(id).orElseThrow(ExceptionSupplier.notFound("User", id));
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), oldPassword));
		user.setPassword(passwordEncoder.encode(password));
		user.setResetPassword(false);
		return repo.save(user);
	}
	
	public String createOtp(Integer id) {
		User user = repo.findById(id).orElseThrow(ExceptionSupplier.notFound("User", id));
		String password = generatePassword();
		user.setPassword(passwordEncoder.encode(password));
		user.setResetPassword(true);
		repo.save(user);
		return password;
	}
	
	public String generatePassword() {
		String uuid = UUID.randomUUID().toString();
		int hash = uuid.hashCode();
		if(hash < 0) { // convert a negative number to positive
			hash = hash - (hash * 2);
		}
		return String.valueOf(hash).substring(0, 4);
	}
}