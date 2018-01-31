/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import javax.mail.internet.MimeMultipart;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.util.MessageDetails;
import com.techm.nms.util.MessageInformation;

import oracle.sdp.messaging.*;
/**
 * @author sm0015566
 *
 */
public interface NotificationManager {

	
	public void sendMessage(MessageDetails msgDetails)throws NotificationMgrGenericException;
	
	//Method added for Yahsat(Sujoy on 7th Aug,2017)
	
	public NotificationHistory sendNotification(MessageDetails msgDetails)throws NotificationMgrGenericException;
	
	//public Object addRecipients();
	
	public Object setNotificationProperties(MessageDetails msgDetails)throws NotificationMgrGenericException;
}
