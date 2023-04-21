package com.inn.cafe.RestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.Rest.UserRest;
import com.inn.cafe.Service.UserService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.Wrapper.UserWrapper;
import com.inn.cafe.constents.CafeConstants;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class UserRestImpl implements UserRest {
	
	@Autowired
	UserService userService;

	
	
	public ResponseEntity<String> signup(Map<String, String> requestMap) {
		
		try {
			return userService.signUp(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		try {
			
			return userService.login(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			log.warn("NO error in REST controller");
			return userService.getAllUser();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.warn("error in REST controller");
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}



	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			
			return userService.update(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	
}
