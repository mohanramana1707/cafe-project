package com.inn.cafe.Rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inn.cafe.Wrapper.ProductWrapper;

@RequestMapping(path = "product")
public interface ProductRest {
	
	@PostMapping(path = "/add")
	ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> requestMap);
	
	@GetMapping(path="/get")
	ResponseEntity<List<ProductWrapper>> getAllProducts();

	@PostMapping(path = "/update")
	ResponseEntity<String> updateProduct(@RequestBody Map<String,String> requestMap);
}
