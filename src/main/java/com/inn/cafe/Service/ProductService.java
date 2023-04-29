package com.inn.cafe.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.Wrapper.ProductWrapper;

public interface ProductService {
	
	ResponseEntity<String> addNewProduct(Map<String, String> requestMap);
	
	ResponseEntity<List<ProductWrapper>> getAllProducts();
	
	ResponseEntity<String> updateProduct(Map<String, String> requestMap);

}

