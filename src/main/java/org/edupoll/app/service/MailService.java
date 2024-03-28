package org.edupoll.app.service;

import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final JavaMailSender javaMailSender;
	private final AccountRepository accountRepository;
	
	public void sendTempoPassword(String target, String password) {
		Optional<Account> find = accountRepository.findByEmail(target);
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(target);
		message.setFrom("no-reply@gamil.com");
		message.setSubject("[Thestargram] 임시비밀번호 발송");
		
		message.setText(password);
		
		try {
			javaMailSender.send(message);
			
		}catch (Exception e) {
			log.warn(e.getMessage());
		}
		
		
	}
}
