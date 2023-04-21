package com.inn.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.Model.User;
import com.inn.cafe.Wrapper.UserWrapper;

import jakarta.transaction.Transactional;


public interface UserDao extends JpaRepository<User, Integer> {

	// return a User object  && its  implementation in entity class USER @namedquery
	// email(got from service)  is passed as named parameter to namedquery ("email" and email should be same)
	
	User findByEmailId(@Param("email") String email); 
	
	// to get a list of Users
	List<UserWrapper> getAllUser();
	
	List<UserWrapper> getAllAdmin();
	
	List<String> getAllAdminEmail();
	
	
	
	// to update the status based on id
	@Transactional  // these 2 annotation should be used for updating DB
	@Modifying 
	Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
															
}
