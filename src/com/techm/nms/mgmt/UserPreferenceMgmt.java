/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Hashtable;

import javax.xml.ws.BindingProvider;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.util.ConnectionManager;
import com.techm.nms.util.Device;
import com.techm.nms.util.UserDeviceDetails;

import oracle.sdp.messaging.DeliveryType;
import oracle.sdp.messaging.userprefs.ActionOperationType;
import oracle.sdp.messaging.userprefs.DeviceAddress;
import oracle.sdp.messaging.userprefs.FilterCondition;
import oracle.sdp.messaging.userprefs.ObjectAlreadyExistsException;
import oracle.sdp.messaging.userprefs.TermOperationType;
import oracle.sdp.messaging.userprefs.UserDevice;
import oracle.sdp.messaging.userprefs.UserPrefsException;
import oracle.sdp.messaging.userprefs.UserPrefsManager;
import oracle.sdp.messaging.userprefs.UserPrefsServices;
import oracle.sdp.messaging.userprefs.UserRuleSet;
import oracle.ucs.messaging.ws.ClientConstants;
import oracle.ucs.userprefs.UserPrefsServicesFactory;
import oracle.sdp.messaging.userprefs.ObjectAlreadyExistsException;
import oracle.sdp.messaging.userprefs.UserRule;
import com.techm.nms.util.DeliveryPreferenceRule;
/**
 * @author sm0015566
 *
 */
public class UserPreferenceMgmt implements UserPreferenceMgmtInt{

	
	public UserPreferenceMgmt(){
		
	}
	
	public static void main(String args[]){
		
		//Host,Port,username, pwd of application server
		ConnectionManager conn=ConnectionManager.getInstance();
		
		//Populating environment properties
		HashMap prop=conn.populateEnvProps(args[0],args[1],args[2],args[3]);
		System.out.println("Properties[" + prop + "]");
		
		Hashtable props=new Hashtable();
		props.putAll(prop);
		try{
		//Creating user preference object
		UserPrefsServices userPref=conn.getUserPrefServices(props);
		
		//Populating Device Details
		UserPreferenceMgmt pref=new UserPreferenceMgmt();
		
		//Instantiating a particular device
		Device userDevice1=pref.getDevice("deviceName1", "deviceAddress", "Email");
		Device userDevice2=pref.getDevice("deviceName2", "deviceAddress2", "SMS");
		
		
		//Associate devices against an user
		ArrayList<Device> deviceList=new ArrayList<Device>();
		deviceList.add(userDevice1);
		deviceList.add(userDevice2);
		UserDeviceDetails deviceListDetails=pref.setUserDeviceDetails("USER:weblogic",deviceList);
		
		//Creating user's devices
		pref.createDeviceDetails(userPref, deviceListDetails);
		
		
		
		//Setting user preference rule
		
		DeliveryPreferenceRule rule=new DeliveryPreferenceRule();
		rule.setBusinessTerm("Time"); //Need to check whether this business term is there or not.
		rule.setOperation("Between");
		rule.setOperandOne("0800");
		rule.setOperandTwo("1700");
		
		pref.createUserRule(userPref, "USER:weblogic", rule);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Creating user rule
	 */
	private void createUserRule(UserPrefsServices userPref,String userId,DeliveryPreferenceRule delvPref){
		try{
		//Creating user rule set
		UserRuleSet ruleSet=userPref.createUserRuleSet(userId);
		//Creating object of UserRule
		UserRule rule=ruleSet.factoryUserRule();
		//Creating object of FilterCondition
		FilterCondition cond=rule.factoryFilterCondition();

		//Setting the business term on the filter condition
		cond.setFilterTerm(delvPref.getBusinessTerm());
		//Setting the operation that need to be performed(e.g: isEqual,between etc.)
		cond.setTermOperation(TermOperationType.valueOf(delvPref.getBusinessTerm()));
		
		//Setting the operand one
		cond.setOperandOne(delvPref.getOperandOne());
		//Setting the operand two(optional)
		cond.setOperandTwo(delvPref.getOperandTwo());
		//This is optional step of setting actions against a rule. If no action is defined default action
		rule.setActionOperation(ActionOperationType.BROADCAST);
		
		
		}catch(UserPrefsException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to populate device object
	 * @param deviceName Unique name of the device
	 * @param deviceAddress Unique address of the device
	 * @param channel Channel to be associated with a device
	 * @return User Device
	 */
	public Device getDevice(String deviceName,String deviceAddress,String channel){
		Device userDevice=new Device();
		userDevice.setDeviceAddress(getDeliveryType(channel)+deviceAddress);
		userDevice.setDeviceName(deviceName);
		//userDevice.setPreferredChannel(channel);
		return userDevice;
	}
	
	/**
	 * Used to associate set of devices against an user
	 * @param userId Unique user identifier
	 * @param ArrayList<Device> List of devices to be associated with a particular user
	 * @return User Device Details
	 */
	
	private UserDeviceDetails setUserDeviceDetails(String userId,ArrayList<Device> deviceList){
	
		UserDeviceDetails deviceDetails=new UserDeviceDetails();
		deviceDetails.setUserId(userId);
		deviceDetails.setDeviceList(deviceList);
		return deviceDetails;
	}
	
	
		
	/**
	 * Creating device details for a particular customer/user 
	 * @param userPref
	 * @param userId - Unique customer identifier configured in iDM
	 * @param deviceName - Unique name of the device customer wants to register
	 * @param deviceAddress - Device on which customer prefers to accept notification(e.g: MSISDN,email)
	 * @param channel - Channel on which customer prefers to accept notification(e.g: SMS,Email etc.)
	 * @return
	 * @throws IllegalArgumentException 
	 * @throws UserPrefsException 
	 * @throws ObjectAlreadyExistsException 
	 */
	private void createDeviceDetails(UserPrefsServices userPref,UserDeviceDetails deviceDetails)throws NotificationMgrGenericException{
		DeviceAddress deviceAddr=null;
		UserDevice device=null;
		try{
			ArrayList<Device> deviceList=deviceDetails.getDeviceList();
			for(int i=0;i<deviceList.size();i++){
				device=userPref.createUserDevice(deviceDetails.getUserId(),deviceList.get(i).getDeviceName());
				userPref.save(device);
				deviceAddr=userPref.createDeviceAddress(device, deviceList.get(i).getDeviceAddress());
				userPref.save(deviceAddr);
			}
		//creating unique device with unique user
		
		//Setting device address(e.g: mobile no., email address)
		
		
		//deviceAddr.setDeliveryType(getDeliveryType(deviceDetails.getChannel()));
		}catch(ObjectAlreadyExistsException e){
			e.printStackTrace();
			throw new NotificationMgrGenericException("User[" + deviceDetails.getUserId() + "] Already Exists",e);
		}catch (UserPrefsException e){
			e.printStackTrace();
			throw new NotificationMgrGenericException("User Preference Exception",e);
		}catch(IllegalArgumentException e){
			throw new NotificationMgrGenericException("Argument is wrong",e);
		}
		
	}
	
		
	/**
	 * Returns a delivery type of the notification channel
	 * @param channel
	 * @return
	 */
	private DeliveryType getDeliveryType(String channel){
		Object deliveryType=null;
		if(channel.equals("Email"))
				deliveryType=DeliveryType.EMAIL;
		if (channel.equals("SMS"))
				deliveryType=DeliveryType.SMS;
		return (DeliveryType)deliveryType;
		}
	
	
	
	
	
	
	
	
	
	
	

}
