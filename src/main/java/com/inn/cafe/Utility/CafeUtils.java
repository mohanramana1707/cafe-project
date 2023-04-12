package com.inn.cafe.Utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {    // contains generic methods 

	
	private CafeUtils() {
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
		
		return new ResponseEntity<String>("message : "+responseMessage,httpStatus);
	}
}
