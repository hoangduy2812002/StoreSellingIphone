package com.ps16445.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	
	@Column(columnDefinition = "nvarchar(150) not null")
	private String name;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false)
	private double unitPrice;
	
	@Column(nullable = false)
	Boolean available;
	
	@Column(length = 255)
	private String image;
	
	@Column(columnDefinition = "nvarchar(255) not null")
	private String description;
	
	
	@Temporal(TemporalType.DATE)
	private Date enteredDate;
	
	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
	private Set<OrderDetail> orderDetails;
}
