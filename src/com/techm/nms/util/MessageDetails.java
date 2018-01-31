/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author sm0015566
 *
 */


public class MessageDetails {
	
	/** Correlator object to be set from calling BPEL service*/
	 private byte[] correlator;
	 
	
	/**
	 * @return the correlator
	 */
	public byte[] getCorrelator() {
		return correlator;
	}
	/**
	 * @param correlator the correlator to set
	 */
	public void setCorrelator(byte[] correlator) {
		this.correlator = correlator;
	}

	/** Type of recipient(Account/Bill Profile/Service MSISDN/Direct) */
	private ArrayList<String> recipientType;
	
	/** Source system name */
	private String source;
	
	/** Subject of the mail as applicable */
	private String subject;
	
	/** Sender address details */
	private AddressDetails sender;
	
	/** Unique identifier of the notification scenario */
	private int scenarioId;
	
	/** Hierarchical data structure of the information to be replaced in template */
	private HashMap<String,String> dataFeed;
	
	private String templateFile;
	
	private String templateDir;
	
	
	/**
	 * @return the templateDir
	 */
	public String getTemplateDir() {
		return templateDir;
	}
	/**
	 * @param templateDir the templateDir to set
	 */
	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	private String attachmentURI;
	
	private boolean isEmailTemplate;
	
	private boolean isTextTemplate;
	
	private String msgTxt;

	private ArrayList<AddressDetails> recipients;
	
	/** Delivery Mode of notification */
	private String deliveryMode;
	
	/** Identifier of recipient(Account no/Service MSIDN/Bill Profile id/Null for direct */
	private ArrayList<String> recipientId;
	
	/** Asset integration ID, applicable only for Service MSISDN */
	private ArrayList<String> integrationID;
	
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
	/**
	 * @return the recipientId
	 */
	public ArrayList<String> getRecipientId() {
		return recipientId;
	}
	/**
	 * @return the recipientType
	 */
	public ArrayList<String> getRecipientType() {
		return recipientType;
	}
	/**
	 * @param recipientId the recipientId to set
	 */
	public void setRecipientId(ArrayList<String> recipientId) {
		this.recipientId = recipientId;
	}
	/**
	 * @param recipientType the recipientType to set
	 */
	public void setRecipientType(ArrayList<String> recipientType) {
		this.recipientType = recipientType;
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


	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(AddressDetails sender) {
		this.sender = sender;
	}
	
	
	public String getMsgTxt() {
		return msgTxt;
	}
	public void setMsgTxt(String msgTxt) {
		this.msgTxt = msgTxt;
	}
	public boolean isEmailTemplate() {
		return isEmailTemplate;
	}
	public void setEmailTemplate(boolean isEmailTemplate) {
		this.isEmailTemplate = isEmailTemplate;
	}
	public boolean isTextTemplate() {
		return isTextTemplate;
	}
	public void setTextTemplate(boolean isTextTemplate) {
		this.isTextTemplate = isTextTemplate;
	}
	public String getAttachmentURI() {
		return attachmentURI;
	}
	public void setAttachmentURI(String attachmentURI) {
		this.attachmentURI = attachmentURI;
	}
	/**
	 * @return the templateFile
	 */
	public String getTemplateFile() {
		return templateFile;
	}
	/**
	 * @param templateFile the templateFile to set
	 */
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}
	/**
	 * @return the dataFeed
	 */
	public HashMap<String,String> getDataFeed() {
		return dataFeed;
	}
	/**
	 * @param dataFeed the dataFeed to set
	 */
	public void setDataFeed(HashMap<String,String> dataFeed) {
		this.dataFeed = dataFeed;
	}
	/**
	 * @return the scenarioId
	 */
	public int getScenarioId() {
		return scenarioId;
	}
	/**
	 * @param scenarioId the scenarioId to set
	 */
	public void setScenarioId(int scenarioId) {
		this.scenarioId = scenarioId;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the sender
	 */
	public AddressDetails getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender,String addrType) {
		this.sender=new AddressDetails();
		this.sender.setAddress(sender);
		this.sender.setAddressType(addrType);
		
	}
	/**
	 * @return the recipients
	 */
	public ArrayList<AddressDetails> getRecipients() {
		return recipients;
	}
	/**
	 * @param recipients the recipients to set
	 */
	public void setRecipients(ArrayList<AddressDetails> recipients) {
		this.recipients = recipients;
	}
	
	/**
	 * 
	 */
	public MessageDetails() {
		// TODO Auto-generated constructor stub
	}

}
