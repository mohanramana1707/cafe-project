package com.inn.cafe.ServiceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.inn.cafe.Model.User;
import com.inn.cafe.Service.UserService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl  implements UserService{

	@Autowired
	 UserDao userDao;       // repository
	
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		
		log.info("{Inside signUp {}",requestMap);
		
		try {
		
					
						
					if(validateSignupMap(requestMap)) {   										 // method present in same class
						 
						User user=userDao.findByEmailId(requestMap.get("email"));
						
						if(Objects.isNull(user)) {           		// checking is this mailid already registered,if not save in REPO
							userDao.save(getUserFromMap(requestMap));                 
							return CafeUtils.getResponseEntity("Successfully registered ", HttpStatus.OK);
						}
						else {
							return CafeUtils.getResponseEntity("email already present",HttpStatus.BAD_REQUEST);
						}
						
					}
					else {
						return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
						
					}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	private Boolean validateSignupMap(Map<String,String> requestMap) {
		
		if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("password"))
			{
				return true;
			}
			return false;
	 }
	
	private User getUserFromMap(Map<String,String> map) {          // MAP to USER object
		
		User u= new User();
		u.setName(map.get("name"));
		u.setContactNumber(map.get("contactNumber"));
		u.setEmail(map.get("email"));
		u.setPassword(map.get("password"));
		u.setStatus("false");
		u.setRole("user");
		
		return u;
		
	}
	
	//********************************************************************************************************

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		
		log.info("{Inside login {}");
		
		try {
			
			User u= getUserFromMap(requestMap);         // given map to user
			
			User userForPasswordCheck =userDao.findByEmailId(u.getEmail());   // checking the DB and returning the USER
			
			// getting email id and checking the password and emailid is same.
			
			if( ! Objects.isNull(userDao.findByEmailId(u.getEmail())) && u.getPassword().equals(userForPasswordCheck.getPassword()) ) {
				
				return CafeUtils.getResponseEntity("Email & Password check Done .Successfully Loged IN", HttpStatus.OK);
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.WRONG_CREDENTIALS, HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			
			log.error("{}",e);
			e.printStackTrace();
		}
		
		
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	

}
