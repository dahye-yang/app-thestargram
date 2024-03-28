package org.edupoll.app.controller.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.edupoll.app.command.AddMessage;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Chatroom;
import org.edupoll.app.entity.Message;
import org.edupoll.app.repository.ChatroomRepository;
import org.edupoll.app.repository.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat/api")
@RequiredArgsConstructor
public class ChatApiController {

	private final MessageRepository messageRepository;
	private final ChatroomRepository chatroomRepository;
	private final SimpMessagingTemplate template;

	// 메세지 등록 후 ajax로 전송
	@PostMapping("/{roomId}")
	public Map<?, ?> handleAddMessage(@PathVariable String roomId, AddMessage cmd,
			@AuthenticationPrincipal(expression = "account") Account account) {

		Chatroom room = chatroomRepository.findById(Long.valueOf(roomId)).get();
		// System.out.println("====> "+room.getTitle());

		Message message = Message.builder()//
								 .account(account)//
								 .chatroom(room)//
								 .content(cmd.getBody())//
								 .build();//

		messageRepository.save(message);
		// ajax 응답용 메세지
		var map = new LinkedHashMap<>();
		map.put("result", true);

		var payload = new LinkedHashMap<>();
		payload.put("type", "newMessage");
		payload.put("data", message);
		// 구독한 채널로 보내는고양....
		template.convertAndSend("/chat/" + roomId, payload);

		return map;

	}
}
