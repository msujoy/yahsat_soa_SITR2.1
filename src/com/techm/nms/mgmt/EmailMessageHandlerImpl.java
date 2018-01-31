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

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import oracle.sdp.messaging.Message;
import oracle.ucs.messaging.ws.types.MessageInfo;
import oracle.ucs.messaging.ws.types.PriorityType;
import oracle.ucs.messaging.ws.types.SessionType;
import oracle.ucs.messaging.ws.types.StatusType;
import oracle.ucs.messaging.ws.types.TrackingType;

import com.techm.dms.exception.ConnectionException;
import com.techm.dms.exception.DMSInteractionException;
import com.techm.dms.mgmt.DMSInteractionManager;
import com.techm.dms.mgmt.DMSInteractionManagerImpl;
import com.techm.dms.utils.DMSRequest;
import com.techm.dms.utils.DMSResponse;
import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.sql.dto.EmailContent;
import com.techm.nms.util.MessageDetails;
import com.techm.nms.util.NotificationUtils;
import com.techm.templatemgr.exception.TemplateMgrException;
import com.techm.templatemgr.mgmt.HTMLTemplateManager;
import com.techm.templatemgr.util.Util;

/**
 * @author sm0015566
 *
 */
public class EmailMessageHandlerImpl extends TemplatizedMessageHandlerImpl {

		
	private static final String CONTENT_TYPE="multipart/mixed";
	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error"); 

	/** Declaration of debug logger responsible for logging error */
	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");
	
	/**
	 * Method is used to populate the email content with/without any mime multi part(i.e attachment).
	 * @param msgDetails
	 * @return ByteArrayOutputStream of the email content that need to be sent to caller.
	 * @throws NotificationMgrGenericException
	 */
	
