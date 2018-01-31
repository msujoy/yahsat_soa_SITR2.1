/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import org.w3c.dom.Document;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.sql.dto.NotificationHistory;

/**
 * @author sm0015566
 *
 */
public interface NotificationManagerDBInteraction {

	/** Used to create notification history record */
	public void createNotificationHistory(NotificationHistory history) throws NotificationMgrSQLException;
	
	/** Used to retrieve notification history */
	public Document retrieveNotificationHistory(Document doc) throws NotificationMgrGenericException;
	
	/** Used to synchronize customer preference in notification data store */
	public void syncCustomerPreference(Document doc) throws NotificationMgrGenericException;
}
