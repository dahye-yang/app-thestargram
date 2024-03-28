package org.edupoll.app.common;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	//private final AccountRepository accountRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		String redirectUrl = request.getParameter("redirect_url");
		if(redirectUrl != null) {
			//Account account = accountRepository.findByUsername(authentication.getName()).get();
			response.sendRedirect(redirectUrl);
		}else {
			response.sendRedirect("/index");
			//super.onAuthenticationSuccess(request, response, authentication);	
		}
		
		
	}
	
}
