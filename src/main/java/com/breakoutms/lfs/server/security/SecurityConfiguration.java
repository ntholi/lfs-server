package com.breakoutms.lfs.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.breakoutms.lfs.server.user.RoleName;
import com.breakoutms.lfs.server.user.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/mortuary/**").hasAnyRole(RoleName.MORTUARY.name(), RoleName.ADMIN.name())
                .antMatchers("/undertaker/**").hasAnyRole(RoleName.UNDERTAKER.name(), RoleName.ADMIN.name())
                .antMatchers("/preneed/**").hasAnyRole(RoleName.PRENEED.name(), RoleName.ADMIN.name())
                .antMatchers("/sales/**").hasAnyRole(RoleName.SALES.name(), RoleName.ADMIN.name())
                .antMatchers("/revenue/**").hasAnyRole(RoleName.REVENUE.name(), RoleName.ADMIN.name())
                .antMatchers("/**").hasRole(RoleName.ADMIN.name())
                .anyRequest().authenticated();

        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new JwtTokenFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);
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