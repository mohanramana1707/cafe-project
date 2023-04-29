package com.inn.cafe.RestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.Rest.ProductRest;
import com.inn.cafe.Service.ProductService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.Wrapper.ProductWrapper;
import com.inn.cafe.constents.CafeConstants;

@RestController
public class ProductRestImpl implements ProductRest {
	
	@Autowired
	ProductService productService;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		
		try {
			
			return productService.addNewProduct(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG ,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
		try {
			return productService.getAllProducts();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			return productService.updateProduct(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG ,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}