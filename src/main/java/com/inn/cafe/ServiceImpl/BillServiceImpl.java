package com.inn.cafe.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Phaser;
import java.util.stream.Stream;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.Model.Bill;
import com.inn.cafe.Service.BillService;
import com.inn.cafe.Utility.CafeUtils;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.BillDao;
import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillServiceImpl implements BillService {
	
	@Autowired
	BillDao billDao;
	
	@Autowired
	JwtFilter jwtFilter;

	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		try {
			
			log.info("inside generate Report");
			String fileName;
			
			if(ValidateRequestMap(requestMap)) {
				// if file
				if(requestMap.containsKey("isGenerate") && (Boolean) requestMap.containsKey("isGenerate")) {
					fileName=(String) requestMap.get("uuid");
				}
				
				else {
					fileName=CafeUtils.getUUID();
					requestMap.put("uuid",fileName);
					insertBill(requestMap);
					
				}
				
				String data="Name: "+requestMap.get("name")+"\n"+"Contact Number: "+requestMap.get("contactNumber")+"\n"
						 +"Email: "+requestMap.get("email")+"\n"+"Payment Method: "+requestMap.get("paymentMethod");
				
				// Document class
				Document document= new Document();
				PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION+"\\" + fileName + ".pdf"));
				
				document.open();
				
				// FILE BORDER
				setRectangleInPdf(document);   // local method to add  rectangle table
				
				
				// local method- HEADING
				Paragraph chunk = new Paragraph("Cafe Management System ",getFont("Header")); 
				chunk.setAlignment(Element.ALIGN_CENTER);
				document.add(chunk);
				
				// ACTUAL DATA
				Paragraph paragraph= new Paragraph(data + "\n \n" + getFont("Data"));
				document.add(paragraph);
				
				//TABLE CONTENT
				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				
				// add table header
				addTableHeader(table);
				
				//get json array from string
				JSONArray jsonArray= CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
				
				// add data(each row) to the table
				
				for(int i=0;i<jsonArray.length();i++) {
					
					// get map obj from json
					addRow(table,CafeUtils.getMapFromJson(jsonArray.getString(i)));
				}
				// add to doc
				document.add(table);
				
				
				// add FOOTER
				Paragraph footer= new Paragraph("Total : "+ requestMap.get("totalAmount")+"\n" +
									" Thank you for Visiting.Please Visit Again" ,getFont("Data"));
				
				document.add(footer);
				
				document.close();
			
			
				return CafeUtils.getResponseEntity("{\"uuid\":\""+fileName+"\"}", HttpStatus.OK);
				
				
			}
			return CafeUtils.getResponseEntity("Required Data not found in request", HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// add rows to the table(from the each json  in Product details)
	private void addRow(PdfPTable table, Map<String, Object> data) {
		
		log.info("inside add rows (data)");
		
		table.addCell((String) data.get("name"));
		table.addCell((String) data.get("category"));
		table.addCell((String) data.get("quantity"));
		table.addCell(Double.toString( (Double)data.get("price")) );
		table.addCell(Double.toString( (Double)data.get("total")) );
				
		
	}

	// add table header
	private void addTableHeader(PdfPTable table) {
		log.info("inside add table header");
		
		// column wise header row (thats y stream)
		Stream.of("Name","Category","Quantity","Price","Sub Total")
		
			.forEach(columnTitle->{
				PdfPCell header= new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(2);
				header.setPhrase(new Phrase(columnTitle));
				header.setBackgroundColor(BaseColor.YELLOW);
				header.setVerticalAlignment(Element.ALIGN_CENTER);
				
				table.addCell(header);
			});
		
	}

	private Font getFont(String type) {
		
		log.info("inside getFont");
		
		switch (type) {
		case "Header": 
			Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		case "Data":
			Font dateFont=FontFactory.getFont(FontFactory.TIMES_ROMAN,18,BaseColor.BLACK);
			dateFont.setStyle(Font.BOLD);
			return dateFont;
			
		default:
			return new Font();
			
			
		
		}
	
		
	}

	private void setRectangleInPdf(Document document) throws DocumentException {
		log.info("Inside setRectangleInPdf");
		
		//Rectangle rect= new Rectangle(577,825,18,15); // 2 rectangle from different package
		
		com.itextpdf.text.Rectangle rect= new com.itextpdf.text.Rectangle(577, 825, 18, 15);
		
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		
		rect.setBorderColor(BaseColor.BLACK);
		rect.setBorderWidth(1);
		
		document.add(rect);
		
	}

	private void insertBill(Map<String, Object> requestMap) {
		try {
			Bill bill = new Bill();
			bill.setUuid((String)requestMap.get("uuid"));
			bill.setName((String) requestMap.get("name"));
			bill.setEmail((String) requestMap.get("email"));
			bill.setContactNumber((String) requestMap.get("contactNumber"));
			bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
			bill.setTotalAmount(Integer.parseInt((String)requestMap.get("totalAmount")) ); // exception
			
			bill.setProductDetails((String) requestMap.get("productDetails")); 
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			
			billDao.save(bill);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private boolean ValidateRequestMap(Map<String, Object> requestMap) {
		
		if(requestMap.containsKey("name") 
				&&requestMap.containsKey("contactNumber") 
				&& requestMap.containsKey("email") 
				&& requestMap.containsKey("paymentMethod")
				&& requestMap.containsKey("productDetails")
				&& requestMap.containsKey("totalAmount")) {
			return true;
		}
		
		return false;
	}
//*******************************************************************************************************************************************	

	//get bills and generate pdf
	
	@Override
	public ResponseEntity<List<Bill>> getBills() {
		
		
		List<Bill> list= new ArrayList<>();
		
		// if admin, get all the bills
		if(jwtFilter.isAdmin()) {
			list= billDao.getAllBills();
		}
		// else Logged in user bill
		else {
			list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
		}
		
		return new ResponseEntity<List<Bill>>(list,HttpStatus.OK);
	}

//*******************************************************************************************************************************************	

	// separate url for gettingPDF of given UUID
	
	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		log.info("inside get Pdf: requestMap {} ",requestMap);
		try {
			byte[] byteArray= new byte[0];
			if(!requestMap.containsKey("uuid") && ValidateRequestMap(requestMap)) {
				
				return new ResponseEntity<byte[]>(byteArray,HttpStatus.BAD_REQUEST);
			}
			// exact file location with given uuid
			String filePath= CafeConstants.STORE_LOCATION + "\\"+(String)requestMap.get("uuid") + ".pdf";
			
			if(CafeUtils.isFileExist(filePath))
			{
				byteArray= getByteArray(filePath);
				return new ResponseEntity<byte[]> (byteArray,HttpStatus.OK);
			}
			// if file doesn't exists in location
			else {
				requestMap.put("isGenerate",false);
				generateReport(requestMap);
				
				byteArray = getByteArray(filePath);
				
				return new ResponseEntity<byte[]>(byteArray,HttpStatus.OK);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getByteArray(String filePath) throws Exception {
		
		File initialFile= new File(filePath);
		InputStream targetStream= new FileInputStream(initialFile);
		
		byte[] byteArray=IOUtils.toByteArray(targetStream);
		
		targetStream.close();
 		
		
		return byteArray;
	}

//*******************************************************************************************************************************************	

	// delete bill based on id
	
	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		try {
			java.util.Optional<Bill> optional= billDao.findById(id);
			
			if(!optional.isEmpty()) {
				
				billDao.deleteById(id);
				return CafeUtils.getResponseEntity("Bill deleted successfully", HttpStatus.OK);
				
			}
			return CafeUtils.getResponseEntity("Bill doesn't exist", HttpStatus.BAD_REQUEST);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
