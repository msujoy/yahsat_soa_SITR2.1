/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dto;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author sm0015566
 *
 */
public class NotificationHistory {
	
	private int notfScenarioId;

	private ArrayList<String> recipientID;
	
	private ArrayList<String> recipientType;
	
	private ArrayList<String> integrationID;
	
	private byte[] emailContentByteArray;
	
	public byte[] getEmailContentByteArray() {
		return emailContentByteArray;
	}





	public void setEmailContentByteArray(byte[] emailContentByteArray) {
		this.emailContentByteArray = emailContentByteArray;
	}





	/**
	 * @return the integrationID
	 */
	public ArrayList<String> getIntegrationID() {
		return integrationID;
	}





	/**
	 * @param integrationID the integrationID to set
	 */
	public void setIntegrationID(ArrayList<String> integrationID) {
		this.integrationID = integrationID;
	}



	private String deliveryMode;
	
	private ArrayList<String> deliveryAddr;
	
	private String messageId;
	
	private String sourceName;
	
	private String msgContent;
	
	private String emailSubject;
	
	private String status;
	
	/** Represents the email body content */
	private Object emailContent;
	
	/** Represents the email attachment */
	private Object emailAttachmentContent;
	
	/**
	 * @return the emailAttachmentContent
	 */
	public Object getEmailAttachmentContent() {
		return emailAttachmentContent;
	}





	/**
	 * @param emailAttachmentContent the emailAttachmentContent to set
	 */
	public void setEmailAttachmentContent(Object emailAttachmentContent) {
		this.emailAttachmentContent = emailAttachmentContent;
	}





	public Object getEmailContent() {
		return emailContent;
	}





	public void setEmailContent(Object emailContent) {
		this.emailContent = emailContent;
	}



	private String statusDescription;

	
	public NotificationHistory(int notfScenarioId, ArrayList<String> recipientID, ArrayList<String> recipientType, ArrayList<String> integrationID, String deliveryMode, ArrayList<String> deliveryAddr, String messageId, String sourceName, String msgContent, String emailSubject, Object emailContent,Object emailAttachmentContent,String status, String statusDescription){
		this.notfScenarioId=notfScenarioId;
		this.recipientID=recipientID;
		this.recipientType=recipientType;
		this.integrationID=integrationID;
		this.deliveryMode=deliveryMode;
		this.deliveryAddr=deliveryAddr;
		this.messageId=messageId;
		this.sourceName=sourceName;
		this.msgContent=msgContent;
		this.emailSubject=emailSubject;
		this.emailContent=emailContent;
		//this.emailContentByteArray=emailContentBytes;
		this.emailAttachmentContent=emailAttachmentContent;
		this.status=status;
		this.statusDescription=statusDescription;
		
	}
	
	
	


	/**
	 * @return the deliveryMode
	 */
	public String getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * @param deliveryMode the deliveryMode to set
	 */
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	
	
	//private Date msgDelvTime;
	
	
	
	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject() {
		return emailSubject;
	}

	/**
	 * @param emailSubject the emailSubject to set
	 */
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	/**
	 * @return the recipientID
	 */
	public ArrayList<String> getRecipientID() {
		return recipientID;
	}

	/**
	 * @param recipientID the recipientID to set
	 */
	public void setRecipientID(ArrayList<String> recipientID) {
		this.recipientID = recipientID;
	}

	/**
	 * @return the recipientType
	 */
	public ArrayList<String> getRecipientType() {
		return recipientType;
	}

	/**
	 * @param recipientType the recipientType to set
	 */
	public void setRecipientType(ArrayList<String> recipientType) {
		this.recipientType = recipientType;
	}

	/**
	 * @return the deliveryAddr
	 */
	public ArrayList<String> getDeliveryAddr() {
		return deliveryAddr;
	}

	/**
	 * @param deliveryAddr the deliveryAddr to set
	 */
	public void setDeliveryAddr(ArrayList<String> deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	
	
	/**
	 * @return the notfScenarioId
	 */
	public int getNotfScenarioId() {
		return notfScenarioId;
	}

	/**
	 * @param notfScenarioId the notfScenarioId to set
	 */
	public void setNotfScenarioId(int notfScenarioId) {
		this.notfScenarioId = notfScenarioId;
	}


	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @return the msgContent
	 */
	public String getMsgContent() {
		return msgContent;
	}

	/**
	 * @param msgContent the msgContent to set
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}



	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}



	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	
}
