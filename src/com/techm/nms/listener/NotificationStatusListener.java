/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.mgmt.NotificationManagerDBInteraction;
import com.techm.nms.mgmt.NotificationManagerDBInteractionImpl;
import com.techm.nms.mgmt.NotificationManagerWSImpl;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.sql.dto.UpdateNotificationHistory;
import com.techm.nms.util.AddressDetails;
import com.techm.nms.util.MessageStatus;

import oracle.ucs.messaging.ws.Listener;
import oracle.ucs.messaging.ws.MessagingException;
import oracle.sdp.messaging.ListenerException;
import oracle.sdp.messaging.Message;
import oracle.sdp.messaging.Status;

/**
 * @author sm0015566
 * 
 */
@WebService(serviceName = "ListenerService", targetNamespace = "http://xmlns.oracle.com/ucs/messaging/", endpointInterface = "oracle.ucs.messaging.ws.Listener", wsdlLocation = "META-INF/wsdl/listener.wsdl", portName = "Listener")
public class NotificationStatusListener implements Listener {

	private static final Logger errorLogger = Logger
			.getLogger("NotificationStatusListener");

	/**
	 * 
	 */
	public NotificationStatusListener() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * oracle.sdp.messaging.Listener#onMessage(oracle.sdp.messaging.Message,
	 * java.io.Serializable)
	 */
	/*
	 * @Override public void onMessage(Message arg0, Serializable arg1) throws
	 * ListenerException { // TODO Auto-generated method stub
	 * 
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.sdp.messaging.Listener#onStatus(oracle.sdp.messaging.Status,
	 * java.io.Serializable)
	 */
	/*
	 * @Override public void onStatus(Status status, Serializable correlator)
	 * throws ListenerException { // TODO Auto-generated method stub
	 * 
	 * 
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * oracle.ucs.messaging.ws.Listener#onMessage(oracle.ucs.messaging.ws.types
	 * .Message, byte[])
	 */
	@Override
	@WebMethod(action = "http://www.oracle.com/communications/messaging/onMessage")
	@RequestWrapper(localName = "onMessage", targetNamespace = "http://xmlns.oracle.com/ucs/messaging/", className = "oracle.ucs.messaging.ws.OnMessage")
	@ResponseWrapper(localName = "onMessageResponse", targetNamespace = "http://xmlns.oracle.com/ucs/messaging/", className = "oracle.ucs.messaging.ws.OnMessageResponse")
	public void onMessage(
			@WebParam(name = "message", targetNamespace = "") oracle.ucs.messaging.ws.types.Message arg0,
			@WebParam(name = "correlator", targetNamespace = "") byte[] arg1)
			throws MessagingException {
		// TODO Auto-generated method stub
		// System.out.println("*********INSIDE ON MESSAGE METHOD *************");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * oracle.ucs.messaging.ws.Listener#onStatus(oracle.ucs.messaging.ws.types
	 * .Status, byte[])
	 */
	@Override
	@WebMethod(action = "http://www.oracle.com/communications/messaging/onStatus")
	@RequestWrapper(localName = "onStatus", targetNamespace = "http://xmlns.oracle.com/ucs/messaging/", className = "oracle.ucs.messaging.ws.OnStatus")
	@ResponseWrapper(localName = "onStatusResponse", targetNamespace = "http://xmlns.oracle.com/ucs/messaging/", className = "oracle.ucs.messaging.ws.OnStatusResponse")
	public void onStatus(
			@WebParam(name = "status", targetNamespace = "") oracle.ucs.messaging.ws.types.Status status,
			@WebParam(name = "correlator", targetNamespace = "") byte[] correlator)
			throws MessagingException {

		// TODO Auto-generated method stub
		NotificationManagerDBInteractionImpl nmsDB = NotificationManagerDBInteractionImpl
				.getInstance();
		// ArrayList<MessageStatus> statusList=nms.getStatusList();
		// TODO- More performant search algo need to be be written

		// Retrieving NMS Instance to get the Status Map
		NotificationManagerWSImpl nms = NotificationManagerWSImpl.getInstance(
				null, null, null, null);
		HashMap<String, MessageStatus> statusMap = nms.getStatusMap();

		MessageStatus msgStatus = statusMap.get(status.getMessageIdentifier());

		// System.out.println("*********Insertion into History table from Status Listner START");
		msgStatus.setStatus(status.getType().value());
		msgStatus.setStatusDesc(status.getContent());

		/**
		 * Populate Notification History POJO for History insertion
		 */
		// ArrayList<String> recipientAddr =
		// populateRecipientAddress(msgStatus.getMsgDetails().getRecipients());

		// System.out.println("*********Start POJO Creation from Status");
		UpdateNotificationHistory updateNotificationHistory = new UpdateNotificationHistory(
				status.getMessageIdentifier(), status.getType().value(),
				status.getContent());

		// System.out.println("*********End POJO Creation from Status, value is ["+
		// notificationHistory + "]");
		/**
		 * Code for Insertion in History table- 1. Successful cases- Message
		 * sent successfully by gateway 2. Failure Cases- Message successfully
		 * sent to GW but GW failed to deliver
		 */
		try {
			// nmsDB.createNotificationHistory(notificationHistory);
			nmsDB.updateNotificationHistory(updateNotificationHistory);
			// System.out.println("*********Insertion into History table from Status Listner END");
		} catch (NotificationMgrSQLException e) {

			// e.printStackTrace();
			errorLogger
					.error("#"
							+ msgStatus.getMsgDetails().getCorrelator()
							+ "# Updating record into Notification History failed due to ["
							+ e.getMessage() + "]");
			/*
			 * logger.error("  Notification Scenario Id : ["+
			 * msgStatus.getMsgDetails().getScenarioId() + "]");
			 * logger.error("  Delivery Mode : ["+
			 * msgStatus.getMsgDetails().getDeliveryMode() + "]");
			 * logger.error("  Message Id : ["+
			 * msgStatus.getMsgInfo().getMessageID() + "]");
			 * logger.error("  Message Content : ["+
			 * msgStatus.getMsgInfo().getMessageContent() + "]");
			 * logger.error("  Message Source : ["+
			 * msgStatus.getMsgDetails().getSource() + "]");
			 * logger.error("  Subject : ["+
			 * msgStatus.getMsgInfo().getEmailSubject() + "]");
			 * logger.error("  Status : [" + msgStatus.getStatus() + "]");
			 * logger
			 * .error(" *************************************************** ");
			 */
			// TODO - Exception to be thrown
		}
	}

	/*
	 * private ArrayList<String> populateRecipientAddress(
	 * ArrayList<AddressDetails> addrList) {
	 * 
	 * ArrayList<String> recipientAddrList = new ArrayList<String>(); for (int i
	 * = 0; i < addrList.size(); i++)
	 * recipientAddrList.add(addrList.get(i).getAddress());
	 * 
	 * return recipientAddrList; }
	 */

}