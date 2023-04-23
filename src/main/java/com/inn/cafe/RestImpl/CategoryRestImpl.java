package com.inn.cafe.RestImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.Model.Category;
import com.inn.cafe.Rest.CategoryRest;
import com.inn.cafe.Service.CategoryService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.constents.CafeConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CategoryRestImpl implements CategoryRest{

	@Autowired
	CategoryService categoryService;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			
			return categoryService.addNewCategory(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			
			return categoryService.getAllCategory(filterValue);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			log.info("inside update category rest impl");
			return categoryService.updateCategory(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
