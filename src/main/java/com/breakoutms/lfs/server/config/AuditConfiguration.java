package com.breakoutms.lfs.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.breakoutms.lfs.server.audit.AuditorAwareImpl;
import com.breakoutms.lfs.server.user.UserService;
import com.breakoutms.lfs.server.user.model.User;

import lombok.AllArgsConstructor;

@Configuration
@EnableJpaAuditing
@AllArgsConstructor
public class AuditConfiguration {

	private final UserService userService;
	
    @Bean
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl(userService);
    }
}