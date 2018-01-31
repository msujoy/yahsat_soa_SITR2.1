/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
//import java.util.logging.Logger;


import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import oracle.sdp.messaging.Address;
import oracle.sdp.messaging.Message;
import oracle.sdp.messaging.MessagingClientFactory;
import oracle.sdp.messaging.MessagingFactory;
import oracle.ucs.messaging.ws.Listener;
import oracle.ucs.messaging.ws.types.ListenerReference;
import oracle.ucs.messaging.ws.types.MessageInfo;
import oracle.ucs.messaging.ws.types.MessageQuery;
import oracle.ucs.messaging.ws.types.MetadataEntry;
import oracle.ucs.messaging.ws.types.MimeHeader;
import oracle.ucs.messaging.ws.types.PriorityType;
import oracle.ucs.messaging.ws.types.SessionType;
import oracle.ucs.messaging.ws.types.Status;
import oracle.ucs.messaging.ws.types.StatusType;
import oracle.ucs.messaging.ws.types.TrackingType;

import com.techm.dms.exception.DMSInteractionException;
import com.techm.dms.utils.DMSResponse;
import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.listener.NotificationStatusListener;
import com.techm.nms.listener.NotificationStatusListener4Java;
import com.techm.nms.sql.dao.CustomerPrefDAO;
import com.techm.nms.sql.dao.NotificationHistoryDAO;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.util.ConnectionManager;
import com.techm.nms.util.CorrelationGenerator;
import com.techm.nms.util.MessageDetails;
import com.techm.nms.util.AddressDetails;
import com.techm.nms.util.MessageInformation;
import com.techm.nms.util.MessageStatus;
import com.techm.nms.util.MessageStatusQuery;
import com.techm.nms.util.NotificationUtils;
import com.techm.templatemgr.exception.TemplateMgrException;
import com.techm.templatemgr.mgmt.HTMLTemplateManager;
import com.techm.templatemgr.mgmt.TemplateManager;
import com.techm.templatemgr.mgmt.TemplateManagerImpl;
import com.techm.templatemgr.util.Util;

/**
 * This class is used for managing notification through Oracle UMS UCS web
 * service API implementation
 * 
 * @author sm0015566
 * 
 */
public class NotificationManagerWSImpl extends NotificationManagerImpl {

	private oracle.ucs.messaging.ws.MessagingClient client;

	private HashMap<String, MessageStatus> statusMap;

	private ApplicationContext context;

	/**
	 * @return the statusMap
	 */
	public HashMap<String, MessageStatus> getStatusMap() {
		return statusMap;
	}

	/**
	 * @param statusMap
	 *            the statusMap to set
	 */
	public void setStatusMap(HashMap<String, MessageStatus> statusMap) {
		this.statusMap = statusMap;
	}

	private static NotificationManagerWSImpl instance;

	private ListenerReference listenerRef;

	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error");

	/** Declaration of debug logger responsible for logging error */

	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	private NotificationManagerWSImpl() {
		instance = null;

	}

	public static NotificationManagerWSImpl getInstance(String host,
			String port, String username, String pwd) {

		if (instance == null) {

			instance = new NotificationManagerWSImpl(host, port, username, pwd);
			debugLogger
					.debug("*********************** LOADING SYSTEM CONFIGUARATION *********************");
			debugLogger.debug("HOST :: [" + host + "] PORT :: [" + port
					+ "] USERNAME :: [" + username + "] PASSWORD ::[" + pwd
					+ "]");
			debugLogger.debug("Instance is null & creating object ...["
					+ instance + "]");
		} else {
			// instance.templateDir = templateDir;
			debugLogger.debug("Instance already present[" + instance + "]");
		}
		return instance;

	}

	/*
	 * public static NotificationManagerWSImpl getInstance() {
	 * 
	 * if (instance == null){
	 * //System.out.print("Instance is null & creating object ...["); instance =
	 * new NotificationManagerWSImpl(); //System.out.println(instance + "]"); }
	 * 
	 * return instance; }
	 */

