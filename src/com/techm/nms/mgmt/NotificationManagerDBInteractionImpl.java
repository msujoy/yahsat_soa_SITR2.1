/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.sql.dao.CustomerPrefDAO;
import com.techm.nms.sql.dao.NotificationHistoryDAO;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.sql.dto.UpdateNotificationHistory;

/**
 * @author sm0015566
 *
 */
public class NotificationManagerDBInteractionImpl implements
		NotificationManagerDBInteraction {

	/** Instance of Spring application context */
	private static ApplicationContext context;

	/** Instance of NotificationManagerDBInteractionImpl */
	private static NotificationManagerDBInteractionImpl instance;

	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error");

	/** Declaration of debug logger responsible for logging error */

	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	/**
	 * 
	 */
	private NotificationManagerDBInteractionImpl() {

		context = new ClassPathXmlApplicationContext("application-context.xml");
	}

	public static NotificationManagerDBInteractionImpl getInstance() {
		if (instance == null)
			instance = new NotificationManagerDBInteractionImpl();
		return instance;
	}

	/*
	 * This method is used to create notification history
	 * 
	 * @see
	 * com.techm.nms.mgmt.NotificationManagerDBInteraction#createNotificationHistory
	 * (com.techm.nms.sql.dto.NotificationHistory)
	 */
	@Override
	public void createNotificationHistory(NotificationHistory history)
			throws NotificationMgrSQLException {
		// TODO Auto-generated method stub

		NotificationHistoryDAO notHistDAO = (com.techm.nms.sql.dao.NotificationHistoryDAO) context
				.getBean("notificationHistoryDAO");
		/*if (debugLogger.isDebugEnabled())
			debugCreateHistory(history);
		else
			debugLogger.debug("Not logging history record into logs as debug logging is disabled ...");*/
		try{
			notHistDAO.insertHistory(history);
		}catch(NotificationMgrSQLException e){
			errorLogger.error("Failed to create record in history ["+ e.getMessage() + "]");
			throw new NotificationMgrSQLException("Failed to create record in history ["+ e.getMessage() + "]");
		}
		
	}
	
	/**
	 * This method is to update NOTF_HIST table after successful notification to gateway 
	 */
	public void updateNotificationHistory(UpdateNotificationHistory updateHistory)
			throws NotificationMgrSQLException {
		// TODO Auto-generated method stub

		NotificationHistoryDAO notHistDAO = (com.techm.nms.sql.dao.NotificationHistoryDAO) context
				.getBean("notificationHistoryDAO");
		/*if (debugLogger.isDebugEnabled())
			debugCreateHistory(updateHistory);
		else
			debugLogger.debug("Not logging history record into logs as debug logging is disabled ...");*/
		try{
			notHistDAO.updateHistory(updateHistory);
			debugLogger.debug("Response recived from gateway and DB updated accordingly...");
			debugLogger.debug("***************************************************************************");
		}catch(NotificationMgrSQLException e){
			errorLogger.error("Failed to update record in history ["+ e.getMessage() + "]");
			throw new NotificationMgrSQLException("Failed to update record in history ["+ e.getMessage() + "]");
		}
		
	}

	/*
	 * This method is used to retrieve notification history records meeting
	 * certain criterion
	 * 
	 * @see com.techm.nms.mgmt.NotificationManagerDBInteraction#
	 * retrieveNotificationHistory(org.w3c.dom.Document)
	 */
	@Override
	public Document retrieveNotificationHistory(Document doc)
			throws NotificationMgrGenericException {
		//debugLogger.debug("Document["+ doc.getDocumentElement().getTextContent() + "]");

		NotificationHistoryDAO notHistDAO = (com.techm.nms.sql.dao.NotificationHistoryDAO) context
				.getBean("notificationHistoryDAO");
		Document docResponse = null;

		docResponse = notHistDAO.retrieveHistory(doc);

		return docResponse;

	}

	/**
	 * This method to be called from any service update the customer preference
	 * record. XML data to be converted in respective POJO & send across
	 * 
	 * @param doc
	 * @return
	 */
	public void syncCustomerPreference(Document doc)
			throws NotificationMgrGenericException {

		debugLogger.debug("Document["
				+ doc.getDocumentElement().getTextContent() + "]");

		CustomerPrefDAO custPrefDAO = (com.techm.nms.sql.dao.CustomerPrefDAO) context
				.getBean("customerPrefDAO");

		custPrefDAO.syncCustPref(doc);

	}

	private void debugCreateHistory(NotificationHistory history) {
		debugLogger
				.debug(" ***** Inserting record into NOTF_HIST START ***** ");
		debugLogger.debug("Message Id[" + history.getMessageId() + "]");
		debugLogger.debug("Delivery Mode[" + history.getDeliveryMode() + "]");
		debugLogger.debug("Notification Scenario Id["
				+ history.getNotfScenarioId() + "]");
		debugLogger.debug("Source System[" + history.getSourceName() + "]");
		debugLogger.debug("Message Content[" + history.getMsgContent() + "]");
		debugLogger.debug("Status[" + history.getStatus() + "]");
		debugLogger.debug(" ***** Inserting record into NOTF_HIST END ***** ");
	}
}
