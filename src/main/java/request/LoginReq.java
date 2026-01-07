package request;

import jakarta.validation.constraints.NotBlank;

public class LoginReq {
	
	@NotBlank(message = "電子郵件不能為空")
	private String email;
	
	@NotBlank(message = "密碼不能為空")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
