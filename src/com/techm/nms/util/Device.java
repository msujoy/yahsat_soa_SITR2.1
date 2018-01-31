/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

/**
 * @author sm0015566
 *
 */
public class Device {

	/** Unique name of the device */
	private String deviceName;
	
	/** Unique address for the device(e.g: Mobile No., email address */
	private String deviceAddress;
	
	/** Preferred Channel of communication */
	private String preferredChannel;

	/**
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the deviceAddress
	 */
	public String getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * @param deviceAddress the deviceAddress to set
	 */
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	/**
	 * @return the preferredChannel
	 */
	public String getPreferredChannel() {
		return preferredChannel;
	}

	/**
	 * @param preferredChannel the preferredChannel to set
	 */
	public void setPreferredChannel(String preferredChannel) {
		this.preferredChannel = preferredChannel;
	}
}
