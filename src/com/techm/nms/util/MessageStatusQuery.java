/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import java.util.Date;
import java.util.List;

/**
 * @author sm0015566
 *
 */
public class MessageStatusQuery {

	/** List of recipients to whom the notification has been sent */
	private List<AddressDetails> recipientAddress;
	
	/**
	 * @return the recipientAddress
	 */
	public List<AddressDetails> getRecipientAddress() {
		return recipientAddress;
	}

	/**
	 * @param recipientAddress the recipientAddress to set
	 */
	public void setRecipientAddress(List<AddressDetails> recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	private Date startTime;
	
	private Date endTime;
}
