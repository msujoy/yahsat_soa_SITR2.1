/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import oracle.ucs.messaging.ws.types.MessageInfo;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.util.MessageDetails;

/**
 * @author sm0015566
 *
 */
public interface MessageHandler {

	public Object createNotificationBody(MessageDetails msgDetails) throws NotificationMgrGenericException;
	
	public String getContentType();
	
	//public Object setMessageProperties(String cat);
	
	public void setTemplateDir(String templateDir);
}
