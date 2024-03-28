package org.edupoll.app.command;

import org.edupoll.app.entity.Chatroom;

import lombok.Data;

@Data
public class ChatroomForInbox {
	
	private Chatroom chatroom;
	private String profileImageUrl;
	private String username;
}
