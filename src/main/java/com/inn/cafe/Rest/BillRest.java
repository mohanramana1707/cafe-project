package com.inn.cafe.Rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inn.cafe.Model.Bill;

import jakarta.websocket.server.PathParam;

@RequestMapping(path="/bill")
public interface BillRest {
	
	@PostMapping(path = "generateReport")
	ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);   // getting json object of product
	
	
	@GetMapping(path="getBills")
	ResponseEntity<List<Bill>> getBills();
	
	// front end will take care of converting byte in pdf
	@PostMapping(path="getPdf")
	ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap);
	
	@PostMapping(path="delete/{id}")
	ResponseEntity<String> deleteBill(@PathVariable Integer id);
	
	
	
}
