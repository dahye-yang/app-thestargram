package org.edupoll.app.config;

import org.edupoll.app.common.CustomAuthenticationSuccessHandler;
import org.edupoll.app.common.CustomUserDetailsService;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,
					CustomAuthenticationSuccessHandler customHandler) throws Exception {
		http.csrf(r -> r.disable());
		http.authorizeHttpRequests(t ->
			t.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
			.requestMatchers("/static/**").permitAll()
			.requestMatchers("/accounts/**").permitAll()
			.anyRequest().authenticated()
				);
		
		http.anonymous(t -> t.disable());
	
		// 로그인
		http.formLogin(t-> t.loginPage("/accounts/signin").permitAll()
					// 로그인 성공 시에 (successHandler)		
					.successHandler(customHandler)
					// 로그인 실피 시에 (failuerHandler)
					.failureHandler((request, response, exception)-> {
						String username = request.getParameter("username");
						response.sendRedirect("/accounts/signin?error&username="+username);
					})
				
				);
			
		// 로그아웃
		http.logout(logout -> logout.logoutSuccessUrl("/accounts/signin").permitAll());
		
		return http.build();
	}
	
	@Bean
	public UserDetailsService jpaUsers(AccountRepository accountRepository) {
		return new CustomUserDetailsService(accountRepository);
	}
}
