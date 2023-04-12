
package com.inn.cafe.Model;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;




@NamedQuery(name="User.findByEmailId", query = "select u from User u where u.email=:email")   // User refers to class name not table name
																				     	// email refers to params that we get from service

@Entity
@Table(name="user")
@DynamicInsert
@DynamicUpdate
@Data
public class User  implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "conatactNumber")
	private String contactNumber;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "role")
	private String role;
	
	
	
	

}
