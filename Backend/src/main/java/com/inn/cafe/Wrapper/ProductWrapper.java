package com.inn.cafe.Wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

	public class ProductWrapper {
		
	
	
		Integer id;
		String name;
		String description;
		Integer price;
		String status;
		Integer categoryId;
		String categoryName;
		
		// all args constructor
		
		
		
	
		// for get all product by category id
		public ProductWrapper(Integer id,String name) {
			this.id=id;
			this.name=name;
		}
		
		// for get product based on id
		public ProductWrapper(Integer id,String name,String description,Integer price) {
			this.id=id;
			this.name=name;
			this.description=description;
			this.price=price;
		}
			
		
	}



	
