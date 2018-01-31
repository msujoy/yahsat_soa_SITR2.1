/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import oracle.ucs.messaging.ws.types.MessageInfo;

import com.techm.nms.util.MessageDetails;


/**
 * @author sm0015566
 *
 */
public class NonTemplatizedMessageHandler implements MessageHandler {

	private static final String CONTENT_TYPE="text/plain; charset=UTF-8";
	//private static final String CONTENT_TYPE="application/x-www-form-urlencoded; charset=UTF-8";
	public String createNotificationBody(MessageDetails msgDetails){
				
		return msgDetails.getMsgTxt();
	}
	
	public String getContentType(){
		return CONTENT_TYPE;
	}

	/* (non-Javadoc)
	 * @see com.techm.nms.mgmt.MessageHandler#setMessageProperties()
	 */
	
	/*public MessageInfo setMessageProperties(String implType) {
		// TODO Auto-generated method stub
		return null;
	}*/

	/* (non-Javadoc)
	 * @see com.techm.nms.mgmt.MessageHandler#setTemplateDir(java.lang.String)
	 */
	@Override 
	public void setTemplateDir(String templateDir) {
		// TODO Auto-generated method stub
		templateDir=null;
	}
}
