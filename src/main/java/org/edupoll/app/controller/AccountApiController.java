package org.edupoll.app.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts/api")
public class AccountApiController {
	
	private final AccountRepository accountRepository;
	
	@GetMapping("/email/{email}")
	@ResponseBody
	public Map<String, Object> checkEmail(@PathVariable String email){
		
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<Account> found = accountRepository.findByEmail(email);
		
		if(found.isPresent()) {
			response.put("result", false);
			return response;
		}
		
		response.put("result", true);
		
		return response;
	}
	
	@GetMapping("/username/{username}")
	@ResponseBody
	public Map<String, Object> checkUsername(@PathVariable String username){
		
		Map<String, Object> response = new LinkedHashMap<>();
		Optional<Account> found = accountRepository.findByUsername(username);
		
		if(found.isPresent()) {
			response.put("result", false);
			return response;
		}
		
		response.put("result", true);
		
		return response;
	}
}
