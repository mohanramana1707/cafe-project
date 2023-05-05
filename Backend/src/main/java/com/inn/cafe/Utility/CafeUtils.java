package com.inn.cafe.Utility;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CafeUtils {    // contains generic methods 

	
	private CafeUtils() {
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
		
		return new ResponseEntity<String>("message : "+responseMessage,httpStatus);
	}
	
	// to GET the unique bill number
	
	public static String getUUID() {
		
		//LocalDate date= LocalDate.now();
		Date date = new Date(2323223232L);
		
		long time= date.getTime();
		
		return "BILL-"+time;
				
		
	}
	
	//string to json Array
	public static JSONArray getJsonArrayFromString(String data) throws JSONException {
		
		JSONArray jsonArray= new JSONArray(data);
		
		return jsonArray;
			
	}
	
	//each  json to map
	public static Map<String,Object> getMapFromJson(String data){
		
		if(!Strings.isNullOrEmpty(data)) {
			return new Gson().fromJson(data, new TypeToken<Map<String,Object>>(){	
			}.getType());
		}
		return new HashMap<>();
		
		
	}
	
	// to check if the file exists in the location or not
	
	
	public static Boolean isFileExist(String path) {
		
		log.info("inside isFileExist() : "+ path);
		try {
			
			File file= new File(path);
			
			if(file!=null && file.exists()) {
				return Boolean.TRUE;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
			