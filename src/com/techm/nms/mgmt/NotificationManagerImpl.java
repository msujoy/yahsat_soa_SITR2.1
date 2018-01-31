/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.sql.dto.EmailContent;
import com.techm.nms.util.AddressDetails;
import com.techm.nms.util.ConnectionManager;
import com.techm.nms.util.MessageDetails;
import com.techm.templatemgr.exception.TemplateMgrException;
import com.techm.templatemgr.mgmt.TemplateManager;
import com.techm.templatemgr.mgmt.TemplateManagerImpl;
import com.techm.templatemgr.util.Util;

import oracle.sdp.messaging.Address;
import oracle.sdp.messaging.Message;
import oracle.sdp.messaging.MessageInfo;
import oracle.sdp.messaging.MessagingClientFactory;
import oracle.sdp.messaging.MessagingFactory;
//import oracle.sdp.messaging.MessagingClient;
import oracle.ucs.messaging.ws.MessagingClient;
import oracle.ucs.messaging.ws.types.MimeHeader;

import com.techm.dms.exception.ConnectionException;
import com.techm.dms.exception.DMSInteractionException;
import com.techm.dms.mgmt.*;
import com.techm.dms.utils.DMSRequest;
import com.techm.dms.utils.DMSResponse;

/**
 * @author sm0015566
 *
 */
public abstract class NotificationManagerImpl implements NotificationManager {

	protected String host;
	protected String port;
	protected String username;
	protected String pwd;

	/*
	 * public Object setNotificationProperties(MessageDetails msgDetails)throws
	 * NotificationMgrGenericException{
	 * 
	 * return null; }
	 */

	// protected String dmsHost;
	// protected String dmsPort;

	// protected String templateDir;
	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error");

	/** Declaration of debug logger responsible for logging error */

	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	private static EmailMessageHandlerImpl emailMsgHandler;

	private static TextMessageHandlerImpl textMsgHandler;
	
	private static MessageHandler msgHandler;
	
	private static NonTemplatizedMessageHandler nonTempMsgHandler;

	/**
	 * 
	 */
	public NotificationManagerImpl() {
		// TODO Auto-generated constructor stub
		System.out
				.println("In the constructor of base NotificationManagerImpl");
	}

	public NotificationManagerImpl(String host, String port, String username,
			String pwd) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.pwd = pwd;

