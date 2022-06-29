package com.ps16445.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@Column(name="username", length = 50, columnDefinition = "varchar(50) not  null UNIQUE")
	private String username;
	
	@Column(name="password", length = 50, columnDefinition = "varchar(50) not  null")
	private String password;

	@Column(name="fullname", length = 150, columnDefinition = "nvarchar(150) not  null")
	private String fullname;
	
	@Column(name="gender", columnDefinition = "bit not  null")
	private boolean gender=true;

	@Column(name="email", length = 150, columnDefinition = "varchar(150) not  null")
	private String email;
	
	@Column(name="phone", length = 15, columnDefinition = "varchar(15) not  null")
	private String phone;
	
	@Column(name="image", length = 150, columnDefinition = "varchar(150) not  null")
	private String image;

	@Column(name="activated", columnDefinition = "bit not  null")
	private boolean activated = true;

	@Column(name="admin", columnDefinition = "bit not  null")
	private boolean admin = false;
	
	@OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
	private Set<Order> orders;
}
