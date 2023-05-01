package com.inn.cafe.Model;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name="Bill.getAllBills",query ="select b from Bill b order by b.id desc")

@NamedQuery(name="Bill.getBillByUserName",query ="select b from Bill b  where b.createdBy=:username order by b.id desc ")





@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "bill")
public class Bill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "contactNumber")
	private String contactNumber;
	
	@Column(name = "paymentMethod")
	private String paymentMethod;
	
	@Column(name = "total")
	private Integer totalAmount;
	
	
	@Column(name = "productDetails",columnDefinition = "json")   // list of json 
	private String productDetails;
	
	@Column(name = "createdBy")
	private String createdBy;
	
	
	
	
	
	

}
