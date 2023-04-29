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
	
	
	

}
