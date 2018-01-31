/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import oracle.ucs.messaging.ws.Listener;
import oracle.ucs.messaging.ws.types.ListenerReference;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.listener.NotificationStatusListener;
import com.techm.nms.mgmt.NotificationManager;
import com.techm.nms.mgmt.NotificationManagerWSImpl;
import com.techm.nms.util.AddressDetails;
import com.techm.nms.util.MessageDetails;

/**
 * @author sm0015566
 *
 */
public class NotificationManagerWSImplTest {
	
	public void test(){
		NotificationManagerWSImpl test=NotificationManagerWSImpl.getInstance("10.10.134.202","8811","weblogic","aia2dwl123");
		MessageDetails msgDetails=new MessageDetails();
		//msgDetails.setSender("msujoy@techmahindra.com","Email");
		//msgDetails.setSubject("Test Email");
		msgDetails.setSender("9999", "SMS");
		msgDetails.setScenarioId(33);
		//msgDetails.setEmailTemplate(false);
		msgDetails.setTextTemplate(true);
		msgDetails.setTemplateFile("200.ftl");
		msgDetails.setTemplateDir("D:\\");
		msgDetails.setDeliveryMode("SMS");
		//msgDetails.setMsgTxt("Test !!!!! ");
		
		ArrayList<String> recipientId=new ArrayList();
		recipientId.add("7777777");
		msgDetails.setRecipientId(recipientId);
		
		ArrayList<String> recipientType=new ArrayList();
		recipientType.add("SMS");
		msgDetails.setRecipientType(recipientType);
		
		AddressDetails recipient=new AddressDetails();
		recipient.setAddressType("SMS");
		recipient.setAddress("9867777");
		ArrayList<AddressDetails> addrs=new ArrayList<AddressDetails>();
		addrs.add(recipient);
		
		msgDetails.setRecipients(addrs);
		msgDetails.setSource("TEST");
		
		ArrayList<String> intId=new ArrayList();
		intId.add("77777");
		msgDetails.setIntegrationID(intId);
		
		ArrayList<String> deliveryAddr=new ArrayList<String>();
		deliveryAddr.add("88888");
		//msgDetails.set
		HashMap<String,String> dataFeed=new HashMap();
		dataFeed.put("FirstName", "Nanditaa");
		dataFeed.put("Surname", "Saha");
		dataFeed.put("ACCOUNTNO", "1234");
		dataFeed.put("OVERDUEAMOUNT", "$10");
		//dataFeed.put("PLAN_NAME", "$29 Carryover Plan's");
		msgDetails.setDataFeed(dataFeed);
		try {
			test.sendMessage(msgDetails);
		} catch (NotificationMgrGenericException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String args[]) throws Exception{
		
		NotificationManagerWSImplTest test=new NotificationManagerWSImplTest();
		//test.publishEndpoint();
		test.test();
		
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
	
	private void publishEndpoint(){
		Listener listener =  new NotificationStatusListener();
		System.out.println("***************Inside getListenerRef() Method");
		//String callbackURL = "http://localhost:7001/UMSStatusListener";
		String callbackURL = "http://10.33.137.233:7077/ListenerService";
		try{
		Endpoint nmsStatusEndpoint = javax.xml.ws.Endpoint.publish(callbackURL, listener);
		
		System.out.println("***** After endpoint set");
		}catch(Exception e){
			System.out.println("***** Endpoint set exception"+e.getMessage());
			e.printStackTrace();
		}
	}
	private ListenerReference getListenerRef(){
		
		Listener listener =  new NotificationStatusListener();
		System.out.println("***************Inside getListenerRef() Method");
		//String callbackURL = "http://localhost:7001/UMSStatusListener";
		String callbackURL = "http://10.33.137.233:7077/UMSStatusListener";
		try{
		Endpoint nmsStatusEndpoint = javax.xml.ws.Endpoint.publish(callbackURL, listener);
		
		System.out.println("***** After endpoint set");
		}catch(Exception e){
			System.out.println("***** Endpoint set exception"+e.getMessage());
			e.printStackTrace();
		}
		ListenerReference ref=new ListenerReference();
		
		ref.setEndpoint(callbackURL);
		System.out.println("********* Ref"+ref.toString());
		return ref;
	}


}
