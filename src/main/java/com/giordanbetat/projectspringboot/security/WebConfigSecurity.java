package com.giordanbetat.projectspringboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementationUserDetailsService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.GET, "/").permitAll()
				.antMatchers(HttpMethod.GET, "/register")
				.hasAnyRole("ADMIN", "COMMON").anyRequest().authenticated().and()
				.formLogin().permitAll()
				.loginPage("/login")
				.defaultSuccessUrl("/register")
				.failureUrl("/login?error=true")
				.and().logout().logoutSuccessUrl("/login")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());

	}

}
