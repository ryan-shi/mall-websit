package com.ryan.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.ryan.security.CustomUserDetailsService;
import com.ryan.security.LoginSuccessHandler;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Resource(name = "dataSource")
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").permitAll().successHandler(loginSuccessHandler())
				.failureUrl("/login?error").and().authorizeRequests()
				.antMatchers("/img/**", "/js/**", "/css/**", "/fonts/**","/").permitAll().anyRequest().authenticated().and()
				.logout().logoutSuccessUrl("/").and().exceptionHandling().accessDeniedPage("/deny").and().rememberMe()
				.tokenValiditySeconds(86400).tokenRepository(tokenRepository()).and().sessionManagement()
				.maximumSessions(1).expiredUrl("/login?error=expired");
	}

	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}

	@Bean
	public JdbcTokenRepositoryImpl tokenRepository() {
		JdbcTokenRepositoryImpl jtr = new JdbcTokenRepositoryImpl();
		jtr.setDataSource(dataSource);
		return jtr;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
