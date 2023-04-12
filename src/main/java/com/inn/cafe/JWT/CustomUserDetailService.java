package com.inn.cafe.JWT;


import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inn.cafe.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	UserDao userDao;   // repository
	
	private com.inn.cafe.Model.User userDetail;  // model(or)POJO  not the "org.springframework.security.core.userdetails.User;" 

	
	@Override // TODO Auto-generated method stub
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		log.info("Inside loadUserByUsername() "+username);
		
		userDetail=userDao.findByEmailId(username);   // check the user by email in DB and return the User
		
		if(!Objects.isNull(userDetail)){
			
			return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList());    // not our Model User
		}
		else {
			throw new UsernameNotFoundException("User Not found");
		}
	}
	
	// to return the User
	public com.inn.cafe.Model.User getUserDetail(){
		return userDetail;
	}

}
