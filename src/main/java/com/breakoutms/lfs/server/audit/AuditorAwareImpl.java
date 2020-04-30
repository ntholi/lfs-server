package com.breakoutms.lfs.server.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuditorAwareImpl implements AuditorAware<Integer>{

	@Override
	public Optional<Integer> getCurrentAuditor() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String userId = ((UserDetails)principal).getUsername();
			return Optional.of(Integer.valueOf(userId));
		}
		return Optional.empty();
	}

}
