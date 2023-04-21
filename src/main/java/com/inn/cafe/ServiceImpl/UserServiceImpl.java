package com.inn.cafe.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.CustomUserDetailService;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.JWT.JwtUtil;
import com.inn.cafe.Model.User;
import com.inn.cafe.Service.UserService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.Utility.EmailUtils;
import com.inn.cafe.Wrapper.UserWrapper;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl  implements UserService{

	@Autowired
	 UserDao userDao;       // repository
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	CustomUserDetailService customUserDetails;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Autowired
	EmailUtils emailUtils;
	
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
	
//**************************************************************************************************************************************************************************************************************

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		
		log.info("{Inside login {} service impl");
		
		try {
			
			// for doing authentication(create authentication object and pass credentials) ,if authenticated, securitycontext will be set automatically (principal)
			org.springframework.security.core.Authentication  auth=authManager.
					authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
			
			log.info("credentials Authenticated ");
			
			
			// check if token is authenticated
			if(auth.isAuthenticated()) {
				
				// check if user is approved by admin, then generate token while logging in with credentials
				if(customUserDetails.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					
					return new ResponseEntity<String>("{\"token\":\""+
							jwtUtil.generateToken(customUserDetails.getUserDetail().getEmail(),
							customUserDetails.getUserDetail().getRole()) +"\"}",HttpStatus.OK);
				}
				else {
					return new ResponseEntity<String>("{\"message\":\"" + "wait for admin approval" + "\"}",HttpStatus.BAD_REQUEST);
					
				}
			}
						
		} catch (Exception e) {
			
			log.error("{}",e);
			e.printStackTrace();
		}
		
		
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

//**************************************************************************************************************************************************************************************************************
	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			
			if(jwtFilter.isAdmin()) { // only admin can see all the users
				
				log.warn("inside getALLUSER --ADMIN");
				
				//return new ResponseEntity<List<UserWrapper>>(userDao.getAllUser(), HttpStatus.OK);
				List<UserWrapper> u= userDao.getAllUser();
				log.warn("inside getALLUSER --USER:"+u);
				
				return new ResponseEntity<List<UserWrapper>>(u, HttpStatus.OK);
			}
			else {  // role=users
				log.warn("inside getALLUSER --ADMIN");
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED); // some probs with status
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.warn("inside getALLUSER --outside if");
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
	}
	
//**************************************************************************************************************************************************************************************************************
	// update the status of the user by the request body and
	// SEND Mail to CURRENT USER with CC of all the ADMINS about the update.
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		
		try {
			// should be a admin(loged in one)
			
			if(jwtFilter.isAdmin()) {
				
				// first checking if user exixts already
				Optional<User> optionalUser= userDao.findById(Integer.parseInt(requestMap.get("id")) );
				
				if(!optionalUser.isEmpty()) {  //exists
					
					sendMailtoAllAdmin(requestMap.get("status"),optionalUser.get().getEmail(),userDao.getAllAdminEmail());
					
					userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")) );
					return CafeUtils.getResponseEntity("User updated Seccessfully", HttpStatus.OK);
				}
				else { // not exists
					CafeUtils.getResponseEntity("User id doesnt Exists", HttpStatus.OK);
				}
				
				
				
			}else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// send mail to all admins about the update
	
	private void sendMailtoAllAdmin(String status, String user, List<String> allAdminEmail) {
		
		
		allAdminEmail.remove(jwtFilter.getCurrentUser());   // except the current admin who is sending mail
		
		if(status!=null && status.equalsIgnoreCase("true")) {
			
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:- "+user+"\n is approved by \nADMIN"+jwtFilter.getCurrentUser(),allAdminEmail );
		}
		else {
			
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "USER:- "+user+"\n is disabled by \nADMIN"+jwtFilter.getCurrentUser(),allAdminEmail );
		}
		
		
	}
	
//********************************************************************************************************************************************************************************************	
	
	

}
