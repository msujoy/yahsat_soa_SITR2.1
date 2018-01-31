/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dto;



/**
 * @author sm0015566
 *
 */
public class UpdateNotificationHistory {
	
	private String messageId;
	//private String emailSubject;
	//private String messageContent;
	private String status;
	private String statusDescription;
	
	
	/**
	 * @return the emailSubject
	 */
	/*public String getEmailSubject() {
		return emailSubject;
	}*/

	/**
	 * @param emailSubject the emailSubject to set
	 */
	/*public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}*/

	/**
	 * @return the messageContent
	 */
	/*public String getMessageContent() {
		return messageContent;
	}*/

	/**
	 * @param messageContent the messageContent to set
	 */
	/*public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}*/

	
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

	

	
	/*public UpdateNotificationHistory(String messageId, String emailSubject, String messageContent, String status, String statusDescription){
		
		this.messageId=messageId;
		this.emailSubject=emailSubject;
		this.messageContent=messageContent;
		this.status=status;
		this.statusDescription=statusDescription;
		
	}*/
	
	public UpdateNotificationHistory(String messageId, String status, String statusDescription){
		
		this.messageId=messageId;
		this.status=status;
		this.statusDescription=statusDescription;
		
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
