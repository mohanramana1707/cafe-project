package com.inn.cafe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.inn.cafe.Model.User;


public interface UserDao extends JpaRepository<User, Integer> {

	
	User findByEmailId(@Param("email") String email);       // return a User object  && its  implementation in entity class USER @namedquery
															// email(got from service)  is passed as named parameter to namedquery ("email" and email should be same)
}