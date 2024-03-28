package org.edupoll.app.controller;

import org.edupoll.app.command.AddComment;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Comment;
import org.edupoll.app.repository.CommentRepository;
import org.edupoll.app.repository.PostRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	
	@PostMapping("/add/comment/api")
	@ResponseBody
	public String proceedAddComment(@RequestBody String testData, @AuthenticationPrincipal(expression = "account") Account account) throws JsonMappingException, JsonProcessingException {
		System.out.println(testData);
		// 가져오는건 성공.. 자료 뽑시는 실패
	
		ObjectMapper objectMapper = new ObjectMapper();
		AddComment addComment = objectMapper.readValue(testData, AddComment.class);
		Long postId = Long.valueOf(addComment.getPostid());
		
		Comment comment = Comment.builder()
								 .account(account)
								 .post(postRepository.findById(postId).get())
								 .commentContent(addComment.getContent()).build();
		
		commentRepository.save(comment);
		
		return "success";
	}
}