	private NotificationManagerWSImpl(String host, String port,
			String username, String pwd) {
		// super(host,port,username,pwd);

		ConnectionManager con = ConnectionManager.getInstance();
		// logger.debug("*********Inside WSImpl, connecton created [" + con+
		// "]");
		// HashMap<String, Object> props = con.populateEnvProps4WS(host, port,
		// username, pwd);
		HashMap<String, Object> props = con.populateEnvProps4Java();
		// logger.debug("*********Inside WSImpl, Env populated [" + props+ "]");
		/*** Commented for locally test ****/
		client = new oracle.ucs.messaging.ws.MessagingClient(props);

		/*** End Commented for locally test ****/
		// logger.debug("*********Inside WSImpl, Client created [" + client +
		// "]");
		// super.templateDir = templateDir;
		// Creating Spring Application Context

		statusMap = new HashMap<String, MessageStatus>();
		String callbackURL = "http://" + host + ":" + port
				+ "/UMSStatusListener";
		debugLogger.debug("Callback URL created [" + callbackURL + "]");
		debugLogger.debug("Setting Listener Reference ...");
		setListenerRef(callbackURL);
	}

	/**
	 * 
	 * Method to register listener for checking the status of message
	 * 
	 * @return
	 */

	// To be Checked
	private void setListenerRef(String callbackURL) {

		Listener listener = new NotificationStatusListener();

		try {
			Endpoint nmsStatusEndpoint = javax.xml.ws.Endpoint.publish(
					callbackURL, listener);

			debugLogger
					.debug("Listener Reference Endpoint has been set succesfully");
		} catch (Exception e) {
			errorLogger.error("Failed to set Endpoint" + e.getMessage());
			// e.printStackTrace();
		}
		listenerRef = new ListenerReference();

		listenerRef.setEndpoint(callbackURL);
		debugLogger.debug("Listerner Reference has been set succesfully ["
				+ listenerRef + "]");
	}

