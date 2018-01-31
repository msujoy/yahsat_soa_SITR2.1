/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import oracle.jrf.PortabilityLayerException;
import oracle.sdp.messaging.Address;
import oracle.sdp.messaging.Message;
import oracle.sdp.messaging.MessagePriorityType;
import oracle.sdp.messaging.MessageSessionType;
import oracle.sdp.messaging.MessagingClientFactory;
import oracle.sdp.messaging.MessagingFactory;
import oracle.sdpinternal.messaging.platform.MessagingPlatform;
import oracle.sdpinternal.messaging.platform.MessagingPlatformFactory;
import oracle.ucs.messaging.ws.types.MessageInfo;
import oracle.ucs.messaging.ws.types.PriorityType;
import oracle.ucs.messaging.ws.types.SessionType;
import oracle.ucs.messaging.ws.types.TrackingType;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.listener.NotificationStatusListener4Java;
import com.techm.nms.sql.dto.EmailContent;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.util.AddressDetails;
import com.techm.nms.util.ConnectionManager;
import com.techm.nms.util.CorrelationGenerator;
import com.techm.nms.util.MessageDetails;
import com.techm.nms.util.MessageInformation;
import com.techm.nms.util.NotificationUtils;
import com.techm.templatemgr.exception.TemplateMgrException;
import com.techm.templatemgr.mgmt.HTMLTemplateManager;
import com.techm.templatemgr.mgmt.TemplateManager;
import com.techm.templatemgr.mgmt.TemplateManagerImpl;
import com.techm.templatemgr.util.Util;

import oracle.sdp.messaging.Listener;
/**
 * Core Notification Management Class for sending any notifications
 * @author Sujoy Mondal
 *
 * Version : 4.0 - Primary Technical functionalities-
 * 					1. Singleton class 
 * 					2. Client of the class will call getInstance()
 * 				    3. sendMessage() method is having ONLY one parameter(i.e MessageDetails POJO)
 *                  4. Removed Listener object from SDP client send() method- Performance reason 
 */
public class NotificationManagerJavaImpl extends NotificationManagerImpl {

	/**
	 * Commented on 3rd May,2016 for instantiating NotificationManagerJavaImpl for every BPEL instance
	 */  
	private static oracle.sdp.messaging.MessagingClient client;
	
	private static NotificationStatusListener4Java listener;

	private static NotificationManagerJavaImpl instance;
	

	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error");

