/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.listener;

import java.io.Serializable;

import oracle.sdp.messaging.Listener;
import oracle.sdp.messaging.ListenerException;
import oracle.sdp.messaging.Message;
import oracle.sdp.messaging.Status;
import org.apache.log4j.Logger;

import org.springframework.util.concurrent.ListenableFuture;

import com.techm.nms.mgmt.NotificationManagerDBInteractionImpl;
import com.techm.nms.sql.dto.UpdateNotificationHistory;
import com.techm.nms.util.NotificationUtils;
import com.techm.nms.exceptions.NotificationMgrSQLException;

/**
 * @author sm0015566
 *
 */

public class NotificationStatusListener4Java implements Listener {

	/**
	 * 
	 */
	private static final Logger errorLogger = Logger.getLogger("NotificationStatusListener4Java");
	private static final Logger debugLogger = Logger.getLogger("NotificationStatusListener4Java");
	
	public NotificationStatusListener4Java() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see oracle.sdp.messaging.Listener#onMessage(oracle.sdp.messaging.Message, java.io.Serializable)
	 */
	@Override
	public void onMessage(Message arg0, Serializable arg1)
			throws ListenerException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see oracle.sdp.messaging.Listener#onStatus(oracle.sdp.messaging.Status, java.io.Serializable)
	 */
	@Override
	public void onStatus(Status status, Serializable correlator)
			throws ListenerException {
		
		//NotificationUtils.printPlatformDetails(); //TODO : This need to be removed after debugging
		
		//NotificationUtils.printMessageStatus(status, correlator);
			
			
		
		
		// TODO Auto-generated method stub
				NotificationManagerDBInteractionImpl nmsDB = NotificationManagerDBInteractionImpl
						.getInstance();
				UpdateNotificationHistory updateNotificationHistory = new UpdateNotificationHistory(status.getMessageId(), status.getType().name(), status.getContent());
				
				try {
								
					nmsDB.updateNotificationHistory(updateNotificationHistory);
					//System.out.println("*********Insertion into History table from Status Listner END");
				} catch (NotificationMgrSQLException e) {

					/*errorLogger.error("===== Received message with correlator id :[" + correlator + "] ===========");
					errorLogger.error(" == Updating Notification History with details ==== ");
					errorLogger.error(" == Message Id: " + status.getMessageId() + " ==== ");
					errorLogger.error(" == Delivery Status : " + status.getType().name() + " ==== ");*/
					NotificationUtils.printMessageStatus(status, correlator);
					errorLogger.error("# Updating record into Notification History failed due to ["+ e.getMessage() + "]");
					
				}
	}
	

}
