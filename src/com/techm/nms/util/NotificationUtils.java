package com.techm.nms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import org.apache.log4j.Logger;

import oracle.jrf.PortabilityLayerException;
import oracle.sdp.messaging.Status;
import oracle.sdpinternal.messaging.platform.MessagingPlatform;
import oracle.sdpinternal.messaging.platform.MessagingPlatformFactory;

import com.techm.templatemgr.util.Util;

public class NotificationUtils implements Serializable {

	public static final String WEBLOGIC_INITIAL_CTX_FACTORY="weblogic.jndi.WLInitialContextFactory";
	
	public static final String NMS_IMPL_CAT_JAVA="Java";
	
	public static final String NMS_IMPL_CAT_WS="WS";
	
	public static final String BOLETO_CONTENT_TYPE="application/pdf";
	
	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");
	
	public static InputStream getEmailContent(Object byteArray){
		InputStream inputStream=null;
		if(byteArray!=null){
		
			ByteArrayOutputStream baos=(ByteArrayOutputStream)byteArray;
			
			inputStream=new ByteArrayInputStream(baos.toByteArray());
		}
		return inputStream;
	}
	
	public static String getContentType(String notificationType){
		String contentType=null;
		if(notificationType.equals("EMAIL"))
			contentType="text/html; charset=UTF-8";
		if(notificationType.equals("SMS"))
			contentType="text/plain; charset=UTF-8";
		/*if(notificationType.equals("ATTACHMENT"))
			contentType=Util.CONTENT_TYPE_ATTACHMENT;*/
		return contentType;
	}
	
	public static byte[] getUUID(){
		
		int size = 16;
		byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;

	}
	
	public static void printPlatformDetails(){
			
	/**
	 * Code snippet to check the server name 
	 */
	MessagingPlatform platform = MessagingPlatformFactory.getInstance();
	
	
	try {
		debugLogger.debug("##################################################################");
		debugLogger.debug(" CLUSTER NAME : ["+platform.getServerPlatformSupport().getClusterName()+"]");
		debugLogger.debug(" DOMAIN NAME : ["+platform.getServerPlatformSupport().getDomainName()+"]");
		debugLogger.debug(" SERVER NAME : ["+platform.getServerPlatformSupport().getServerName()+"]");
		debugLogger.debug(" APPLICATION NAME : ["+platform.getApplicationName()+"]");
		debugLogger.debug(" APPLICATION IDENTIFIER : ["+platform.getServerPlatformSupport().getApplicationIdentifier()+"]");
		debugLogger.debug("##################################################################");
	} catch (PortabilityLayerException e1) {
		// TODO Auto-generated catch block
		debugLogger.debug("Enviroment details not found");
		e1.printStackTrace();
	}
	}
	
	public static void printMessageStatus(Status status, Serializable correlator){
		debugLogger.debug("===== Received message with correlator id :[" + correlator + "] ===========");
		debugLogger.debug(" == Updating Notification History with details ==== ");
		debugLogger.debug(" == Message Id: " + status.getMessageId() + " ==== ");
		debugLogger.debug(" == Delivery Status : " + status.getType().name() + " ==== ");
	}
}

