package org.edupoll.app.controller;

import org.edupoll.app.command.NewAccount;
import org.edupoll.app.entity.Account;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.service.AccountService;
import org.edupoll.app.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
	
	private final AccountService accountService;
	private final AccountRepository accountRepository;
	private final MailService mailSevice;
	
	
	// 회원가입
	@GetMapping("/signup")
	public String registAccount(Model model) {
		Account dummy = Account.builder().build();
		model.addAttribute("newAccount", dummy);
		return "accounts/signup";
	}
	
	@PostMapping("/signup")
	public String proceedSignup(@ModelAttribute @Valid NewAccount cmd, 
									BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			
			return "accounts/signup";
		}
		
		boolean signupResult =accountService.createAccount(cmd);
		
		// 회원가입 실패시
		if(!signupResult) {
			return "redirect:/accounts/signup";
		}
		
		return "redirect:/accounts/signin";
	}
	
	// 로그인

	@GetMapping("/signin")
	public String showSignin(@RequestParam(required = false) String username,
			 					@RequestParam(required = false) String redirect_url, Model model) {
		
		return "accounts/signin";
			
	}
	
	// 임시비밀빈호 전송
	@GetMapping("/password/reset")
	public String resetPasswordForm(@RequestParam(required = false) String email) {
		
		return "accounts/password/reset";
	}
	
	@PostMapping("/password/reset")
	public String proceedSendTemporalPassword(@RequestParam String email, Model model) {
		
		String newPassword = accountService.sendTemporalPassword(email);
		
		if(newPassword.equals("error")) {
			return "redirect:/accounts/password/reset?error";
		}
		
		Account found = accountRepository.findByEmail(email).get();
		mailSevice.sendTempoPassword(email, newPassword);
		model.addAttribute("username", found.getUsername());
		
		return "redirect:/accounts/signin";
	}
	
}
