package com.ps16445.model;


import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto implements Serializable{
	
	private Long orderDetailId;
	
	private String name;
	
	private Long orderId;
	
	private Long productId;
	
	private int quantity;
	
	private String image;
	
	private double unitPrice;
	
	private String description;
	
	@Length(min = 5)
	private String address;
}
