/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.io.ByteArrayOutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import oracle.ucs.messaging.ws.types.MessageInfo;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.util.MessageDetails;
import com.techm.nms.util.NotificationUtils;
import com.techm.templatemgr.exception.TemplateMgrException;

/**
 * @author sm0015566
 *
 */
public class TextMessageHandlerImpl extends TemplatizedMessageHandlerImpl {

	private static final String CONTENT_TYPE="text/plain; charset=UTF-8";
	/* (non-Javadoc)
	 * @see com.techm.nms.mgmt.MessageHandler#getContentType()
	 */
	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error"); 

	/** Declaration of debug logger responsible for logging error */
	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");
	
	public Object createNotificationBody(MessageDetails msgDetails) throws NotificationMgrGenericException{
		
		//parameter is notification scenario id
		ByteArrayOutputStream bytes;
		long currentTime=System.currentTimeMillis();
		String msgBody=null;
		try {
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+" # Before calling merge in createNotificationBody- Text Message"); 
			//bytes = getHTMLTemplateManager().mergeTemplate(msgDetails.getTemplateFile(),msgDetails.getDataFeed());
			bytes = getHTMLTemplateManager().merge(msgDetails.getTemplateFile(),msgDetails.getDataFeed());
			if(debugLogger.isDebugEnabled())
			{
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in replacing placeholders in TEXT template [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			}
		
			msgBody=bytes.toString();
			//System.out.println("SMS msg:[" + msgBody + "]");
		} catch (TemplateMgrException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorLogger.error("#"+msgDetails.getCorrelator()+"# Merging ["+msgDetails.getDeliveryMode()+"] Template has failed [" + e.getMessage() +"]");
			throw new NotificationMgrGenericException("Merging Template Failed with message[" + e.getMessage() +"]", e);
		}
		
		return msgBody;
	}
	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return CONTENT_TYPE;
	}

	/*public Object setMessageProperties(String implType) {
		// TODO Auto-generated method stub
		Object msgInfo=null;
		if(implType.equals(NotificationUtils.NMS_IMPL_CAT_WS))
			msgInfo = new MessagePropertyHandler4WS().populateMessageProperties();
		
		if(implType.equals(NotificationUtils.NMS_IMPL_CAT_JAVA))
			msgInfo=new MessagePropertyHandler4Java().populateMessageProperties();
		return msgInfo;
		
	
	}*/

	
}