	public EmailContent createEmailBody(MessageDetails msgDetails) throws NotificationMgrGenericException{

		ByteArrayOutputStream bytes;
		EmailContent emailContent = new EmailContent();
		long currentTime=System.currentTimeMillis();
		long initialTime=currentTime;
		MimeMultipart mp=new MimeMultipart("mixed");
		try {
			//bytes = getHTMLTemplateManager().mergeTemplate(msgDetails.getTemplateFile(),msgDetails.getDataFeed());
			
			bytes = getHTMLTemplateManager().merge(msgDetails.getTemplateFile(),msgDetails.getDataFeed());
			emailContent.setEmailContentAsByteArray(bytes); //Setting email body content
			if(debugLogger.isDebugEnabled()){
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in replacing placeholders in EMAIL template [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			}
		
		} catch (TemplateMgrException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Merging ["+msgDetails.getDeliveryMode()+"] Template has failed [" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Merging Template Failed with message[" + e.getMessage() +"]", e);
		}
		try {
			currentTime=System.currentTimeMillis();
			// Adding the body of the EMAIL notification
			mp.addBodyPart(createBodyPart(bytes,NotificationUtils.getContentType("EMAIL")));
			if(debugLogger.isDebugEnabled()){
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in creating email body [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			}
		} catch (MessagingException e) {
			
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Creating email body Failed with message[" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Creating email body Failed with message[" + e.getMessage() +"]", e);
		}
		//Retrieval of attachment- Added on 31st Aug,2017
		//Setting the MimeMultiPart content type
		
		if(msgDetails.getAttachmentURI()!=null && !msgDetails.getAttachmentURI().equals("")){
			//Adding the attachment
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Attachment URI [" + msgDetails.getAttachmentURI() + "is present");
			MimeBodyPart emailAttachment=getAttachmentLocal(msgDetails,emailContent);
			if(emailAttachment!=null){
				try {
					mp.addBodyPart(emailAttachment);
					if(debugLogger.isDebugEnabled()){
						debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in adding attachment with [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
					}
				} catch (MessagingException e) {
					errorLogger.error("#"+msgDetails.getCorrelator()+"# Populating email attachment failed with message[" + e.getMessage() +"]");
					throw new NotificationMgrGenericException("Populating email attachment failed with message[" + e.getMessage() +"]", e);
				}
			}
		}
		
		//adding the whole email content(body+attachment)
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		try {
			mp.writeTo(bos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			errorLogger.error("#"+msgDetails.getCorrelator()+"# adding email attachment failed with message[" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Adding email attachment failed with message[" + e.getMessage() +"]", e);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		emailContent.setWholeEmailAsByteArray(bos);
		emailContent.setEmailContent(mp); //MimeMultiPart content of email+attachment
		return emailContent;
		
	}
	
	
	
	public MimeMultipart createNotificationBody(MessageDetails msgDetails) throws NotificationMgrGenericException{
	
		//parameter is notification scenario id
		ByteArrayOutputStream bytes;
		long currentTime=System.currentTimeMillis();
		long initialTime=currentTime;
		try {
			//bytes = getHTMLTemplateManager().mergeTemplate(msgDetails.getTemplateFile(),msgDetails.getDataFeed());
			
			bytes = getHTMLTemplateManager().merge(msgDetails.getTemplateFile(),msgDetails.getDataFeed());
			if(debugLogger.isDebugEnabled())
			{
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in replacing placeholders in EMAIL template [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			}
		
		} catch (TemplateMgrException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Merging ["+msgDetails.getDeliveryMode()+"] Template has failed [" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Merging Template Failed with message[" + e.getMessage() +"]", e);
		}
		
		//Setting the MimeMultiPart content type
		MimeMultipart mp=new MimeMultipart("mixed");
		
		try {
			currentTime=System.currentTimeMillis();
			// Adding the body of the EMAIL notification
			mp.addBodyPart(createBodyPart(bytes,NotificationUtils.getContentType("EMAIL")));
			if(debugLogger.isDebugEnabled())
			{
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in creating email body [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Creating email body Failed with message[" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Creating email body Failed with message[" + e.getMessage() +"]", e);
		}
		
		currentTime=System.currentTimeMillis();
		//Adding the attachment
		MimeBodyPart emailAttachment=getAttachment(msgDetails);
		if(debugLogger.isDebugEnabled())
		{
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in getting attachment [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
		}
		if(emailAttachment!=null)
			try {
				
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Creating multi-part notification attachment");
				mp.addBodyPart(emailAttachment);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				errorLogger.error("#"+msgDetails.getCorrelator()+"# Populating email attachment failed with message[" + e.getMessage() +"]");
				throw new NotificationMgrGenericException("Populating email attachment failed with message[" + e.getMessage() +"]", e);
			}
		//mp.addBodyPart(txtBody);
		
		if(debugLogger.isDebugEnabled())
		{
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time taken in creating notification body [" + String.valueOf(System.currentTimeMillis()-initialTime) + "]");
		} 
		return mp;
	}
	
	/**
	 * Method used to retrieve the attachment from local disk
	 * @param details
	 * @return
	 */
	private MimeBodyPart getAttachmentLocal(MessageDetails msgDetails,EmailContent emailContent) throws NotificationMgrGenericException{
		
		MimeBodyPart emailAttachment=null;
		if(msgDetails.getAttachmentURI()!=null && !msgDetails.getAttachmentURI().equals("")){
			
			byte attachmentContent[]=getFileContent(msgDetails.getAttachmentURI());
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			try {
				baos.write(attachmentContent);
				emailContent.setAttachmentAsByteArray(baos);// Setting the attachment content
			} catch (IOException e) {
				// TODO Auto-generated catch block
				errorLogger.error("#"+msgDetails.getCorrelator()+"# Writing attachment content failed with message[" + e.getMessage() +"]");
			}
			emailAttachment=createBodyPart(attachmentContent, NotificationUtils.BOLETO_CONTENT_TYPE);
			try {
				emailAttachment.setFileName(getFilename(msgDetails.getAttachmentURI()));
				if(debugLogger.isDebugEnabled()){
					debugLogger.debug("#"+msgDetails.getCorrelator()+"# Attachment added in email ");
				}
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				errorLogger.error("#"+msgDetails.getCorrelator()+"# setting filename of attachment failed with message[" + e.getMessage() +"]");
				throw new NotificationMgrGenericException("setting filename of attachment failed with message[" + e.getMessage() +"]", e);
			}
		}
		return emailAttachment;
	}
	
	private String getFilename(String fileLoc){
		File attachment=new File(fileLoc);
		return attachment.getName();
	}
	private byte[] getFileContent(String fileLoc) throws NotificationMgrGenericException{
		
		File attachment=new File(fileLoc);
		byte content[]=new byte[(int) attachment.length()];
		FileInputStream fin=null;
		try {
			fin=new FileInputStream(attachment);
			
			try {
				fin.read(content);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new NotificationMgrGenericException("IOException occured during reading of file[" + fileLoc + "] with message[" + e.getMessage() + "]", e);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException("FileNotFoundException occured during reading of file[" + fileLoc + "] with message[" + e.getMessage() + "]", e);
		}finally{
			try{
				if(fin!=null)
					fin.close();
			}catch(IOException e){
				throw new NotificationMgrGenericException("FileInputStream during reading of file[" + fileLoc + "] could not be closed with message[" + e.getMessage() + "]", e);
			}
		}
		return content;
	}
	
	/**
	 * Method used to retrieve attachment from Document Management System
	 * @param msgDetails
	 * @return
	 * @throws NotificationMgrGenericException
	 */
	private MimeBodyPart getAttachment(MessageDetails msgDetails) throws NotificationMgrGenericException{
		
		MimeBodyPart emailAttachment=null;
		DMSResponse response=null;
		//Retrieving attachment(TODO: need to call only when attachment is present)
		if(msgDetails.getAttachmentURI()!=null && !msgDetails.getAttachmentURI().equals("")){
		//ByteArrayOutputStream attachmentBytes=new ByteArrayOutputStream();
		try {
			
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Calling DMS to Fetch attachment with URI[" + msgDetails.getAttachmentURI() + "]");
			response=getAttachmentFromDMS(msgDetails.getAttachmentURI());
			//response=getAttachmentHardcoded(msgDetails.getAttachmentURI());
			//debugLogger.debug("Byte Code from DMS ["+response.getBinaryData()+"]");
			
			//Temporary File Write into server START
			/*File someFile = new File("/aiaapp/oracle/Oracle/Middleware/user_projects/domains/aiacit2d_domain/servers/AIACIT2D_SOA_Server/logs/nms/"+response.getFileName());
	        FileOutputStream fos = new FileOutputStream(someFile);
	        fos.write(response.getBinaryData());
	        fos.flush();
	        fos.close();*/
	        //Temporary File Write into server END
			//attachmentBytes.write(response.getBinaryData());
			
			//debugLogger.debug("Attachment Byte for the file recieved from DMS["+response.getBinaryData()+"]");
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# "+response.getFileName() +" has been successfully attached with Email body");
			
		} /*catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("Writing attachment bytes to output stream has failed");
			throw new NotificationMgrGenericException("Writing attachment bytes to output stream failed",e);
		}*/ catch (NotificationMgrGenericException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Retrieval of attachment from DMS has failed ["+e.getMessage()+"]");
			throw new NotificationMgrGenericException("Retrieval of attachment from DMS has failed [" + e.getMessage() + "]",e);
		}
		
		//Adding Attachment Body part
		
		debugLogger.debug("#"+msgDetails.getCorrelator()+"# Creating attachment body part with bytes received from DMS");
		emailAttachment=createBodyPart(response.getBinaryData(), response.getContentType());
		try {
			emailAttachment.setFileName(response.getFileName());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Setting attachment filename has failed [" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Setting attachment filename Failed with message[" + e.getMessage() +"]", e);
		}//Attachment File Name
		}
		return emailAttachment;
		
	}
	/**
	 * Used to create notification attachment
	 * @param bytes ByteArrayOutputStream of merged template
	 * @param notificationType Type of the notification(EMAIL)
	 * @return content of the notification attachment
	 */
	
	
	protected DMSResponse getAttachmentFromDMS(String attachmentURI) throws NotificationMgrGenericException{
		//debugLogger.debug("System is trying to retrieve attachment from Plone for ["+attachmentURI+"]");
		DMSRequest dmsRequest = populateDMSRequest(attachmentURI);
		//DMSInteractionManager dmsMgr = new DMSInteractionManagerImpl(dmsRequest);
		//System.out.println("*********Start");
		DMSInteractionManager dmsMgr = DMSInteractionManagerImpl.getInstance();
		DMSResponse response = null;
		
		try {
				//System.out.println("*************Trying to fetch....");
				response=dmsMgr.retrieveDocument(dmsRequest);
				//System.out.println("*************Document Fetched successfully");
				//debugLogger.debug(response.getFileName()+" has been successfully received from DMS");
				//debugLogger.debug("After retrieving attachment from DMS bytes[" + response.getBinaryData()+"]");
			}catch (ConnectionException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				errorLogger.error("Connection with DMS has failed ["+e.getMessage()+"]");
				throw new NotificationMgrGenericException("Connection with DMS has failed [" + e.getMessage() + "]",e);
			}catch(DMSInteractionException e){
				errorLogger.error("Interaction with DMS has failed ["+e.getMessage()+"]");
				throw new NotificationMgrGenericException("Interaction with DMS has failed [" + e.getMessage() + "]",e);
			}
		return response;
	}
	
	/*protected DMSResponse getAttachmentHardcoded(String attachmentURI) throws NotificationMgrGenericException{
		
			DMSResponse response = null;
			byte[] binaryData=null;
				System.out.println("*************Trying to fetch....");
				response.setContentType("application/pdf");
				response.setFileName(attachmentURI);
				response.setBinaryData(binaryData);
				System.out.println("*************Document Fetched successfully");
				
		return response;
	}*/
	
	private DMSRequest populateDMSRequest(String attachmentURI){
		DMSRequest request=new DMSRequest();
		request.setConnectMethod("GET");
		//request.setHostname(dmsHost);
		//request.setPort(dmsPort);
		request.setFile(attachmentURI);
		request.setUri("?path="+attachmentURI+"&complete=yes");
		request.setParent_path("path");
        request.setComplete("complete");

		//request.setComplete("yes");
		
		return request;
	}
	
	public String getContentType(){
		return CONTENT_TYPE;
	}
	
	/**
	 * Method used to set Message Properties like priority, importance etc.
	 */
	/*public Object setMessageProperties(String implType){
        
		Object msgInfo=null;
		if(implType.equals(NotificationUtils.NMS_IMPL_CAT_WS))
			msgInfo = new MessagePropertyHandler4WS().populateMessageProperties();
		
		if(implType.equals(NotificationUtils.NMS_IMPL_CAT_JAVA))
			msgInfo=new MessagePropertyHandler4Java().populateMessageProperties();
		return msgInfo;
		
  }*/


}
