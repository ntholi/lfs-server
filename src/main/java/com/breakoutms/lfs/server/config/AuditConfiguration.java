package com.breakoutms.lfs.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.breakoutms.lfs.server.audit.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new AuditorAwareImpl();
    }
}