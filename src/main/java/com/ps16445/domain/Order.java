package com.ps16445.domain;

import java.io.Serializable;
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
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@Temporal(TemporalType.DATE)
	private Date enteredDate;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private Account account;
	
	@Column(nullable = false)
	private double total;
	
	@Column(nullable = false)
	private int quantity;
	
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
	private Set<OrderDetail> orderDetails;
}
