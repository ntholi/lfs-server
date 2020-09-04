package com.breakoutms.lfs.server.audit;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.breakoutms.lfs.server.user.UserService;
import com.breakoutms.lfs.server.user.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuditorAwareImpl implements AuditorAware<User>{

	private final UserService userService;
	
	@Override
	public Optional<User> getCurrentAuditor() {
		SecurityContext context = SecurityContextHolder.getContext();
		if(context != null && context.getAuthentication() != null && context.getAuthentication().getPrincipal() != null) {
			Object principal = context.getAuthentication().getPrincipal();
			if (principal instanceof UserDetails) {
				String userId = ((UserDetails)principal).getUsername();
				if(StringUtils.isNumeric(userId)) {
					return userService.get(Integer.valueOf(userId));
				}
			}
		}

		return Optional.empty();
	}

}
