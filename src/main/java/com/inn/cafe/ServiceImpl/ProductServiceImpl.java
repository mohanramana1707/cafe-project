package com.inn.cafe.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.Model.Category;
import com.inn.cafe.Model.Product;
import com.inn.cafe.Service.ProductService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.Wrapper.ProductWrapper;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.ProductDao;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;
	
	@Autowired
	JwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			
			if(jwtFilter.isAdmin()) {
				if(validateProductMap(requestMap,false)) {
					
					productDao.save(getProductFromMap(requestMap,false));
					return CafeUtils.getResponseEntity("product added Successfully", HttpStatus.OK);
					
				}
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				
				
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	



	// boolean is used as this helper method is being used by 2 methods[for SAVING & UPDATIONG ](to differntiate by T/F)
	
	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		
		if(requestMap.containsKey("name")){
			if(requestMap.containsKey("id") && validateId) {  // if validateid= true(for UPDATION it should TRUE)
				return true;
			}
			else if(!validateId){  // if validateid= false
				return true;
			}
			
		}
		return false;
	}
	
	// boolean is used as this helper method is being used by 2 methods[for SAVING & UPDATIONG ](to differntiate by T/F)
	// for saving no id is required(FALSE) & for updation requires id(TRUE)
	
	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
		
		Category category= new Category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		
		Product product = new Product();
		
		if(isAdd) {	 // for updation of product
			product.setId(Integer.parseInt(requestMap.get("id"))) ;		
		}else {     //adding new product
			product.setStatus("true");	
		}
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")) );
		
		
		return product;
	}


//**********************************************************************************************************************************************


	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProducts() {
	
		try {
			
			return new ResponseEntity<List<ProductWrapper>>(productDao.getAllProduct(),HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<ProductWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}





	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		
		try {
			
			if(jwtFilter.isAdmin()) {
				
				if(validateProductMap(requestMap, true)) {
					Optional<Product> optional= productDao.findById(Integer.parseInt(requestMap.get("id")));
					
					if(!optional.isEmpty()) {
						
						Product product= getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						
						return CafeUtils.getResponseEntity("Product updated successfully", HttpStatus.OK);
																																																																												
						
					}
					return CafeUtils.getResponseEntity("Product id doesnt exists", HttpStatus.OK);
					
				}
				else {
					return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
				
			}
			return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
