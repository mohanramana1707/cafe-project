package com.inn.cafe.Utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// For sending email

@Service
public class EmailUtils {
	
	
	// class to send mail
	@Autowired
	private JavaMailSender emailSender;
	
	public void sendSimpleMessage(String to,String subject,String text,List<String> list) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("mohanramana1707@gmail.com");   // enter mail id(same as in application.properties file)
		message.setTo(to);             // current logged in USER
		message.setSubject(subject);
		message.setText(text);     // data
		
		if(list!=null && list.size()>0) {
			message.setCc(getCcArray(list));  // all the admins
		}
		
		emailSender.send(message);
		
	}
	
	// List to String Array
	private String[] getCcArray(List<String> ccList) {
		
		String[] cc= new String[ccList.size()];
		
		for(int i=0;i<ccList.size();i++) {
			cc[i]=ccList.get(i);
		}
		return cc;
	}
	

}
