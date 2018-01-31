/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.mgmt.NotificationManagerJavaImpl;
import com.techm.nms.mgmt.NotificationManagerWSImpl;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.util.AddressDetails;
import com.techm.nms.util.MessageDetails;

/**
 * @author sm0015566
 *
 */
public class NMSJavaPOJOTest {

	
	
	public void test(String host,String port,String username,String pwd)
	{
		
		//NotificationManagerJavaImpl obj=NotificationManagerJavaImpl.getInstance(host, port, username, pwd);
		NotificationManagerJavaImpl obj=NotificationManagerJavaImpl.getInstance();
		/*
		MessageDetails nonTemplate=getNonTemplatePOJO();
		setMessageDetails(nonTemplate,"7777", "SMS", "983983");
		nonTemplate.setDataFeed(getSMSFeed());
		try {
			obj.sendMessage(nonTemplate);
		} catch (NotificationMgrGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MessageDetails sms=getTextPOJO();
		setMessageDetails(sms,"7777", "SMS", "983983");
		sms.setDataFeed(getSMSFeed());
		try {
			obj.sendMessage(sms);
		} catch (NotificationMgrGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// END OF SMS
		
		//NotificationManagerJavaImpl email=NotificationManagerJavaImpl.getInstance(host, port, username, pwd);
		NotificationManagerJavaImpl email=NotificationManagerJavaImpl.getInstance();
		MessageDetails emailMsg=getEmailPOJO();
		setMessageDetails(emailMsg,"abc@x.com", "EMAIL","abc@x.com");
		NotificationHistory history=null;
		emailMsg.setDataFeed(getEmailFeed());
		try{
			history=email.sendNotification(emailMsg);
			
			byte[] emailContent=history.getEmailContentByteArray();
			
			FileOutputStream fos=null;
			try {
				fos = new FileOutputStream(new File("D:\\Sujoy\\Test\\nms.html"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fos.write(emailContent);
			fos.close();
		}catch (NotificationMgrGenericException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		//NotificationManagerJavaImpl sms2nms=NotificationManagerJavaImpl.getInstance(host, port, username, pwd);
		NotificationManagerJavaImpl sms2nms=new NotificationManagerJavaImpl();
		MessageDetails sms2=getTextPOJO();
		setMessageDetails(sms2,"777", "SMS", "983");
		sms2.setDataFeed(getSMSFeed());
		try {
			sms2nms.sendMessage(sms2);
		} catch (NotificationMgrGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//NotificationManagerJavaImpl email2nms=NotificationManagerJavaImpl.getInstance(host, port, username, pwd);
		NotificationManagerJavaImpl email2nms=new NotificationManagerJavaImpl();
		MessageDetails email2=getEmailPOJO();
		setMessageDetails(email2,"PQR@Y.com", "EMAIL", "XYZ@x.com");
		sms2.setDataFeed(getSMSFeed());
		try {
			email2nms.sendMessage(email2);
		} catch (NotificationMgrGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	private void setMessageDetails(MessageDetails msgDetails,String recipientId, String recipientType,String deliveryAddress){
		
		setRecipientId(msgDetails,recipientId);
		setRecipientType(msgDetails,recipientType);
		setAddress(msgDetails,recipientType,recipientId);
				
		msgDetails.setSource("TEST");
		
		ArrayList<String> intId=new ArrayList();
		intId.add(recipientId);
		msgDetails.setIntegrationID(intId);
		
		ArrayList<String> deliveryAddr=new ArrayList<String>();
		deliveryAddr.add(deliveryAddress);
		//msgDetails.set
		
		//return msgDetails;
	}
	private void setRecipientId(MessageDetails msgDetails,String recipient){
		ArrayList<String> recipientId=new ArrayList();
		recipientId.add(recipient);
		msgDetails.setRecipientId(recipientId);
	}
	
	private void setRecipientType(MessageDetails msgDetails,String type){
		ArrayList<String> recipientType=new ArrayList();
		recipientType.add(type);
		msgDetails.setRecipientType(recipientType);
	}
	
	private void setAddress(MessageDetails msgDetails, String addressType,String address){
		AddressDetails recipient=new AddressDetails();
		recipient.setAddressType(addressType);
		recipient.setAddress(address);
		ArrayList<AddressDetails> addrs=new ArrayList<AddressDetails>();
		addrs.add(recipient);
		
		msgDetails.setRecipients(addrs);
	}
	
	private HashMap<String,String> getSMSFeed(){
		HashMap<String,String> dataFeed=new HashMap();
		dataFeed.put("NAME", "Sujoy");
		dataFeed.put("PLAN_NAME", "SMS");
		return dataFeed;
	}
	
	private HashMap<String,String> getEmailFeed(){
		HashMap<String,String> dataFeed=new HashMap();
		dataFeed.put("FirstName", "Sujoy");
		dataFeed.put("BillBalance", "20.00");
		dataFeed.put("DueDate", "10/06/2017");
		dataFeed.put("PaymentMethod", "DDA");
		return dataFeed;
	} 
	private MessageDetails getTextPOJO(){
		MessageDetails msgDetails=new MessageDetails();
		//msgDetails.setSender("msujoy@techmahindra.com","Email");
		//msgDetails.setSubject("Test Email");
		msgDetails.setSender("9999", "SMS");
		msgDetails.setScenarioId(33);
		//msgDetails.setEmailTemplate(false);
		msgDetails.setTextTemplate(true);
		msgDetails.setTemplateFile("TestSMS.ftl");
		msgDetails.setTemplateDir("D:\\NMSTest\\SMS");
		msgDetails.setDeliveryMode("SMS");
		return msgDetails;
	}
	private MessageDetails getNonTemplatePOJO(){
		MessageDetails msgDetails=new MessageDetails();
		//msgDetails.setSender("msujoy@techmahindra.com","Email");
		//msgDetails.setSubject("Test Email");
		msgDetails.setSender("9999", "SMS");
		msgDetails.setScenarioId(33);
		//msgDetails.setEmailTemplate(false);
		msgDetails.setTextTemplate(true);
		msgDetails.setTemplateFile(null);
		msgDetails.setTemplateDir(null);
		msgDetails.setDeliveryMode("SMS");
		return msgDetails;
	}
	private MessageDetails getEmailPOJO(){
		MessageDetails msgDetails=new MessageDetails();
		msgDetails.setSender("msujoy@techmahindra.com","Email");
		msgDetails.setSubject("Test Email");
		//msgDetails.setSender("9999", "SMS");
		msgDetails.setScenarioId(33);
		//msgDetails.setEmailTemplate(false);
		msgDetails.setEmailTemplate(true);
		msgDetails.setTemplateFile("Payment_Due_Notification.html");
		msgDetails.setTemplateDir("D:\\NMSTest\\Email\\");
		msgDetails.setDeliveryMode("EMail");
		msgDetails.setAttachmentURI("D:\\Sujoy\\Projects\\Yahsat\\Design\\Solution Docs\\SOA\\Notification\\boleto_presentation.pdf");
		return msgDetails;
	}
	
	
	public static void main(String args[]) throws Exception{
	
	NMSJavaPOJOTest test=new NMSJavaPOJOTest();
	//test.publishEndpoint();
	test.test("10.10.134.202","8811","weblogic","aia2dwl123");
	
	//NotificationManagerWSImpl nms=NotificationManagerWSImpl.getInstance("10.10.134.202","8811","weblogic","aia2dwl123");
	//nms.getListenerRef();
	//System.out.println("Done");
	/*
	//String host,String port,String username,String pwd, String templateDir,String dmsHost,String dmsPort
	NotificationManagerWSImpl nm=null;
	try{
	//nm=new NotificationManagerWSImpl(args[0],args[1],args[2],args[3],args[4]);
		System.out.println("Start");
		nm=new NotificationManagerWSImpl("10.33.137.233","7077","weblogic","weblogic123","D:\\EclipseWorkspace\\NotificationManager\\bin");
		System.out.println("End");
	}catch(Exception e){
		System.out.println("Inside Catch [ "+e);
	}
	MessageDetails msgDetails=new MessageDetails();
	msgDetails.setSender("msujoy@techmahindra.com","Email");
	msgDetails.setSubject("Test Email");
	msgDetails.setScenarioId("2");
	msgDetails.setEmailTemplate(true);
	msgDetails.setTemplateFile("TextTemplate.ftl");
	
	
	HashMap<String,String> dataFeed=new HashMap();
	dataFeed.put("msisdn", "9830108618");
	dataFeed.put("name", "Debu");
	msgDetails.setDataFeed(dataFeed);
	
	AddressDetails recipient=new AddressDetails();
	recipient.setAddressType("Email");
	recipient.setAddress("smju00@gmail.com");
	//recipient.setAddress("dg00352798@techmahindra.com");
	
	ArrayList<AddressDetails> addrs=new ArrayList<AddressDetails>();
	addrs.add(recipient);
	QName qName = new QName("http://schemas.oracle.com/bpel/extension", "remoteFault");
	//BPELFault bpelFault = new BPELFault(qName);
	
	//com.oracle.bpel.client.BPELFault = null;
	
	msgDetails.setRecipients(addrs);
	try{
	nm.sendMessage(msgDetails);
	}catch(NotificationMgrGenericException e){
		e.printStackTrace();
	
		//throw new Exception();
	}*/
}

}
