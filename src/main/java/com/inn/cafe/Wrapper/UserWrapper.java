package com.inn.cafe.Wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

// to avoid exposing the password
public class UserWrapper {
	
	public UserWrapper(Integer id, String name, String email, String contactNumber, String status) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.contactNumber = contactNumber;
		this.status = status;
	}
	private Integer id;
	private String name;
	private String email;
	private String contactNumber;
	private String status;
	
	

}
