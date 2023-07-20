package com.smart.smartcontactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//WebSecurityConfigurerAdapter is depricated instead of this u can use security filter chain link is below
//https://codejava.net/frameworks/spring-boot/fix-websecurityconfigureradapter-deprecated
public class SecurityConfiguration {

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

//	@Bean
	public AuthenticationManagerBuilder authenticationManagerBuilder() throws Exception {
		return authenticationManagerBuilder().authenticationProvider(authenticationProvider());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

		security.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/user/**")
				.hasRole("USER").requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();
// --> you can use login failure url after the login page so that if error occur this will redirec to error page		

		return security.build();
	}

}