		// System.out.println("host[" + host + "]port["+port+"]user[" + username
		// + "]pwd[" + pwd + "]");

	}

	/**
	 * The method used to populate SMS content in notification history table
	 * 
	 * @param notifBody
	 * @return SMS Content in a message
	 */
	protected Object getMessageContent(Object notifBody) throws NotificationMgrGenericException{
		Object msgContent = null;
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		
		if (notifBody instanceof String)
			msgContent = notifBody;
		//The following code is added by Sujoy(1st Aug,2017) to store the mime message content(email)
		if(notifBody instanceof MimeMultipart){
			try {
				((MimeMultipart)notifBody).writeTo(out);
				/**
				//To check whether proper file is being written
				 FileOutputStream fos=new FileOutputStream("D:\\TestFile.html");
				 fos.write(out.toByteArray());
				 fos.close();
				 
				 // Code end
				  * */
				  
				
			} catch (IOException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorLogger.error("Retrieving the content of notification message got failed due to[" + e.getMessage() + "]");
				throw new NotificationMgrGenericException("Retrieving the content of notification message got failed", e);
			}
			msgContent=out;
		}
		if(notifBody instanceof ByteArrayOutputStream)
			msgContent=notifBody;
		if(notifBody instanceof EmailContent)
			msgContent=((EmailContent)notifBody).getEmailContentAsByteArray();
		return msgContent;
	}

	protected EmailMessageHandlerImpl getEmailMsgHandler(MessageDetails msgDetails){
		
		if (emailMsgHandler == null) // Code added on 28th Apr for
			// minimizing memory usage
		{
			emailMsgHandler = new EmailMessageHandlerImpl();
			emailMsgHandler.setTemplateDir(msgDetails.getTemplateDir()); 
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("Email Message Handler Initiated ..... and template directory set to " + msgDetails.getTemplateDir());
			
		}
		return emailMsgHandler;
	}
	
	protected TextMessageHandlerImpl getTextMsgHandler(MessageDetails msgDetails){
		
		if (textMsgHandler == null){
			textMsgHandler = new TextMessageHandlerImpl();
			textMsgHandler.setTemplateDir(msgDetails.getTemplateDir());
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("Text Message Handler Initiated .....");
			
		}
		return textMsgHandler;
	}
	
	protected NonTemplatizedMessageHandler getNonTempMsgHandler(MessageDetails msgDetails){
		if (nonTempMsgHandler == null){
			
			nonTempMsgHandler = new NonTemplatizedMessageHandler();
			debugLogger.debug("#" + msgDetails.getCorrelator()
	
			+ "# System is trying to send Non-Template SMS...");
		
		}
		return nonTempMsgHandler;
	}
	
	//TODO - Problematic one(29th Apr- Last Instance data stayed --
	protected MessageHandler getMessageHandlerInstance(MessageDetails msgDetails) {
		

		if (msgDetails.getTemplateFile() != null
				&& msgDetails.isEmailTemplate()) {
			
			debugLogger.debug("#" + msgDetails.getCorrelator()
					+ "# System is trying to find ["
					+ msgDetails.getTemplateFile()
					+ "] template file from Location ["
					+ msgDetails.getTemplateDir() + "]");
		} else if (msgDetails.getTemplateFile() != null
				&& msgDetails.isTextTemplate()) {
			
			}
			debugLogger.debug("#" + msgDetails.getCorrelator()
					+ "# System is trying to find ["
					+ msgDetails.getTemplateFile()
					+ "] template file from Location ["
					+ msgDetails.getTemplateDir() + "]");
			return null;
		} 
		

	protected ArrayList<oracle.ucs.messaging.ws.types.Address> addRecipients(
			MessageDetails details) {

		ArrayList<AddressDetails> recipients = details.getRecipients();
		ArrayList<oracle.ucs.messaging.ws.types.Address> addressList = new ArrayList<oracle.ucs.messaging.ws.types.Address>();
		oracle.ucs.messaging.ws.types.Address address = null;

		for (int i = 0; i < recipients.size(); i++) {
			address = oracle.ucs.messaging.ws.MessagingFactory
					.createAddress(recipients.get(i).getAddressType() + ":"
							+ recipients.get(i).getAddress());
			addressList.add(address);
		}
		return addressList;
	}

	protected MimeHeader populateHeader(String contentType) {
		MimeHeader mh = new MimeHeader();
		mh.setName("Content-Type");
		mh.setValue(contentType);
		return mh;
	}

	/**
	 * Used to create notification body
	 * 
	 * @param bytes
	 *            ByteArrayOutputStream of merged template
	 * @param notificationType
	 *            Type of the notification(EMAIL,SMS)
	 * @return content of the notification body
	 */
	/*
	 * protected final MimeBodyPart createNotificationBody(ByteArrayOutputStream
	 * bytes,String notificationType){ ByteArrayDataSource ds=new
	 * ByteArrayDataSource
	 * (bytes.toByteArray(),getContentType(notificationType));
	 * System.out.println("Content Type For Mail Body ..." +
	 * ds.getContentType());
	 * 
	 * MimeBodyPart body=null;
	 * 
	 * try { body=new MimeBodyPart(); body.setDataHandler(new DataHandler(ds));
	 * body.addHeader(Message.HEADER_SDPM_PAYLOAD_PART_DELIVERY_TYPE,
	 * notificationType);
	 * 
	 * //Checking whether the body part is generated properly
	 * //body.saveFile("test.rtf"); } catch (MessagingException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return body; }
	 */
	/*
	 * private byte [] getAttachmentContent(){
	 * 
	 * byte [] byteArrayData = null;
	 * 
	 * ResponseProcessor responseProcessor = new ResponseProcessor(); try {
	 * byteArrayData
	 * =responseProcessor.populateResponse("80c7abd2311a4509b37b33cfe7b89f8b"
	 * ).getBinaryData(); } catch (ConnectionException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return byteArrayData; }
	 */

	/*
	 * private byte [] getAttachmentContent(){
	 * 
	 * FileInputStream fileInputStream=null; File file = new
	 * File("/AIA1/samplebill.pdf"); byte [] byteArrayData = new byte[(int)
	 * file.length()];
	 * 
	 * try {
	 * 
	 * fileInputStream = new FileInputStream(file);
	 * fileInputStream.read(byteArrayData); fileInputStream.close();
	 * }catch(Exception e){ e.printStackTrace(); } return byteArrayData; }
	 */

}
