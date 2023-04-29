
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


//queryName= className.MethodName
//User refers to class name not table name & email refers to params that we get from service

@NamedQuery(name="User.findByEmailId", query = "select u from User u where u.email=:email") 

// select a details of User whose ROLE is USER from user TABLE ,and CREATING  objects using CONSTRUCTOR while QUERING for {RETURNING UserWrapper Object} 

@NamedQuery(name="User.getAllUser",query = "select new com.inn.cafe.Wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user' ")

// return all admin
@NamedQuery(name="User.getAllAdmin",query = "select new com.inn.cafe.Wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='admin' ")

// return all admin email ids
@NamedQuery(name="User.getAllAdminEmail",query = "select u.email from User u where u.role='admin' ")

// to update the status based on id

@NamedQuery(name="User.updateStatus" , query = "update User u set u.status=:status where u.id=:id")

@Entity
@Table(name="user")
@DynamicInsert
@DynamicUpdate
@Data
public class User  implements Serializable{
	
	private static final long serialVersionUID=1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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
