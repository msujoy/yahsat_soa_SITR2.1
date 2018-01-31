/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import oracle.ucs.messaging.ws.types.MessageInfo;
import oracle.ucs.messaging.ws.types.MimeHeader;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.util.MessageDetails;
import com.techm.nms.util.NotificationUtils;
import com.techm.templatemgr.exception.TemplateMgrException;
import com.techm.templatemgr.mgmt.HTMLTemplateManager;

/**
 * @author sm0015566
 *
 */
public abstract class TemplatizedMessageHandlerImpl implements MessageHandler {

	protected static String templateDir;
	
	private static HTMLTemplateManager mgr;
	
	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error"); 

	/** Declaration of debug logger responsible for logging error */
	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");
	
	//public abstract Object setMessageProperties(String implType);
	
	protected MimeHeader getMimeHeader(){
		MimeHeader multiHeader = new MimeHeader();
		multiHeader.setName(oracle.sdp.messaging.Message.HEADER_SDPM_MULTIPLE_PAYLOAD);
		multiHeader.setValue(Boolean.TRUE.toString());
		return multiHeader;
	}
	public abstract Object createNotificationBody(MessageDetails msgDetails) throws NotificationMgrGenericException;
	
	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}
	
	protected static HTMLTemplateManager getHTMLTemplateManager() throws NotificationMgrGenericException {
		
		HTMLTemplateManager templateMgr=null;
		try {
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("getInstance() of HTMLTemplateManager being called...."); 
			templateMgr=HTMLTemplateManager.getInstance();
		} catch (TemplateMgrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException("Instantiating HTML Template Manager failed [" + e.getMessage() + "]", e);
		}
		return templateMgr;
	}
	
	/**
	 * Method used to create the body part of the message from ByteArrayOutputStream
	 * Generally used to create the content of the notification body
	 * @param bytes- ByteArrayOutStream after parsing the template of the message
	 * @param contentType- MIME content type.
	 * @return MimeBodyPart to be added in the notification MimeMultipart
	 */
	protected MimeBodyPart createBodyPart(ByteArrayOutputStream bytes,String contentType) throws NotificationMgrGenericException{
		
		ByteArrayDataSource ds=new ByteArrayDataSource(bytes.toByteArray(),contentType);
				
		MimeBodyPart body=null;
		
		try {
			body=new MimeBodyPart();
			body.setDataHandler(new DataHandler(ds));
			
			//body.addHeader(Message.HEADER_SDPM_PAYLOAD_PART_DELIVERY_TYPE, "EMAIL");
			
			//Checking whether the body part is generated properly
			//body.saveFile("test.rtf");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("Creation of MimeBodyPart of message got failed with reason[" + e.getMessage() + "]");
			throw new NotificationMgrGenericException("Creation of MimeBodyPart of message failed with reason [" + e.getMessage() + "]",e);
		} 
		return body;
	}
	/**
	 * Method used to create the mime body part of the message from bytes array.
	 * Generally used to create MimeBodyPart of the attachment where the DMS would respond back with the bytes array of the requested file
	 * @param bytes - byte array retrieved from the file from DMS
	 * @param contentType- MIME content type
	 * @return MimeBodyPart of the attachment
	 */
	protected MimeBodyPart createBodyPart(byte[] bytes, String contentType) throws NotificationMgrGenericException{
		debugLogger.debug("TemplateMessageHandler Bytes passed in createAttachment[" + bytes +"]" );
		//ByteArrayDataSource ds=new ByteArrayDataSource(bytes,NotificationUtils.getContentType("EMAIL"));
		ByteArrayDataSource ds=new ByteArrayDataSource(bytes,contentType);
		
		MimeBodyPart body=null;
		
		try {
			body=new MimeBodyPart();
			body.setDataHandler(new DataHandler(ds));
			debugLogger.debug("Content Type in createBodyPart " + body.getContentType());
			
			//body.addHeader(Message.HEADER_SDPM_PAYLOAD_PART_DELIVERY_TYPE, "EMAIL");
			
			//Checking whether the body part is generated properly
			//body.saveFile("test.rtf");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("Creation of MimeBodyPart of attachment got failed with reason [" + e.getMessage() + "]");
			throw new NotificationMgrGenericException("Creation of MimeBodyPart of attachment got failed with reason [" + e.getMessage() + "]", e);
		} 
		return body;
	}
}
