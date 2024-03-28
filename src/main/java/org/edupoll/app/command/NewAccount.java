package org.edupoll.app.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewAccount {
	
	@NotBlank(message = "사용자 이름은 반드시 입력해야 합니다.")
	private String username;
	
	@Size(min = 4, max = 12, message = "비밀번호는 4자 이상 12자 이하로 반드시 입력해야 합니다.")
	private String password;
	
	@Size(min = 1, max = 15, message = "성명은 1자 이상 15자 이하로 반드시 입력해야 합니다.")
	private String name;
	
	@Email
	@NotBlank(message = "이메일 주소는 반드시 입력해야 합니다.")
	private String email;
}
