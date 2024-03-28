package org.edupoll.app.service;

import java.util.Optional;
import java.util.UUID;

import org.edupoll.app.command.NewAccount;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Profile;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.ProfileRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;
	private final ProfileRepository profileRepository;
	
	@Transactional
	public boolean createAccount(NewAccount cmd) {
		
		Optional<Account> found = accountRepository.findByUsername(cmd.getUsername());
		
		if(found.isPresent()) {
			return false;
		}
		
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		
		Account account = Account.builder()
								 .username(cmd.getUsername())
								 .password("{bcrypt}"+bcrypt.encode(cmd.getPassword()))
								 .name(cmd.getName())
								 .email(cmd.getEmail())
								 .build();
		
		Profile profile = Profile.builder()
								 .account(account)
								 .profileImageUrl("/static/기본프로필사진.jpg")
								 .build();
		
		accountRepository.save(account);
		profileRepository.save(profile);
		return true;
	}
	
	public String sendTemporalPassword(String email) {
		
		if(accountRepository.findByEmail(email).isEmpty()) {
			return "error";
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String tempoPassword = UUID.randomUUID().toString().split("-")[0];
		
		Account found = accountRepository.findByEmail(email).get();
		found.setPassword("{bcrypt}"+ encoder.encode(tempoPassword));
		
		accountRepository.save(found);
		return tempoPassword;
	
	}
}
