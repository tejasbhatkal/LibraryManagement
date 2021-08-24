package com.mcbirdtechnologies.LibManagement.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mcbirdtechnologies.LibManagement.Constants.LibManagementURL;
import com.mcbirdtechnologies.LibManagement.Filters.ValidationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ROLE_ADMIN = "ADMIN";
	private static final String ROLE_USER = "USER";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private ValidationFilter validationFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService);

	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {

		return super.authenticationManagerBean();

	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
				.antMatchers(LibManagementURL.USER_AVAILABLE_BOOKS, LibManagementURL.USER_ISSUED_BOOKS)
				.hasRole(ROLE_USER)
				.antMatchers(LibManagementURL.ADMIN_ADD_BOOKS, LibManagementURL.ADMIN_REMOVE_BOOK,
						LibManagementURL.ADMIN_VIEW_BOOKS)
				.hasRole(ROLE_ADMIN).antMatchers("/authenticate").permitAll().anyRequest().authenticated().and()
				.exceptionHandling().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(validationFilter, UsernamePasswordAuthenticationFilter.class);

	}

}