	/**
	 * Method to send message to customer. Primary functionalities- 1.
	 * Instantiate appropriate message handler interface(i.e
	 * Email,Text,Non-Template). 2. Populate the notification body which need to
	 * be sent to customer 3. Send the message and store the unique generated
	 * message id into datastore.
	 */
	public void sendMessage(MessageDetails msgDetails)
			throws NotificationMgrGenericException {

		// Instantiating correct object instance for notification body
		// generation

		MessageHandler msgHandler = getMessageHandlerInstance(msgDetails);

		// Retrieving the MimeMultiPart for Email message or Text String for SMS
		Object notifBody = msgHandler.createNotificationBody(msgDetails);

		// Creating message
		oracle.ucs.messaging.ws.types.Message msg = createMessage(notifBody,
				msgHandler);

		// Adding recipients
		msg.getRecipients().addAll(addRecipients(msgDetails));
		msg.getSenders().add(getSenderAddress(msgDetails));
		msg.setSubject(msgDetails.getSubject());
		// msg.setMessageInfo((oracle.ucs.messaging.ws.types.MessageInfo)msgHandler.setMessageProperties(NotificationUtils.NMS_IMPL_CAT_WS));
		// // Setting
		// message
		// priority &
		// other msg
		msg.setMessageInfo(setNotificationProperties(msgDetails));

		debugLogger.debug("After setting message property :: Session : ["
				+ msg.getMessageInfo().getSession());

		String msgId = null;
		MessageInformation msgInfo = new MessageInformation();
		// Sending message

		// byte[] correlator = NotificationUtils.getUUID();
		try {
			// logger.debug("************ Inside sendMessage() before msgId******");
			ArrayList<String> recipientAddr = populateRecipientAddress(msgDetails
					.getRecipients());
			// ListenerReference ref=getListenerRef();
			// logger.debug("Listener Ref" + ref);
			// msgId=client.send(msg, null, correlator);
			// client.setMessageListener(getListenerRef());
			debugLogger.debug("#" + msgDetails.getCorrelator()
					+ "# Listener object has been set to " + listenerRef
					+ " and NMS is trying to send "
					+ msgDetails.getDeliveryMode() + " Notification...");
			// System.out.println("********Message Object ["+msg+"]");
			// System.out.println("***********Listener Object ["+listenerRef+"]");
			// System.out.println("************Correlator Object ["+correlator+"]");
			// msgId = client.send(msg, listenerRef, correlator);

			// Commenting the WS Based API call
			msgId = client.send(msg, listenerRef, msgDetails.getCorrelator());

			debugLogger.debug("#" + msgDetails.getCorrelator()
					+ "# Message sent to gateway for Message Identifier [ "
					+ msgId + "]");

			/**
			 * Populating HISTORY POJO for inserting record in DB
			 */

			String status = "WAITING_FOR_GATEWAY_RESPONSE";
			String statusDescription = "Waiting for gateway response";
			NotificationManagerDBInteractionImpl nmsDB = NotificationManagerDBInteractionImpl
					.getInstance();
			
			NotificationHistory notificationHistory = new NotificationHistory(
					msgDetails.getScenarioId(), msgDetails.getRecipientId(),
					msgDetails.getRecipientType(),
					msgDetails.getIntegrationID(),
					msgDetails.getDeliveryMode(), recipientAddr, msgId,
					msgDetails.getSource(),
					msgDetails.getMsgTxt() != null ? msgDetails.getMsgTxt()
							: (notifBody instanceof String ? (String)getMessageContent(notifBody):null),
					
					msgDetails.getSubject(), null,(notifBody instanceof MimeMultipart ? getMessageContent(notifBody):null),
					status, statusDescription);

			/**
			 * Populating History record in DB
			 */
			try {
				nmsDB.createNotificationHistory(notificationHistory);
				debugLogger
						.debug("System has stored notification information in DB, and waiting for updates from Gateway...");
			} catch (NotificationMgrSQLException e) {
				errorLogger
						.error("#"
								+ msgDetails.getCorrelator()
								+ "# Inserting record into Notification History failed due to ["
								+ e.getMessage() + "]");
			}

		} catch (oracle.ucs.messaging.ws.MessagingException e) {

			errorLogger.error("#" + msgDetails.getCorrelator()
					+ "# NMS has failed to send message via UMS ["
					+ e.getMessage() + "]");
			throw new NotificationMgrGenericException(
					"NMS has failed to send message via UMS [" + e.getMessage()
							+ "]", e);
		} catch (Exception e) {
			errorLogger
					.error("#"
							+ msgDetails.getCorrelator()
							+ "# Internal exception occured and NMS has failed to send message ["
							+ e.getMessage() + "]");
			throw new NotificationMgrGenericException(
					"Internal exception occured and NMS has failed to send message ["
							+ e.getMessage() + "]", e);

		}
		msgInfo.setMessageID(msgId);
		if (msgDetails.isTextTemplate())
			msgInfo.setMessageContent(notifBody.toString());
		if (msgDetails.isEmailTemplate())
			msgInfo.setEmailSubject(msgDetails.getSubject());
		/*
		 * if(msgDetails.getScenarioId() == null)
		 * msgInfo.setMessageContent(msgDetails.getMsgTxt());
		 */

		/**
		 * calling notification status
		 * */
		// populateMsgStatus(msgInfo, correlator, msgDetails);
		populateMsgStatus(msgInfo, msgDetails.getCorrelator(), msgDetails);

		// return msgInfo;
	}

	/**
	 * Used to populate MessageStatus object with correlator generated while
	 * sending message
	 * 
	 * @param info
	 * @param correlator
	 * @param msgDetails
	 * @return
	 */
	private void populateMsgStatus(MessageInformation info, byte[] correlator,
			MessageDetails msgDetails) {

		// logger.debug("Correlator from populateMsgStatus [" + correlator +
		// "]");
		MessageStatus status = new MessageStatus();
		status.setCorrelator(correlator);
		status.setMsgInfo(info);
		status.setMsgDetails(msgDetails);

		statusMap.put(info.getMessageID(), status);
	}

