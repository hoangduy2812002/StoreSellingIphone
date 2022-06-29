package com.ps16445.model;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {

	@Length(min = 5)
	private Long orderId;

	private Date enteredDate;

	private String username;
}
