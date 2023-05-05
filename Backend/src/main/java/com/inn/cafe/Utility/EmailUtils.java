package com.inn.cafe.Utility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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
	
	
	// forgetMail = sending mail to reset password
	public void forgetMail(String to,String subject,String password) throws MessagingException {
		
		MimeMessage message=emailSender.createMimeMessage();
		
		MimeMessageHelper helper= new MimeMessageHelper(message,true);
		helper.setFrom("mohanramana1707@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		
		String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";

		message.setContent(htmlMsg,"text/html");
		
		emailSender.send(message);
	}

}
