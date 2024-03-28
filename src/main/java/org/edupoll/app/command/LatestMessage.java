package org.edupoll.app.command;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LatestMessage {
	
	private String message;
	private Long chatroomId;
	private LocalDateTime sentAt;
}
