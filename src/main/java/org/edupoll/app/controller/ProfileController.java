package org.edupoll.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.edupoll.app.command.CommentWithProfile;
import org.edupoll.app.command.PostForProfileCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Comment;
import org.edupoll.app.entity.Follow;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.PostImage;
import org.edupoll.app.entity.Profile;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.CommentRepository;
import org.edupoll.app.repository.FollowRepository;
import org.edupoll.app.repository.PostImageRepository;
import org.edupoll.app.repository.PostRepository;
import org.edupoll.app.repository.ProfileRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProfileController {
	
	private final ProfileRepository profileRepository;
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final AccountRepository accountRepository;
	private final CommentRepository commentRepository;	
	private final FollowRepository followRepository;
	
	// 프로필 메인
	@GetMapping("/{username}")
	public String showProfileMain(@PathVariable String username, Model model,
			@AuthenticationPrincipal(expression = "account") Account myAccount) {
		//내 프로필
		Optional<Account> my = accountRepository.findByUsername(myAccount.getUsername());
		Optional<Profile> myoptional = profileRepository.findByAccount(my.get());
		model.addAttribute("myprofile", myoptional.get().getProfileImageUrl());
		// 내 계정
		model.addAttribute("myaccount", my.get());
		
		//타인 계정 프로필
		Optional<Account> accountOptional = accountRepository.findByUsername(username);
		if(!accountOptional.isPresent()) {
			return "index";
		}
		Account account = accountOptional.get();
		Optional<Profile> optional = profileRepository.findByAccount(account);
		if(optional.isPresent()) {
			Profile profile = optional.get();
			model.addAttribute("profile", profile.getProfileImageUrl());	
			// 타인 계정
			model.addAttribute("account", account);
		}
		// 팔로우 팔로워 숫자
		Long follow = followRepository.countByFollow(account); 
		Long follower = followRepository.countByFollower(account); 
		//System.out.println("팔로우 숫자 -> "+ follow);
		//System.out.println("팔로워 숫자 -> "+ follower);
		model.addAttribute("follow", follow);
		model.addAttribute("follower", follower);

		
		// 해당계정 게시물 가져오기
		List<PostForProfileCommand> posts = new ArrayList<>();
	
		List<Post> postList = postRepository.findByAccountOrderByIdDesc(account);
		if(postList.isEmpty()) {
			model.addAttribute("noPost", true);
			return "accounts/profile";
		}
		
		postList.stream().forEach(t -> {
			List<PostImage> images = postImageRepository.findByPostId(t.getId());
			PostForProfileCommand one = PostForProfileCommand.builder()
														 .post(t)
														 .images(images)
														 .build();
			posts.add(one);
		});
		// 게시물 개수
		Long postCnt =postRepository.countByAccount(account);
		
		// 게시물 comment 가지고 오기
		List<CommentWithProfile> withProfile = new ArrayList<>(); 
		List<Comment> comments = commentRepository.findAllByOrderByIdDesc();
		List<Profile> profiles = profileRepository.findAll();
		
		comments.stream().forEach(t ->{
			profiles.stream().forEach(f ->{
				if(t.getAccount().getUsername().equals(f.getAccount().getUsername())) {
					CommentWithProfile cmd = new CommentWithProfile();
					cmd.setProfiles(f);
					cmd.setComments(t);
					
					withProfile.add(cmd);
				}
			});
			
		});
		
		
		
		model.addAttribute("comments", withProfile); 
		model.addAttribute("postCnt", postCnt);
		model.addAttribute("posts",posts);
		
		return "accounts/profile";
	}
	// 프로필 사진등록
	@PostMapping("/changeProfileImage")
	public String changeProfileImage(@AuthenticationPrincipal(expression = "account") Account account
						,@RequestParam("profileImage") MultipartFile file) throws IllegalStateException, IOException {
		
		MultipartFile profileImage = file;
		File dir = new File("c:\\thestargram\\profileImage\\"+account.getId());
		dir.mkdirs();
		
		if(file == null) {
			return "/"+account.getUsername();
		}
		
		String fileName = UUID.randomUUID().toString();
		File target = new File(dir, fileName);
		profileImage.transferTo(target);
		
		Optional<Profile> one = profileRepository.findByAccount(account);
		if(one.isEmpty()) {
			return "/"+account.getUsername();
		}
		Profile profile = one.get();
		profile.setProfileImageUrl("/static/profileImage/"+account.getId()+"/"+fileName);
		profileRepository.save(profile);
		
		//System.out.println("파일이름 : "+ file.getOriginalFilename());
		//System.out.println("프로필사진 변경 컨트롤러 in");
		return "redirect:/"+account.getUsername();
	}
	// 프로필 사진삭제
	@GetMapping("/deleteProfileImage")
	public String deleteProfileImage(@AuthenticationPrincipal(expression = "account") Account account) {
		
		Optional<Profile> one = profileRepository.findByAccount(account);
		if(one.isEmpty()) {
			return "/"+account.getUsername();
		}
		Profile profile = one.get();
		profile.setProfileImageUrl("/static/기본프로필사진.jpg");
		profileRepository.save(profile);
		
		return "redirect:/"+account.getUsername();
	}
	
	// 팔로워 화면
	@GetMapping("/follower")
	public String showFollowers(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		
		Optional<Profile> optional = profileRepository.findByAccount(account);
		if(!optional.isPresent()) {
			return null;
		}
		
		Profile profile = optional.get();
		model.addAttribute("myprofile", profile.getProfileImageUrl());			
		model.addAttribute("myaccount", account);
		
		
		List<Profile> profiles = profileRepository.findAll3(account.getId());
		model.addAttribute("profiles", profiles);
		
		return "accounts/follower";
	}
	
	// 팔로우 화면
		@GetMapping("/follow")
		public String showFollows(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
			
			Optional<Profile> optional = profileRepository.findByAccount(account);
			if(!optional.isPresent()) {
				return null;
			}
			
			Profile profile = optional.get();
			model.addAttribute("myprofile", profile.getProfileImageUrl());			
			model.addAttribute("myaccount", account);
			
			///////////////////////////////////////////////////////////////////////////////
			List<Profile> profiles = profileRepository.findAll4(account.getId());
			model.addAttribute("profiles", profiles);
						
			return "accounts/follow";
		}
	
}
