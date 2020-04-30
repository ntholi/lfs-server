package com.breakoutms.lfs.server.user;

import static com.breakoutms.lfs.server.security.JwtUtils.ROLE_PREFIX;
import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.security.JwtUtils;
import com.breakoutms.lfs.server.user.model.Privilege;
import com.breakoutms.lfs.server.user.model.Role;
import com.breakoutms.lfs.server.user.model.User;
import com.breakoutms.lfs.server.user.repo.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
    private final JwtUtils jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userRepository.findByUsername(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username '%s' does not exist", s)));
        
		return withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(getGrantedAuthority(user.getRoles()))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }

	protected List<GrantedAuthority> getGrantedAuthority(List<Role> list) {
		List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : list) {
			authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX+role.getName()));
			for (Privilege privilege : role.getPrivileges()) {
				authorities.add(new SimpleGrantedAuthority(privilege.getType().name()));
			}
		}
		return authorities;
	}

    /**
     * Extract username and roles from a validated jwt string.
     *
     * @param jwtToken jwt string
     * @return UserDetails if valid, Empty otherwise
     */
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(
                withUsername(jwtProvider.getUserId(jwtToken))
                .authorities(jwtProvider.getRoles(jwtToken))
                .password("") //token does not have password but field may not be empty
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build());
        }
        return Optional.empty();
    }
}