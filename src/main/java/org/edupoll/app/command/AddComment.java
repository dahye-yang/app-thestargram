package org.edupoll.app.command;

import lombok.Data;

@Data
public class AddComment {
	
	private String postid;
	private String content;
	private String username;
}
