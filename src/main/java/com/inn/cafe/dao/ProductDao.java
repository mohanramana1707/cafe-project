package com.inn.cafe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.cafe.Model.Product;
import com.inn.cafe.Wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer>{
	
	List<ProductWrapper> getAllProduct();

}
