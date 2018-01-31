/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import oracle.ucs.messaging.ws.types.MessageInfo;
import oracle.ucs.messaging.ws.types.PriorityType;
import oracle.ucs.messaging.ws.types.SessionType;
import oracle.ucs.messaging.ws.types.TrackingType;

/**
 * @author sm0015566
 *
 */
public class MessagePropertyHandler4WS implements MessagePropertyHandler {

	/**
	 * 
	 */
	public MessagePropertyHandler4WS() {
		// TODO Auto-generated constructor stub
	}


	/*public MessageInfo populateMessageProperties() {
		MessageInfo msgInfo=new MessageInfo();
        //TrackingType tracking
        //msgInfo.setTracking(TrackingType.TIGHT_FULL_TRACK);
        msgInfo.setTracking(TrackingType.TIGHT_NOTIFICATION);
        msgInfo.setPriority(PriorityType.HIGH);
        msgInfo.setSession(SessionType.FULL_SESSION);
        //msgInfo.setHighestStatusLevel(StatusType.READ_ACKNOWLEDGEMENT_SUCCESS);
        return msgInfo;
	}*/

}
