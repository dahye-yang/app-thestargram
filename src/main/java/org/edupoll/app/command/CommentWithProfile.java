package org.edupoll.app.command;

import org.edupoll.app.entity.Comment;
import org.edupoll.app.entity.Profile;

import lombok.Data;

@Data
public class CommentWithProfile {
	
	private Comment comments;
	private Profile profiles;
}
