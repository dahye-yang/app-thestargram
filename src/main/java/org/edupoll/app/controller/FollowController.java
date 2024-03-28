package org.edupoll.app.controller;

import java.util.Optional;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Follow;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.FollowRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FollowController {

	private final AccountRepository accountRepository;
	private final FollowRepository followRepository;

	
	@GetMapping("/add/follow")
	public String proceedAddFollow(@RequestParam Long accountId, @AuthenticationPrincipal(expression = "account") Account account) {
		//System.out.println("팔로우 컨트롤러 in");
		//System.out.println("accountId --> "+accountId);
		Optional<Account> optional = accountRepository.findById(accountId);
		if(optional.isPresent()) {
			Account one = optional.get();
			Follow follow = Follow.builder()
						  .follow(one)
						  .follower(account)
						  .build();
			
			followRepository.save(follow);
		}
		
		return "redirect:/index";
	}
	@Transactional
	@GetMapping("/delete/follow")
	public String proceedDeleteFollow(@RequestParam Long followId, @AuthenticationPrincipal(expression = "account") Account account) {
		//System.out.println("팔로우삭제 컨트롤러 in");
		//System.out.println("accountId --> "+accountId);

		Optional<Account> optional = accountRepository.findById(followId);
		if(optional.isPresent()) {
			Account follow = optional.get();
			followRepository.deleteByFollowAndFollower(follow.getId(), account.getId());
		}
		
		return "redirect:/follower";
	}
}
