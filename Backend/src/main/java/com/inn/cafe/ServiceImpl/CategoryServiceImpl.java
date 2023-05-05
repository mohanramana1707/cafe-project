package com.inn.cafe.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.Model.Category;
import com.inn.cafe.Service.CategoryService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.CategoryDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl  implements CategoryService{

	@Autowired
	CategoryDao categoryDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			
			// only ADMIN can able to add category
			if(jwtFilter.isAdmin()) {
				
				if(validateCategoryMap(requestMap,false)) {
					categoryDao.save(getCategoryFromMap(requestMap, false));
					return CafeUtils.getResponseEntity("category added succesfully", HttpStatus.OK);
				}
				
			}
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
// to check all the fields are in request body
	
	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		
		if(requestMap.containsKey("name")) {
			if(requestMap.containsKey("id") && validateId) {
				return true;
			}
			else if (!validateId){   // even if there is no id ,its valid
				return true;
			}
		}
		
		return false;
	}

// map to Category entity
	private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
		
		Category category= new Category();
		
		if(isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id"))  );
			
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	

	
//***************************************************************************************************************************************************

// GET a list of all category	if filtervalue is FALSE(request param)
// else if FILTER VALUE is true, returns CATEGORY which has PRODUCTS in it...(refer namedQ in Category model)
	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				
				log.info("inside if");
				return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
			}
			
			return new ResponseEntity<List<Category>>(categoryDao.findAll(),HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//***************************************************************************************************************************************************
// UPDATE any a category

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			
			if(jwtFilter.isAdmin()) { // to check he is a admin
				
				if(validateCategoryMap(requestMap, true)) { // check if requestbody has required fields(name)
					
					Optional<Category> optional =categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					
					
					if(!optional.isEmpty()) {
						
						categoryDao.save(getCategoryFromMap(requestMap, true));
						
						return CafeUtils.getResponseEntity("category updated successfully", HttpStatus.OK);	
						
					}
					else 
						return CafeUtils.getResponseEntity("category id doesnt exists", HttpStatus.OK);
				 }
				
				
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				
			}
			else {
			
			log.info("inside update category service impl-not-admin ");
			
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}








}
