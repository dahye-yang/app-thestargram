package org.edupoll.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.edupoll.app.command.CommentWithProfile;
import org.edupoll.app.command.PostForIndexCommand;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Comment;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.PostImage;
import org.edupoll.app.entity.PostLike;
import org.edupoll.app.entity.Profile;
import org.edupoll.app.repository.CommentRepository;
import org.edupoll.app.repository.PostImageRepository;
import org.edupoll.app.repository.PostLikeRepository;
import org.edupoll.app.repository.PostRepository;
import org.edupoll.app.repository.ProfileRepository;
import org.edupoll.app.service.RandomAccountService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class WelcomeController {
	
	private final ProfileRepository profileRepository;
	private final RandomAccountService randomAccountService;
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final PostLikeRepository postLikeRepository;
	private final CommentRepository commentRepository;
	
	
	@GetMapping({"/","/index"})
	public String showIndex(@AuthenticationPrincipal(expression = "account") Account account
								, Model model) {
		Optional<Profile> optional = profileRepository.findByAccount(account);
		if(!optional.isPresent()) {
			return null;
		}
		
		Profile profile = optional.get();
		model.addAttribute("myprofile", profile.getProfileImageUrl());			
		model.addAttribute("myaccount", account);
		
		// 우측 상단 랜덤 친구추천
		List<Profile> profiles = randomAccountService.randomAccountAndProfile(account.getId());
		model.addAttribute("profiles", profiles);
		
		// 게시물 랜덤
		List<PostForIndexCommand> posts = new ArrayList<>();
		List<Post> postList = postRepository.findAllByOrderByIdDesc(account.getId());
		
		
		if(postList.isEmpty()) {
			model.addAttribute("noPost", true);
			return "index";
		}
			
		postList.stream().forEach(t -> {
			List<PostImage> images = postImageRepository.findByPostId(t.getId());
			
			PostForIndexCommand one = PostForIndexCommand.builder()
														 .post(t)
														 .images(images)
														 .profile(profileRepository.findByAccount(t.getAccount()).get())
														 .build();
			posts.add(one);
		});
		model.addAttribute("posts", posts);
		// 내가찜한 게시물 List
		List<PostLike> likes = postLikeRepository.findAllByAccountOrderByIdDesc(account);
		//System.out.println("웰컴컨트롤러에서 받아온 게시물갯수--> "+posts.size());
		List<Long> idid = new ArrayList<>();
		for(PostLike one : likes) {
			//System.out.println("postLikeId :: "+one.getId());
			idid.add(one.getId());
		}
		model.addAttribute("like", idid);
		
		// 게시물 comment 가지고 오기
			List<CommentWithProfile> withProfile = new ArrayList<>(); 
			List<Comment> comments = commentRepository.findAllByOrderByIdDesc();
			List<Profile> profiles2 = profileRepository.findAll();
			
			comments.stream().forEach(t ->{
				profiles2.stream().forEach(f ->{
					if(t.getAccount().getUsername().equals(f.getAccount().getUsername())) {
						
						CommentWithProfile cmd = new CommentWithProfile();
						cmd.setProfiles(f);
						cmd.setComments(t);
						
						withProfile.add(cmd);
					}
				});
				
			});
			model.addAttribute("comments", withProfile); 
		
		
		return "index";
	}
	
	
	@GetMapping("/people")
	public String showPeople(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		Optional<Profile> optional = profileRepository.findByAccount(account);
		if(!optional.isPresent()) {
			return null;
		}
		
		Profile profile = optional.get();
		model.addAttribute("myprofile", profile.getProfileImageUrl());			
		model.addAttribute("myaccount", account);
		
		
		List<Profile> profiles = profileRepository.findAll2(account.getId());		
		model.addAttribute("profiles", profiles);
		return "/accounts/people";
	}
}
