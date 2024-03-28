package org.edupoll.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.edupoll.app.command.ChatroomForInbox;
import org.edupoll.app.command.LatestMessage;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Chatroom;
import org.edupoll.app.entity.Message;
import org.edupoll.app.entity.Profile;
import org.edupoll.app.repository.AccountRepository;
import org.edupoll.app.repository.ChatroomRepository;
import org.edupoll.app.repository.MessageRepository;
import org.edupoll.app.repository.ProfileRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/direct")
public class DirectController {

	private final ChatroomRepository chatroomRepository;
	private final ProfileRepository profileRepository;
	private final MessageRepository messageRepository;
	private final AccountRepository accountRepository;
	
	@GetMapping("/inbox")
	public String showDirect(@AuthenticationPrincipal(expression = "account") Account account
								,Model model) {
		
		Optional<Profile> optional = profileRepository.findByAccount(account);
		if(!optional.isPresent()) {
			return null;
		}
		Profile profile = optional.get();
		model.addAttribute("profile", profile.getProfileImageUrl());
		model.addAttribute("account", account);
		
		List<Chatroom> rooms = chatroomRepository.findByCreaterOrInvitee(account, account);
		
		List<LatestMessage> latestMessage = new ArrayList<>();

		List<ChatroomForInbox> forIndex = new ArrayList<>();
		rooms.stream().forEach(t -> {
			ChatroomForInbox cmd = new ChatroomForInbox();
			if(t.getCreater().getId().equals(account.getId())) {
				cmd.setChatroom(t);
				cmd.setProfileImageUrl(profileRepository.findByAccount(t.getInvitee()).get().getProfileImageUrl());
				cmd.setUsername(t.getInvitee().getUsername());
				forIndex.add(cmd);
			}else {
				cmd.setChatroom(t);
				cmd.setProfileImageUrl(profileRepository.findByAccount(t.getCreater()).get().getProfileImageUrl());
				cmd.setUsername(t.getCreater().getUsername());
				forIndex.add(cmd);
			}
			// 제일최근 메세지 (해당방의)
			LatestMessage message = new LatestMessage();
			message.setChatroomId(t.getId());
			
			List<Message> a =  messageRepository.findByChatroomIdOrderByIdDesc(t.getId());
			if(a.size() > 0) {
				message.setMessage(a.get(0).getContent());
				message.setSentAt(a.get(0).getSentAt());
				latestMessage.add(message);
			}
			
		});
		if(forIndex != null) {
			model.addAttribute("chatroomList", forIndex);
			
		}
		if(latestMessage != null) {
			model.addAttribute("latestMessage", latestMessage);	
		}	
		return "direct/inbox";
	}
	// 채팅방 내용
	@GetMapping("/{chatroomId}")
	public String showSpecificChatRoom(@PathVariable String chatroomId ,Model model
						,@AuthenticationPrincipal(expression = "account") Account account) {
		Optional<Profile> optional = profileRepository.findByAccount(account);
		if(!optional.isPresent()) {
			return null;
		}
		Profile profile = optional.get();
		model.addAttribute("profile", profile.getProfileImageUrl());	
		model.addAttribute("account", account);
		
		List<Chatroom> rooms = chatroomRepository.findByCreaterOrInvitee(account, account);
		List<LatestMessage> latestMessage = new ArrayList<>();
		List<ChatroomForInbox> forIndex = new ArrayList<>();
		rooms.stream().forEach(t -> {
			ChatroomForInbox cmd = new ChatroomForInbox();
			if(t.getCreater().getId().equals(account.getId())) {
				cmd.setChatroom(t);
				cmd.setProfileImageUrl(profileRepository.findByAccount(t.getInvitee()).get().getProfileImageUrl());
				cmd.setUsername(t.getInvitee().getUsername());
				forIndex.add(cmd);
			}else {
				cmd.setChatroom(t);
				cmd.setProfileImageUrl(profileRepository.findByAccount(t.getCreater()).get().getProfileImageUrl());
				cmd.setUsername(t.getCreater().getUsername());
				forIndex.add(cmd);
			}
			// 제일최근 메세지 (해당방의)
			LatestMessage message = new LatestMessage();
			message.setChatroomId(t.getId());
			
			List<Message> a =  messageRepository.findByChatroomIdOrderByIdDesc(t.getId());
			if(a.size() > 0) {
				message.setMessage(a.get(0).getContent());
				message.setSentAt(a.get(0).getSentAt());
				latestMessage.add(message);
			}
			
		});
		if(forIndex != null) {
			model.addAttribute("chatroomList", forIndex);
			
		}
		if(latestMessage != null) {
			model.addAttribute("latestMessage", latestMessage);	
		}	
		
		Chatroom found = chatroomRepository.findById(Long.valueOf(chatroomId)).get();
		
		// 채팅방 상대방 찾기
		ChatroomForInbox cmd = new ChatroomForInbox();
		cmd.setChatroom(found);
		
		//Optional<Profile> one = profileRepository.findByAccount(found.getInvitee());
		
		
		if(account.getId()==found.getCreater().getId()) {
			
				cmd.setProfileImageUrl(profileRepository.findByAccount(found.getInvitee()).get().getProfileImageUrl());
				cmd.setUsername(found.getInvitee().getUsername());
		
			
		}else {
			
				cmd.setProfileImageUrl(profileRepository.findByAccount(found.getCreater()).get().getProfileImageUrl());
				cmd.setUsername(found.getCreater().getUsername());
			
		}
		model.addAttribute("chatroom", cmd);
		
		// 채팅메세지 찾기
		List<Message> message = messageRepository.findByChatroomIdOrderByIdAsc(Long.valueOf(chatroomId));
		model.addAttribute("message", message);
		
	
		return "direct/chatroom-detail";
	}
	
	// 채팅방 개설 
	@PostMapping("/create")
	public String proceedCreateChatroom(@RequestParam Long accountId, @AuthenticationPrincipal(expression = "account") Account account) {
		Optional<Account> optional =  accountRepository.findById(accountId);
		if(!optional.isPresent()) {
			return null;
		}
		Account invitee = optional.get();
		
		// 이미 채팅방이 존재하는지 여부 체크
		Chatroom chatroom1 = chatroomRepository.findByCreaterAndInvitee(account, invitee);
		Chatroom chatroom2 = chatroomRepository.findByCreaterAndInvitee(invitee, account);
		if(chatroom1 != null || chatroom2 != null) {
			if(chatroom1 != null) {
				return "redirect:/direct/"+chatroom1.getId();	
			}else {
				return "redirect:/direct/"+chatroom2.getId();				
			}
		}
		
		Chatroom chatroom = Chatroom.builder()
								    .creater(account)
								    .invitee(invitee)
								    .build();
		
		chatroomRepository.save(chatroom);
		
		
		return "redirect:/direct/"+chatroom.getId();
	}
	
}
