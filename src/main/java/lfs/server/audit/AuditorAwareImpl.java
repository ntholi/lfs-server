package lfs.server.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import lfs.server.user.User;

public class AuditorAwareImpl implements AuditorAware<User>{

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(null);
	}

}
