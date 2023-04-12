package com.inn.cafe.Rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping(path = "/user")
public interface UserRest {
	
	@PostMapping  (path = "/signup")
	public ResponseEntity<String> signup(@RequestBody(required = true) Map<String,String> requestMap);
	
	@PostMapping (path = "/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap);

	//@GetMapping(path = "/get")
	
}
