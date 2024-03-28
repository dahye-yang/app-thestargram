package org.edupoll.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.edupoll.app.command.AddPost;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.PostImage;
import org.edupoll.app.entity.PostLike;
import org.edupoll.app.repository.PostImageRepository;
import org.edupoll.app.repository.PostLikeRepository;
import org.edupoll.app.repository.PostRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {
	
	private final PostRepository postRepository;
	private final PostImageRepository postImageRepository;
	private final PostLikeRepository postLikeRepository;
	
	@PostMapping("/add/post")
	public String proceedAddPost(@ModelAttribute AddPost addPost, 
				@AuthenticationPrincipal(expression = "account") Account account) throws IllegalStateException, IOException {
		//System.out.println(addPost.getImages().length);
		
		Post post = Post.builder().content(addPost.getContent()).account(account).build();
		postRepository.save(post);
		
		MultipartFile[] images = addPost.getImages();	
		File dir = new File("c:\\thestargram\\postImage\\"+post.getId());
		dir.mkdirs();
		
		for(MultipartFile image : images) {
			if (image.isEmpty()) {
				continue;
			}
			String fileName = UUID.randomUUID().toString();
			File target = new File(dir, fileName);
			image.transferTo(target);
			
			PostImage postImage = PostImage.builder()
										   .postImageUrl("/postImage/" + post.getId() + "/" + fileName)
										   .post(post)
										   .build();
			
			postImageRepository.save(postImage);
		}
		
		return "redirect:/"+account.getUsername();
	}
	
	// 게시물 좋아요 관리
	// Post로 Ajax해보쟈...
	@PostMapping("/add/like")
	public String proceedAddPostLike(@AuthenticationPrincipal(expression = "account") Account account,
										@RequestParam String postId) {
		System.out.println("라이크 변동 컨트롤러 in");
		System.out.println("post 아이디 -->" + postId);
		Long postLongId = Long.valueOf(postId);
		Post post = postRepository.findById(postLongId).get();
		Optional<PostLike> postLike = postLikeRepository.findByAccount(account);
		if(!postLike.isEmpty()) {
			postLikeRepository.delete(postLike.get());
		}else {
			PostLike one = PostLike.builder().account(account).post(post).build();
			postLikeRepository.save(one);
		}
		
		///////
	
		
		
		return "redirect:/index";
	}
	
//	@PostMapping("/add/like/api")
//	@ResponseBody
//	public String addPostLike(@AuthenticationPrincipal(expression = "account") Account account,
//										@RequestParam String postId) {
//		
//		
//		
//		return "redirect:/index";
//	}
}
