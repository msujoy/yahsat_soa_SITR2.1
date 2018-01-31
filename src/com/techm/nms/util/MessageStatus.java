/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import java.text.SimpleDateFormat;

/**
 * @author sm0015566
 *
 */
public class MessageStatus {

	private byte[] correlator;
	
	private MessageInformation msgInfo;
	
	private MessageDetails msgDetails;
	
	
	private String gwMessageId;
	
		
	private String status;
	
	private String statusDesc;

	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc() {
		return statusDesc;
	}

	/**
	 * @param statusDesc the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	private String date;
	
	/**
	 * @return the msgDetails
	 */
	public MessageDetails getMsgDetails() {
		return msgDetails;
	}

	/**
	 * @param msgDetails the msgDetails to set
	 */
	public void setMsgDetails(MessageDetails msgDetails) {
		this.msgDetails = msgDetails;
	}

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
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	
	/**
	 * @return the gwMessageId
	 */
	public String getGwMessageId() {
		return gwMessageId;
	}

	/**
	 * @param gwMessageId the gwMessageId to set
	 */
	public void setGwMessageId(String gwMessageId) {
		this.gwMessageId = gwMessageId;
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
	 * @return the msgInfo
	 */
	public MessageInformation getMsgInfo() {
		return msgInfo;
	}

	/**
	 * @param msgInfo the msgInfo to set
	 */
	public void setMsgInfo(MessageInformation msgInfo) {
		this.msgInfo = msgInfo;
	}

}
