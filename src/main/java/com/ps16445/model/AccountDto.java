package com.ps16445.model;

import java.io.Serializable;

import javax.validation.constraints.DecimalMin;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable{
	
	private Long userId;
	
	@Length(min = 5)
	private String username;
	
	@Length(min = 5)
	private String password;
	
	private String confirmPassword;
	
	@Length(min = 5)
	private String fullname;
	
	private boolean gender=true;
	
	@Length(min = 8)
	private String email;
	
	private String image = "user.jpg";
	
	private MultipartFile imageFile;
	
	@Length(min = 10)
	private String phone;
	
	private boolean activated = true;
	
	private boolean admin = false;
	
	private Boolean checkEdit=false;
	
}
