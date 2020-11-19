package com.breakoutms.lfs.server.security;

import static com.breakoutms.lfs.common.enums.Domain.ADMIN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        		.antMatchers("/startup-data/**").permitAll()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/users/*/change-password").authenticated();
        for(Domain domain: Domain.values()) {
        	authorize(http, domain);
        }
        http.authorizeRequests()
        	.antMatchers("/**")
        	.hasRole(ADMIN.name())
        	.anyRequest().authenticated();


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(new JwtTokenFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);
    }
    
    private void authorize(HttpSecurity http, Domain domain) throws Exception {
    	http.authorizeRequests()
    		.antMatchers(HttpMethod.GET, domain.antPattern()).access(domain.canRead())   
    		.antMatchers(HttpMethod.POST, domain.antPattern()).access(domain.canWrite())
    		.antMatchers(HttpMethod.PUT, domain.antPattern()).access(domain.canUpdate())
    		.antMatchers(HttpMethod.DELETE, domain.antPattern()).access(domain.canDelete());
	}

	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}