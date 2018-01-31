/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import java.util.ArrayList;

/**
 * @author sm0015566
 *
 */
public class UserDeviceDetails {

	/** Unique identifier of the customer/user as registered*/
	private String userId;
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** Collection to hold multiple devices against an user */
	private ArrayList<Device> deviceList;
	
	/**
	 * @return the deviceList
	 */
	public ArrayList<Device> getDeviceList() {
		return deviceList;
	}

	/**
	 * @param deviceList the deviceList to set
	 */
	public void setDeviceList(ArrayList<Device> deviceList) {
		this.deviceList = deviceList;
	}

	
	
}
