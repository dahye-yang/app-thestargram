package org.edupoll.app.command;

import java.util.List;

import org.edupoll.app.entity.Post;
import org.edupoll.app.entity.PostImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostForProfileCommand {
	
	private Post post;
	
	private List<PostImage> images;

}
