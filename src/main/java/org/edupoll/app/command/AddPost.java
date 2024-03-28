package org.edupoll.app.command;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddPost {
	
	private MultipartFile[] images;
	private String content;
}
