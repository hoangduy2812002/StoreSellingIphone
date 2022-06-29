package com.ps16445.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable{
	
	private Long productId;

	@Length(min = 5)
	private String name;

	@DecimalMin("1")
	private int quantity;

	@DecimalMin("1")
	private double unitPrice;

	
	private String image="download.png";
	
	private MultipartFile imageFile;

	@Length(min = 10)
	private String description;

	
	private Date enteredDate;
	
	private Long categoryId;
	
	private boolean checkEdit = false;
	
//	private double min;
//	
//	private double max;
}
