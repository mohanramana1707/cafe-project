package com.inn.cafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication  (  scanBasePackages={"com.inn.cafe.dao","com.inn.cafe.RestImpl","com.inn.cafe.Rest","com.inn.cafe.ServiceImpl","com.inn.cafe.Service",
												"com.inn.cafe.Model","com.inn.cafe.JWT","com.inn.cafe.Utility","com.inn.cafe.constents"} )  

public class CafeManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeManagementSystemApplication.class, args);
	}

}