	/** Declaration of debug logger responsible for logging error */

	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	
	/**TODO - Commented on 3rd May,2016 for instantiating NotificationManagerJavaImpl for every BPEL instance
	 *   
	 * @param host
	 * @param port
	 * @param username
	 * @param pwd
	 */
	private NotificationManagerJavaImpl() {
		super();
		ConnectionManager con = ConnectionManager.getInstance();
		HashMap<String, Object> props = con.populateEnvProps4Java();
		//System.out.println("Props[" + props + "]");

		//TO DO- NEED TO REMOVE COMMENT WHILE TESTING IS OVER(29TH APR)
		
		try {
			client = MessagingClientFactory.createMessagingClient(props);
			//listener=new NotificationStatusListener4Java();
			//client.setMessageListener(listener); //Message Listener need NOT to be set as we are not receiving any message.
			//Rather than sending listener for each message it is performant to be set for the client
			//client.setStatusListener(listener); //Added by Sujoy on 15th May,2016
			
		} catch (oracle.sdp.messaging.MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static NotificationManagerJavaImpl getInstance() {
		if (instance == null) {
			debugLogger.debug("*********************** LOADING SYSTEM CONFIGUARATION *********************");
			instance = new NotificationManagerJavaImpl();
			debugLogger.debug("Instance is null & creating object ...["+instance + "]");
		}
		
		return instance;
	}
	
	/**
	 * Comment End (3rd May,2016)
	 * @param msgDetails
	 * @param client
	 * @param listener
	 * @throws NotificationMgrGenericException
	 */
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techm.nms.mgmt.NotificationManager#sendMessage()
	 */
	
	public void sendMessage(MessageDetails msgDetails)
			throws NotificationMgrGenericException {

		long initialTime=System.currentTimeMillis();
		long currentTime=System.currentTimeMillis();
		if(debugLogger.isDebugEnabled())
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Inside sendMessage");
		
		EmailMessageHandlerImpl emailHandler=null;
		TextMessageHandlerImpl textHandler=null;
		NonTemplatizedMessageHandler nonTemplateHandler=null;
		Object notifBody=null;
		
		String contentType=null;
		/*
		 * Code added on 29th Apr to check what message handler to be initiated
		 */
		if (msgDetails.getTemplateFile() != null
				&& msgDetails.isEmailTemplate()) {
			emailHandler=getEmailMsgHandler(msgDetails);
			contentType=emailHandler.getContentType();
			currentTime=System.currentTimeMillis();
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken before creating notification body [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			notifBody=emailHandler.createNotificationBody(msgDetails); //The object returned consists of email content, attachment content & both
			
		}
		if(msgDetails.getTemplateFile()!=null && msgDetails.isTextTemplate()){
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Text Message Handler ");
			textHandler=getTextMsgHandler(msgDetails);
			contentType=textHandler.getContentType();
			notifBody=textHandler.createNotificationBody(msgDetails);		
		}
		
		//Added Code to incorporate non-template messages(4th Oct,2016)- Sujoy
		if(msgDetails.getTemplateFile()==null){
			nonTemplateHandler=getNonTempMsgHandler(msgDetails);
			contentType=nonTemplateHandler.getContentType();
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#" + msgDetails.getCorrelator() + " # Direct notification without any template");
			notifBody=nonTemplateHandler.createNotificationBody(msgDetails);
		}
			
		if(debugLogger.isDebugEnabled())
		{
			long timeDiff=System.currentTimeMillis()- currentTime;
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in creating notification body [" + String.valueOf(timeDiff) + "]");
			currentTime=System.currentTimeMillis();
		}
		
		
		// Creating message
		oracle.sdp.messaging.Message msg = createMessage(notifBody, contentType);
		if(debugLogger.isDebugEnabled())
		{
			long timeDiff=System.currentTimeMillis()- currentTime;
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in creating notification msg [" + String.valueOf(timeDiff) + "]");
		}
		// Adding recipients
		try{
		msg.addAllRecipients(addRecipients4Java(msgDetails));
		}catch(Exception e){
			//System.out.println("Exception in addAllRecipients...");
			e.printStackTrace();
		}
		//debugLogger.debug("After ADD ALL Recipient..");
		try{
		msg.addSender(getSenderAddress(msgDetails));
		}catch(Exception e){
			//System.out.println("Exception in getSenderAddress");
			e.printStackTrace();
		}
		//debugLogger.debug("After ADD Sender...");
		msg.setSubject(msgDetails.getSubject());
		//debugLogger.debug("After ADD Subject...");
		// msg.setMessageInfo(msgHandler.setMessageProperties(NotificationUtils.NMS_IMPL_CAT_JAVA));
		// // Setting message priority & other msg
		msg.setMessageInfo(setNotificationProperties(msgDetails));
		
		//debugLogger.debug("After setting message property :: Session : ["+msg.getMessageInfo().getSession());

		String msgId = null;
		MessageInformation msgInfo = new MessageInformation();
		ArrayList<String> recipientAddr = populateRecipientAddress(msgDetails
				.getRecipients());
		byte[] correlator=CorrelationGenerator.getUUID();
		// Sending message
		try {
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Sending Message to Gateway Client");
			currentTime=System.currentTimeMillis();
			if(debugLogger.isDebugEnabled())
			{
				long timeDiff=System.currentTimeMillis()- currentTime;
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in before calling send [" + String.valueOf(timeDiff) + "]");
			}
			currentTime=System.currentTimeMillis();
			//debugLogger.debug("====== Sending Message with correlator id ;[" + correlator + "] =======");
			
			/** Removed Listener for performance improvement(11th Jul,2016 **/
			//msgId = client.send(msg, listener,correlator);
			msgId=client.send(msg);
			
			if(debugLogger.isDebugEnabled())
			{
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Message sent to gateway for Message Identifier [ "+msgId+"]");
				long timeDiff=System.currentTimeMillis()- currentTime;
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time Taken in SDP Client send ms [" +  String.valueOf(timeDiff) + "]");
			}
			currentTime=System.currentTimeMillis();
			
			String status = "DELIVERY TO GATEWAY SUCCESS";
			String statusDescription = "Message has been successfully delivered";
			
			NotificationManagerDBInteractionImpl nmsDB = 
			NotificationManagerDBInteractionImpl.getInstance();
			NotificationHistory notificationHistory = new NotificationHistory(
					msgDetails.getScenarioId(), msgDetails.getRecipientId(),
					msgDetails.getRecipientType(),
					msgDetails.getIntegrationID(),
					msgDetails.getDeliveryMode(), recipientAddr, msgId,
					msgDetails.getSource(),
					msgDetails.getMsgTxt() != null ? msgDetails.getMsgTxt()
							: (notifBody instanceof String ? (String)getMessageContent(notifBody):null),
					
					msgDetails.getSubject(), 
					(notifBody instanceof EmailContent ? ((EmailContent)notifBody).getEmailContentAsByteArray():null),
					(notifBody instanceof EmailContent ? ((EmailContent)notifBody).getAttachmentAsByteArray():null),
					status, statusDescription);

			/**
			 * Populating History record in DB
			 */
			try {
				nmsDB.createNotificationHistory(notificationHistory);
				if(debugLogger.isDebugEnabled())
				{
					debugLogger.debug("#"+msgDetails.getCorrelator()+"# System has stored notification information in DB, and waiting for updates from Gateway...");
					long timeDiff=System.currentTimeMillis()- currentTime;
					debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time Taken in storing data in DB [" +  String.valueOf(timeDiff) + "]");
				}
			} catch (NotificationMgrSQLException e) {
				errorLogger
						.error("#"
								+ msgDetails.getCorrelator()
								+ "# Inserting record into Notification History failed due to ["
								+ e.getMessage() + "]");
			}
		} /*catch (oracle.sdp.messaging.MessagingException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Messaging Sending failed with reason[" + e.getMessage()
							+ "]", e);
			throw new NotificationMgrGenericException(
					"Messaging Sending failed with reason[" + e.getMessage()
							+ "]", e);
		}*/catch(Exception e){e.printStackTrace();}
		if(debugLogger.isDebugEnabled())
		{
			
			long timeDiff=System.currentTimeMillis()- initialTime;
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time Taken in send method [" +  String.valueOf(timeDiff) + "]");
		}
		
	}

	/**
	 * Method is used to send notification and populate the history
	 * @return NotificationHistory instance returned to caller
	 */
	public NotificationHistory sendNotification(MessageDetails msgDetails)
			throws NotificationMgrGenericException {

		long initialTime=System.currentTimeMillis();
		long currentTime=System.currentTimeMillis();
		if(debugLogger.isDebugEnabled())
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Inside sendMessage");
		
		EmailMessageHandlerImpl emailHandler=null;
		TextMessageHandlerImpl textHandler=null;
		NonTemplatizedMessageHandler nonTemplateHandler=null;
		NotificationHistory notificationHistory=null;
		Object notifBody=null;
		boolean isEmailTemplate=false;
		String contentType=null;
		/*
		 * Code added on 29th Apr to check what message handler to be initiated
		 */
		if (msgDetails.getTemplateFile() != null
				&& msgDetails.isEmailTemplate()) {
			emailHandler=getEmailMsgHandler(msgDetails);
			contentType=emailHandler.getContentType();
			currentTime=System.currentTimeMillis();
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken before creating notification body [" + String.valueOf(System.currentTimeMillis()-currentTime) + "]");
			//notifBody=emailHandler.createNotificationBody(msgDetails);
			//This method is called for Yahsat where we need to just populate the email content without any attachment
			//The 'notifBody' is populated with ByteArrayOutputStream
			notifBody=emailHandler.createEmailBody(msgDetails);
			isEmailTemplate=true;
		}
		if(msgDetails.getTemplateFile()!=null && msgDetails.isTextTemplate()){
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Text Message Handler ");
			textHandler=getTextMsgHandler(msgDetails);
			contentType=textHandler.getContentType();
			notifBody=textHandler.createNotificationBody(msgDetails);		
		}
		
		//Added Code to incorporate non-template messages(4th Oct,2016)- Sujoy
		if(msgDetails.getTemplateFile()==null){
			nonTemplateHandler=getNonTempMsgHandler(msgDetails);
			contentType=nonTemplateHandler.getContentType();
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#" + msgDetails.getCorrelator() + " # Direct notification without any template");
			notifBody=nonTemplateHandler.createNotificationBody(msgDetails);
		}
			
		if(debugLogger.isDebugEnabled())
		{
			long timeDiff=System.currentTimeMillis()- currentTime;
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in creating notification body [" + String.valueOf(timeDiff) + "]");
			currentTime=System.currentTimeMillis();
		}
		
		
		// Creating message
		oracle.sdp.messaging.Message msg = null;
		
		//The following code is commented by Sujoy(1st Sep,2017)
		/*if(isEmailTemplate)
			msg=createEmailMsg(notifBody, contentType);
		else
		*/
			msg=createMessage(notifBody, contentType);
		if(debugLogger.isDebugEnabled())
		{
			long timeDiff=System.currentTimeMillis()- currentTime;
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in creating notification msg [" + String.valueOf(timeDiff) + "]");
		}
		// Adding recipients
		try{
		msg.addAllRecipients(addRecipients4Java(msgDetails));
		}catch(Exception e){
			//System.out.println("Exception in addAllRecipients...");
			e.printStackTrace();
		}
		
		try{
		msg.addSender(getSenderAddress(msgDetails));
		}catch(Exception e){
		
			e.printStackTrace();
		}
		
		msg.setSubject(msgDetails.getSubject());
		
		
		// // Setting message priority & other msg
		msg.setMessageInfo(setNotificationProperties(msgDetails));
		
		String msgId = null;
		MessageInformation msgInfo = new MessageInformation();
		ArrayList<String> recipientAddr = populateRecipientAddress(msgDetails
				.getRecipients());
		byte[] correlator=CorrelationGenerator.getUUID();
		// Sending message
		try {
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Sending Message to Gateway Client");
			currentTime=System.currentTimeMillis();
			if(debugLogger.isDebugEnabled())
			{
				long timeDiff=System.currentTimeMillis()- currentTime;
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Time taken in before calling send [" + String.valueOf(timeDiff) + "]");
			}
			currentTime=System.currentTimeMillis();
			//debugLogger.debug("====== Sending Message with correlator id ;[" + correlator + "] =======");
			
			/** Removed Listener for performance improvement(11th Jul,2016 **/
			//msgId = client.send(msg, listener,correlator);
			msgId=client.send(msg);
			
			if(debugLogger.isDebugEnabled())
			{
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Message sent to gateway for Message Identifier [ "+msgId+"]");
				long timeDiff=System.currentTimeMillis()- currentTime;
				debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time Taken in SDP Client send ms [" +  String.valueOf(timeDiff) + "]");
			}
			currentTime=System.currentTimeMillis();
			
			String status = "DELIVERY TO GATEWAY SUCCESS";
			String statusDescription = "Message has been successfully delivered";
			
			
			notificationHistory = new NotificationHistory(
					msgDetails.getScenarioId(), msgDetails.getRecipientId(),
					msgDetails.getRecipientType(),
					msgDetails.getIntegrationID(),
					msgDetails.getDeliveryMode(), recipientAddr, msgId,
					msgDetails.getSource(),
					msgDetails.getMsgTxt() != null ? msgDetails.getMsgTxt()
							: (notifBody instanceof String ? (String)getMessageContent(notifBody):null),
					
					msgDetails.getSubject(),
					(notifBody instanceof EmailContent ? ((EmailContent)notifBody).getEmailContentAsByteArray():null),
					(notifBody instanceof EmailContent ? ((EmailContent)notifBody).getAttachmentAsByteArray():null),
					
					
					status, statusDescription);

			/**
			 * Populating History record in DB
			 */
			NotificationManagerDBInteractionImpl nmsDB = 
					NotificationManagerDBInteractionImpl.getInstance();
			
			try {
				nmsDB.createNotificationHistory(notificationHistory);
				if(debugLogger.isDebugEnabled())
				{
					debugLogger.debug("#"+msgDetails.getCorrelator()+"# System has stored notification information in DB, and waiting for updates from Gateway...");
					long timeDiff=System.currentTimeMillis()- currentTime;
					debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time Taken in storing data in DB [" +  String.valueOf(timeDiff) + "]");
				}
			} catch (NotificationMgrSQLException e) {
				errorLogger
						.error("#"
								+ msgDetails.getCorrelator()
								+ "# Inserting record into Notification History failed due to ["
								+ e.getMessage() + "]");
			}
			
		} /*catch (oracle.sdp.messaging.MessagingException e) {
			// TODO Auto-generated catch block
			errorLogger.error("Messaging Sending failed with reason[" + e.getMessage()
							+ "]", e);
			throw new NotificationMgrGenericException(
					"Messaging Sending failed with reason[" + e.getMessage()
							+ "]", e);
		}*/catch(Exception e){e.printStackTrace();}
		if(debugLogger.isDebugEnabled())
		{
			
			long timeDiff=System.currentTimeMillis()- initialTime;
			debugLogger.debug("#"+msgDetails.getCorrelator()+"# Total Time Taken in send method [" +  String.valueOf(timeDiff) + "]");
		}
		return notificationHistory;
	}


	private ArrayList<String> populateRecipientAddress(
			ArrayList<AddressDetails> addrList) {

		ArrayList<String> recipientAddrList = new ArrayList<String>();
		for (int i = 0; i < addrList.size(); i++)
			recipientAddrList.add(addrList.get(i).getAddress());

		return recipientAddrList;
	}

	private oracle.sdp.messaging.Address getSenderAddress(
			MessageDetails msgDetails) {
		oracle.sdp.messaging.Address sender = oracle.sdp.messaging.MessagingFactory
				.buildAddress(msgDetails.getSender().getAddress(), msgDetails
						.getSender().getAddressType(), msgDetails.getSender()
						.getAddressType().contains("SMS") ? null
						: Address.EMAIL_MODE_TO);

		debugLogger.debug("Sender Address added ["+sender);
		return sender;
	}

	private oracle.sdp.messaging.Address[] addRecipients4Java(
			MessageDetails details) {

		ArrayList<AddressDetails> recipients = details.getRecipients();
		oracle.sdp.messaging.Address address[] = new oracle.sdp.messaging.Address[recipients.size()];

		int index = 0;
		for (int i = 0; i < recipients.size(); i++) {
			//debugLogger.debug("1 ["+recipients.get(i).getAddress());
			//debugLogger.debug("2 ["+recipients.get(i).getAddressType());
			//debugLogger.debug("3 [" + recipients.get(i).getAddressType().indexOf("Email"));
			address[index] = oracle.sdp.messaging.MessagingFactory
					.buildAddress(recipients.get(i).getAddress(), recipients
							.get(i).getAddressType(), recipients.get(i)
							.getAddressType().indexOf("Email")!=-1 ? null
							: Address.EMAIL_MODE_TO);
			
			index++;
		}
		//debugLogger.debug("Recipient added ["+address);
		return address;
	}

	/**
	 * Used to create Message in Java API
	 * 
	 * @param obj
	 * @param handler
	 * @return
	 * @throws NotificationMgrGenericException
	 */
	/*private oracle.sdp.messaging.Message createMessage(Object obj,
			MessageHandler handler) throws NotificationMgrGenericException {
		oracle.sdp.messaging.Message msg = null;
		msg = oracle.sdp.messaging.MessagingFactory.createMessage();
		try {
			msg.setContent((Multipart) obj);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException(
					"Instantiating Message through Java API failed", e);
		}
		System.out.println("Content Type [" + handler.getContentType() + "]");

		// msg.getHeaders().add(populateHeader(handler.getContentType()));
		return msg;
	}*/
	
	
	private oracle.sdp.messaging.Message createMessage(Object obj,
			String contentType) throws NotificationMgrGenericException {
		oracle.sdp.messaging.Message msg = null;
		
		msg = oracle.sdp.messaging.MessagingFactory.createMessage();
		if(obj instanceof EmailContent)
			try {
				msg.setContent(((EmailContent)obj).getEmailContent());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			msg.setContent(obj, contentType);
		return msg;
	}
	
	private oracle.sdp.messaging.Message createEmailMsg(Object obj, String contentType) throws NotificationMgrGenericException{
		
		ByteArrayOutputStream baos= (ByteArrayOutputStream)obj;
	//Setting the MimeMultiPart content type
			MimeMultipart mp=new MimeMultipart("mixed");
			
			try {
			
				// Adding the body of the EMAIL notification
				mp.addBodyPart(createEmailBodyPart(baos,NotificationUtils.getContentType("EMAIL")));
				
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
				throw new NotificationMgrGenericException("Creating email body Failed with message[" + e.getMessage() +"]", e);
			}
			oracle.sdp.messaging.Message msg = null;
			msg = oracle.sdp.messaging.MessagingFactory.createMessage();
			//debugLogger.debug("Before message creation...");
			msg.setContent(mp, contentType);
		return msg;
	}
	
	private MimeBodyPart createEmailBodyPart(ByteArrayOutputStream baos, String contentType) throws NotificationMgrGenericException{
		ByteArrayDataSource ds=new ByteArrayDataSource(baos.toByteArray(),contentType);
		
		MimeBodyPart body=null;
		
		try {
			body=new MimeBodyPart();
			body.setDataHandler(new DataHandler(ds));
			
		} catch (MessagingException e) {
			
			throw new NotificationMgrGenericException("Creating email body Failed with message[" + e.getMessage() +"]", e);
		}
		
		return body;
	}
	

	/**
	 * Used to populate recipients oracle.sdp.messaging.Address[] from incoming
	 * message
	 * 
	 * @param details
	 *            Details of the message
	 * @return Recipients of the message
	 */
	/*
	 * private Address[] addRecipients(MessageDetails details){
	 * 
	 * ArrayList<AddressDetails> recipients=details.getRecipients();
	 * ArrayList<oracle.sdp.messaging.Address> addressList=new
	 * ArrayList<oracle.sdp.messaging.Address>(); oracle.sdp.messaging.Address
	 * address=null;
	 * 
	 * for(int i=0;i<recipients.size();i++){ address=
	 * oracle.sdp.messaging.MessagingFactory
	 * .createAddress(recipients.get(i).getAddressType()+ ":" +
	 * recipients.get(i).getAddress()); addressList.add(address); } return
	 * (Address[])addressList.toArray(); }
	 */

	/**
	 * Used to populate oracle.sdp.messaging.Address instance from address
	 * string
	 * 
	 * @param addrDetails
	 *            Address Details containing type of address and address string
	 * @return oracle.sdp.messaging.Address instance from address string
	 */
	private Address getAddress(AddressDetails addrDetails) {
		Address addr = MessagingFactory.createAddress(addrDetails
				.getAddressType() + ":" + addrDetails.getAddress());
		return addr;
	}
	
	public oracle.sdp.messaging.MessageInfo setNotificationProperties(MessageDetails msgDetails) {
		oracle.sdp.messaging.MessageInfo msgInfo=MessagingFactory.createMessageInfo();
		
		msgInfo.setSession(MessageSessionType.INBOUND_SESSION);
        //TrackingType tracking
        //msgInfo.setTracking(TrackingType.TIGHT_FULL_TRACK);
		if (msgDetails.getDeliveryMode().equals("Email")){
			
			msgInfo.setPriority(MessagePriorityType.HIGH);
		}
        //msgInfo.setHighestStatusLevel(StatusType.READ_ACKNOWLEDGEMENT_SUCCESS);
        return msgInfo;
	}
	
	
}