	/**
	 * Used to print Status of Notification
	 */
	private void printStatus(List<Status> msgStatusList) {
		for (int i = 0; i < msgStatusList.size(); i++) {
			debugLogger.debug("Msg Id["
					+ msgStatusList.get(i).getMessageIdentifier() + "]");
			debugLogger.debug("Gateway Msg Id["
					+ msgStatusList.get(i).getGatewayMessageIdentifier() + "]");
			debugLogger.debug("Status["
					+ msgStatusList.get(i).getType().value() + "]");
		}
	}

	/**
	 * Used to add recipient
	 * 
	 * @return
	 */
	private oracle.ucs.messaging.ws.types.Address addRecipients(
			ArrayList<String> addressList) {
		// This address can be a user identifier like USER:Sujoy or specific
		// device address like Email:msujoy@techmahindra.com
		oracle.ucs.messaging.ws.types.Address recipient = oracle.ucs.messaging.ws.MessagingFactory
				.createAddress("");

		return recipient;
	}

	private oracle.ucs.messaging.ws.types.Address getSenderAddress(
			MessageDetails msgDetails) {
		oracle.ucs.messaging.ws.types.Address sender = oracle.ucs.messaging.ws.MessagingFactory
				.createAddress(msgDetails.getSender().getAddressType() + ":"
						+ msgDetails.getSender().getAddress());
		return sender;
	}

	/**
	 * Method used to retrieve status of message that has been sent to
	 * recipients It is expected that external system can search details of the
	 * messages that has been sent to multiple recipients
	 * 
	 * @param query
	 *            Query criterion to find out the status
	 */
	/*
	 * public List<MessageStatus> getMessageStatus(MessageStatusQuery
	 * query)throws NotificationMgrGenericException{
	 * 
	 * //Populate the MessageQuery instance for the input search criterion
	 * ArrayList<MessageQuery> msgQryList=populateSearchCriterion(query);
	 * MessageQuery qry=null; List<Status> msgStatus=null; List<MessageStatus>
	 * msgStatusAll=new ArrayList<MessageStatus>();
	 * 
	 * //Find out status of messages for each MessageQuery instance(i.e search
	 * criterion) for(int i=0;i<msgQryList.size();i++){ qry=msgQryList.get(i);
	 * 
	 * try { //Find out the message ids fulfilling the search criterion
	 * List<String> msgIdList=client.getMessageIDs(qry);
	 * 
	 * 
	 * //As getStatus method expects recipients list as input populating //the
	 * recipient list from the MessageQuery object instance
	 * List<oracle.ucs.messaging.ws.types.Address> recipientList= new
	 * ArrayList<oracle.ucs.messaging.ws.types.Address>();
	 * recipientList.add(qry.getRecipient());
	 * 
	 * String msgId=null; //Finding out status of message for a particular
	 * message id for(int j=0;j<msgIdList.size();j++){ msgId=msgIdList.get(j);
	 * msgStatus=client.getStatus(msgId, recipientList);
	 * msgStatusAll.addAll(populateMessageStatusList(msgStatus)); } } catch
	 * (oracle.ucs.messaging.ws.MessagingException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); throw new
	 * NotificationMgrGenericException("Retrieving message status got failed[" +
	 * e.getMessage() + "]",e); } } return msgStatusAll; }
	 * 
	 * private ArrayList<MessageStatus> populateMessageStatusList(List<Status>
	 * listStatus){ ArrayList<MessageStatus> msgStatusList=new
	 * ArrayList<MessageStatus>(); for(int i=0;i<listStatus.size();i++)
	 * msgStatusList.add(populateMessageStatus(listStatus.get(i))); return
	 * msgStatusList; } private MessageStatus populateMessageStatus(Status
	 * status){ MessageStatus msgStatus=new MessageStatus();
	 * msgStatus.setStatus(status.getType().value());
	 * msgStatus.setMessageId(status.getMessageIdentifier());
	 * msgStatus.setDate(getFormattedDate(status.getDate()));
	 * msgStatus.setRecipient(status.getAddress().getValue());
	 * printMsgContent(status); return msgStatus; }
	 */
	private void printMsgContent(Status status) {
		List<MetadataEntry> entries = status.getMetadata().getEntries();

		for (int i = 0; i < entries.size(); i++)
			debugLogger.debug("Name[" + entries.get(i).getName() + "]:"
					+ entries.get(i).getValue());
	}

