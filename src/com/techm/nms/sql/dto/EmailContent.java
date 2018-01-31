/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dto;

import java.io.ByteArrayOutputStream;

import javax.mail.internet.MimeMultipart;

/**
 * @author sm0015566
 *
 */
public class EmailContent {
	
	/** Represents mime multipart of email content(including attachment) */
	private MimeMultipart emailContent;
	
	/** Represents subject of email */
	private String emailSubject;
	
	/** Represents content of email(Without attachment) as byte array */
	private ByteArrayOutputStream emailContentAsByteArray;
	
	/** Represents content of email attachment as byte array */
	private ByteArrayOutputStream attachmentAsByteArray;
	
	/** Represents email content and attachment as byte array */
	private ByteArrayOutputStream wholeEmailAsByteArray;

	/**
	 * @return the emailContent
	 */
	public MimeMultipart getEmailContent() {
		return emailContent;
	}

	/**
	 * @param emailContent the emailContent to set
	 */
	public void setEmailContent(MimeMultipart emailContent) {
		this.emailContent = emailContent;
	}

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
	 * @return the emailContentAsByteArray
	 */
	public ByteArrayOutputStream getEmailContentAsByteArray() {
		return emailContentAsByteArray;
	}

	/**
	 * @param emailContentAsByteArray the emailContentAsByteArray to set
	 */
	public void setEmailContentAsByteArray(ByteArrayOutputStream emailContentAsByteArray) {
		this.emailContentAsByteArray = emailContentAsByteArray;
	}

	/**
	 * @return the attachmentAsByteArray
	 */
	public ByteArrayOutputStream getAttachmentAsByteArray() {
		return attachmentAsByteArray;
	}

	/**
	 * @param attachmentAsByteArray the attachmentAsByteArray to set
	 */
	public void setAttachmentAsByteArray(ByteArrayOutputStream attachmentAsByteArray) {
		this.attachmentAsByteArray = attachmentAsByteArray;
	}

	/**
	 * @return the wholeEmailAsByteArray
	 */
	public ByteArrayOutputStream getWholeEmailAsByteArray() {
		return wholeEmailAsByteArray;
	}

	/**
	 * @param wholeEmailAsByteArray the wholeEmailAsByteArray to set
	 */
	public void setWholeEmailAsByteArray(ByteArrayOutputStream wholeEmailAsByteArray) {
		this.wholeEmailAsByteArray = wholeEmailAsByteArray;
	}

}
