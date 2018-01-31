/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import oracle.sdp.messaging.MessageInfo;
import oracle.sdp.messaging.MessageSessionType;
import oracle.sdp.messaging.MessagingFactory;
import oracle.ucs.messaging.ws.types.PriorityType;
import oracle.ucs.messaging.ws.types.SessionType;
import oracle.ucs.messaging.ws.types.TrackingType;

/**
 * @author sm0015566
 *
 */
public class MessagePropertyHandler4Java implements MessagePropertyHandler {

	/**
	 * 
	 */
	public MessagePropertyHandler4Java() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.techm.nms.mgmt.MessagePropertyHandler#populateMessageProperties()
	 */
	
	/*public oracle.sdp.messaging.MessageInfo populateMessageProperties() {
		// TODO Auto-generated method stub
		MessageInfo msgInfo=MessagingFactory.createMessageInfo();
		
		msgInfo.setSession(MessageSessionType.INBOUND_SESSION);
		
		msgInfo.sesetTracking(TrackingType.TIGHT_NOTIFICATION);
        msgInfo.setPriority(PriorityType.HIGH);
        msgInfo.setSession(SessionType.FULL_SESSION);
		
		return msgInfo;
	}*/

}