	private String getFormattedDate(XMLGregorianCalendar gc) {
		return gc.getDay() + "-" + gc.getMonth() + "-" + gc.getYear() + ":"
				+ gc.getHour() + ":" + gc.getMinute() + ":" + gc.getSecond();

	}

	private ArrayList<MessageQuery> populateSearchCriterion(
			MessageStatusQuery status) throws NotificationMgrGenericException {
		MessageQuery query = new MessageQuery();
		ArrayList<MessageQuery> msgQryList = new ArrayList<MessageQuery>();
		if (status.getStartTime() != null)
			query.setFromTime(getXMLGCFromDate(status.getStartTime()));
		if (status.getEndTime() != null)
			query.setToTime(getXMLGCFromDate(status.getEndTime()));
		for (int i = 0; i < status.getRecipientAddress().size(); i++) {
			query.setRecipient(getAddress(status.getRecipientAddress().get(i)));
			msgQryList.add(query);
		}
		return msgQryList;
	}

	private oracle.ucs.messaging.ws.types.Address getAddress(AddressDetails addr) {
		oracle.ucs.messaging.ws.types.Address address = oracle.ucs.messaging.ws.MessagingFactory
				.createAddress(addr.getAddressType() + ":" + addr.getAddress());
		return address;
	}

	private XMLGregorianCalendar getXMLGCFromDate(Date date)
			throws NotificationMgrGenericException {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		XMLGregorianCalendar xmlGC = null;
		try {
			xmlGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException(
					"Converting Date to XMLGregorianCalendar failed", e);
		}
		return xmlGC;
	}

	private ArrayList<String> populateRecipientAddress(
			ArrayList<AddressDetails> addrList) {

		ArrayList<String> recipientAddrList = new ArrayList<String>();
		for (int i = 0; i < addrList.size(); i++)
			recipientAddrList.add(addrList.get(i).getAddress());

		return recipientAddrList;
	}

	/**
	 * Used to create oracle.ucs.messaging.ws.types.Message
	 * 
	 * @Obj Instance of NotificationBody
	 * @handler Instance of MessageHandler
	 * @return oracle.ucs.messaging.ws.types.Message instance
	 */
	protected oracle.ucs.messaging.ws.types.Message createMessage(Object obj,
			MessageHandler handler) throws NotificationMgrGenericException {

		oracle.ucs.messaging.ws.types.Message msg = null;
		// MimeMultipart mp=(MimeMultipart)obj;
		// Creating oracle.ucs.messaging.ws.type.Message instance
		if (debugLogger.isDebugEnabled())
			debugLogger
					.debug("Creating UCS Messaging instance with content type["
							+ handler.getContentType() + "]");
		msg = oracle.ucs.messaging.ws.MessagingFactory.createMessage();
		msg.setContent(new DataHandler(obj, handler.getContentType()));
		System.out.println("Content Type [" + handler.getContentType() + "]");
		msg.getHeaders().add(populateHeader(handler.getContentType()));
		// msg.setContent(new DataHandler(obj,mp.getContentType()));

		return msg;
	}

	public oracle.ucs.messaging.ws.types.MessageInfo setNotificationProperties(
			MessageDetails msgDetails) {
		MessageInfo msgInfo = new MessageInfo();
		// TrackingType tracking
		msgInfo.setTracking(TrackingType.TIGHT_FULL_TRACK);
		msgInfo.setSession(SessionType.INBOUND_SESSION);
		if (msgDetails.getDeliveryMode().equals("Email")) {

			msgInfo.setPriority(PriorityType.HIGH);

		}
		// msgInfo.setHighestStatusLevel(StatusType.READ_ACKNOWLEDGEMENT_SUCCESS);
		return msgInfo;
	}

	/* (non-Javadoc)
	 * @see com.techm.nms.mgmt.NotificationManager#sendNotification(com.techm.nms.util.MessageDetails)
	 */
	@Override
	public NotificationHistory sendNotification(MessageDetails msgDetails)
			throws NotificationMgrGenericException {
		// TODO Auto-generated method stub
		return null;
	}

}
