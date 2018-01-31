/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
//import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.w3c.dom.Document;

import com.techm.nms.dto.pref.generated.CustomerPreferenceIOType;
import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.sql.dto.NotificationHistory;
import com.techm.nms.sql.dto.QueryCriterion;
import com.techm.nms.sql.dto.UpdateNotificationHistory;
import com.techm.nms.sql.dto.generated.ListOfAccountType;
import com.techm.nms.sql.dto.generated.ListOfSearchResponseType;
import com.techm.nms.sql.dto.generated.NotificationHistoryIOType;
import com.techm.nms.sql.dto.generated.NotificationHistoryResponseIOType;
import com.techm.nms.sql.dto.generated.RecipientAddressType;
import com.techm.nms.sql.dto.generated.SearchResponseType;
import com.techm.nms.util.NotificationUtils;
import com.techm.nms.util.XMLJavaBindingUtil;

/**
 * @author sm0015566
 * 
 */
public class NotificationHistoryDAO {

	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error");

	/** Declaration of debug logger responsible for logging error */
	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	private JdbcTemplate jdbcTemplate;

	private static final String CONTEXT_PATH = "com.techm.nms.sql.dto.generated";

	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	/*
	 * public NotificationHistory getHistory(String serviceMSISDN){ String sql=
	 * "select recipient_addr,service_msisdn,date,msg_id from NOTIFICATION_HIST WHERE SERVICE_MSISDN=?"
	 * ; RowMapper<NotificationHistory> history=new
	 * RowMapper<NotificationHistory>(){ public NotificationHistory
	 * mapRow(ResultSet rs,int rowNum) throws SQLException{ NotificationHistory
	 * history=new NotificationHistory();
	 * history.setMessageId(rs.getString("msg_id"));
	 * history.setRecipientAddr(rs.getString("recipient_addr")); return history;
	 * } }; return (NotificationHistory) jdbcTemplate.queryForObject(sql, new
	 * Object[] {serviceMSISDN},history); }
	 */

	public void insertHistory(final NotificationHistory notificationHistory)
			throws NotificationMgrSQLException {

        final int countDirectRecipient = notificationHistory.getDeliveryAddr().size();
        String insertSql = "insert into NOTF_HIST (NOTF_SCENARIO_ID, RECIPIENT_ID, RECIPIENT_TYPE, DELIVERY_MODE, "
                                   + "INTEGRATION_ID, DELIVERY_ADDR, MSG_ID, SOURCE_NAME, MSG_CONTENT, EMAIL_CONTENT,ATTACH_CONTENT,CREATED_DT, STATUS, STATUS_DESC) "
                                   + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                     
                     jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter(){
                            public void setValues(PreparedStatement ps,int i) throws SQLException{
                                   ps.setInt(1, notificationHistory.getNotfScenarioId());
                                   ps.setString(2, notificationHistory.getRecipientID().get(i));
                                   ps.setString(3, notificationHistory.getRecipientType().get(i));
                                   ps.setString(4, notificationHistory.getDeliveryMode());
                                   ps.setString(5, notificationHistory.getIntegrationID().get(i));
                                   ps.setString(6, notificationHistory.getDeliveryAddr().get(i));
                                   ps.setString(7, notificationHistory.getMessageId());
                                   ps.setString(8, notificationHistory.getSourceName());
                                   ps.setString(9, (notificationHistory.getDeliveryMode()!=null && notificationHistory.getDeliveryMode().equalsIgnoreCase("EMAIL")) ? notificationHistory.getEmailSubject() : notificationHistory.getMsgContent());
                                 //Code added by Sujoy(1st Aug,2017) to store email content as BLOB

                               	   ps.setBlob(10, (notificationHistory.getDeliveryMode()!=null && notificationHistory.getDeliveryMode().equalsIgnoreCase("EMAIL")) ? NotificationUtils.getEmailContent(notificationHistory.getEmailContent()) : null);
                               	//Code added by Sujoy(3rd Nov, 2017) to store attachment content separately
                               	   ps.setBlob(11, (notificationHistory.getDeliveryMode()!=null && notificationHistory.getDeliveryMode().equalsIgnoreCase("EMAIL")) ? NotificationUtils.getEmailContent(notificationHistory.getEmailAttachmentContent()) : null);
                                   ps.setTimestamp(12, new java.sql.Timestamp(System.currentTimeMillis()));
                                   ps.setString(13,notificationHistory.getStatus());
                                   ps.setString(14,notificationHistory.getStatusDescription());
                                  
                            }
                            public int getBatchSize(){
                                   return countDirectRecipient;
                            }
                     });
}

	/*
	 * int countDirectRecipient = notificationHistory.getDeliveryAddr().size();
	 * for (int i = 0; i < countDirectRecipient; i++) {
	 * 
	 * String insertSql = null; String systemDate = "sysdate";
	 * 
	 * if (notificationHistory.getDeliveryMode() != null &&
	 * notificationHistory.getDeliveryMode().equalsIgnoreCase( "Email")) {
	 * insertSql =
	 * "insert into NOTF_HIST (NOTF_SCENARIO_ID, RECIPIENT_ID, RECIPIENT_TYPE, DELIVERY_MODE, INTEGRATION_ID, DELIVERY_ADDR, MSG_ID, SOURCE_NAME, MSG_CONTENT, CREATED_DT, STATUS, STATUS_DESC) values "
	 * + "(" + notificationHistory.getNotfScenarioId() + ",'" +
	 * (notificationHistory.getRecipientID().get(i) != null ?
	 * notificationHistory.getRecipientID().get(i):null) + "','" +
	 * (notificationHistory.getRecipientType().get(i) != null ?
	 * notificationHistory.getRecipientType().get(i):null) + "','" +
	 * notificationHistory.getDeliveryMode() + "','" +
	 * (notificationHistory.getIntegrationID().get(i) != null ?
	 * notificationHistory.getIntegrationID().get(i):null) + "','" +
	 * (notificationHistory.getDeliveryAddr().get(i) != null ?
	 * notificationHistory.getDeliveryAddr().get(i):null) + "','" +
	 * (notificationHistory.getMessageId() != null ?
	 * notificationHistory.getMessageId():null) + "','" +
	 * (notificationHistory.getSourceName() != null ?
	 * notificationHistory.getSourceName():null) + "','" +
	 * (notificationHistory.getEmailSubject() != null ?
	 * notificationHistory.getEmailSubject():null) + "'," + systemDate + ",'" +
	 * notificationHistory.getStatus() + "','" +
	 * notificationHistory.getStatusDescription() + "')"; } else { insertSql =
	 * "insert into NOTF_HIST (NOTF_SCENARIO_ID, RECIPIENT_ID, RECIPIENT_TYPE, DELIVERY_MODE, INTEGRATION_ID, DELIVERY_ADDR, MSG_ID, SOURCE_NAME, MSG_CONTENT, CREATED_DT, STATUS, STATUS_DESC) values "
	 * + "(" + notificationHistory.getNotfScenarioId() + ",'" +
	 * (notificationHistory.getRecipientID().get(i) != null ?
	 * notificationHistory.getRecipientID().get(i) : null) + "','" +
	 * (notificationHistory.getRecipientType()!=null ?
	 * notificationHistory.getRecipientType().get(i):null) + "','" +
	 * notificationHistory.getDeliveryMode() + "','" +
	 * (notificationHistory.getIntegrationID()!=null ?
	 * notificationHistory.getIntegrationID().get(i):null) + "','" +
	 * (notificationHistory.getDeliveryAddr()!=null ?
	 * notificationHistory.getDeliveryAddr().get(i): null) + "','" +
	 * (notificationHistory.getMessageId()!=null ?
	 * notificationHistory.getMessageId():null) + "','" +
	 * (notificationHistory.getSourceName()!=null ?
	 * notificationHistory.getSourceName():null) + "','" +
	 * (notificationHistory.getMsgContent()!=null ?
	 * notificationHistory.getMsgContent():null) + "'," + systemDate + ",'" +
	 * notificationHistory.getStatus() + "','" +
	 * notificationHistory.getStatusDescription() + "')"; } try {
	 * 
	 * jdbcTemplate.update(insertSql);
	 * debugLogger.debug("Data inserted in history DB :: ["+ insertSql+"]");
	 * 
	 * } catch (DataAccessException e) { //e.printStackTrace();
	 * errorLogger.error("Inserting record in history has failed ["+
	 * e.getMessage() + "]"); throw new
	 * NotificationMgrSQLException("Inserting record in history fails["+
	 * e.getMessage() + "]");
	 * 
	 * } }
	 */

	/**
	 * This is to be implemented to change to PreparedStatement
	 * 
	 */
	/*
	 * public void insertHistory(NotificationHistory notificationHistory) throws
	 * NotificationMgrSQLException { int countDirectRecipient =
	 * notificationHistory.getDeliveryAddr().size(); for (int i = 0; i <
	 * countDirectRecipient; i++) {
	 * 
	 * }
	 * 
	 * }
	 */

	public void updateHistory(
			final UpdateNotificationHistory updateNotificationHistory)
			throws NotificationMgrSQLException {
		String updateSql = null;
		updateSql = "UPDATE NOTF_HIST SET STATUS=?,STATUS_DESC=?,UPDATED_DT=? WHERE MSG_ID=?";
		try {
			// System.out.println("********************Final SQl ::::::: ["+
			// sql+"]");
			jdbcTemplate.update(updateSql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, updateNotificationHistory.getStatus());
					ps.setString(2,
							updateNotificationHistory.getStatusDescription());
					ps.setTimestamp(3,
							new java.sql.Timestamp(System.currentTimeMillis()));
					ps.setString(4, updateNotificationHistory.getMessageId());
				}
			});

		} catch (DataAccessException e) {
			// e.printStackTrace();
			errorLogger.error("Updating record in history has failed ["
					+ e.getMessage() + "]");
			throw new NotificationMgrSQLException(
					"Updating record in history fails[" + e.getMessage() + "]");

		}

	}

	public Document retrieveHistory(org.w3c.dom.Document doc)
			throws NotificationMgrGenericException {

		XMLJavaBindingUtil util = new XMLJavaBindingUtil(CONTEXT_PATH);
		if (debugLogger.isDebugEnabled())
			debugLogger.debug("Converting document to object");
		NotificationHistoryIOType history = (NotificationHistoryIOType) util
				.getObject(doc);

		NotificationHistoryDAOUtil daoUtil = new NotificationHistoryDAOUtil();

		String sql = daoUtil.getSQLString(history);
		if (debugLogger.isDebugEnabled())
			debugLogger.debug("SQL to be executed [" + sql + "]");
		NotificationHistoryResponseIOType response = null;

		try {

			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
			response = populateResponse(rs);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new NotificationMgrGenericException(
					"Retrieving resultset fails with message[" + e.getMessage()
							+ "]");
		}
		// Populating XML Document from response java POJO
		Document docResponse = util.populateResponseXML(response);
		return docResponse;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 */
	/*
	 * public void syncCustPref(org.w3c.dom.Document doc) throws
	 * NotificationMgrGenericException {
	 * 
	 * XMLJavaBindingUtil util = new XMLJavaBindingUtil();
	 * CustomerPreferenceIOType custPref = (CustomerPreferenceIOType)
	 * util.getObject(doc); //NotificationHistoryIOType history =
	 * (NotificationHistoryIOType) util.getObject(doc);
	 * 
	 * NotificationHistoryDAOUtil daoUtil = new NotificationHistoryDAOUtil();
	 * 
	 * String sql = daoUtil.getSQLString(custPref);
	 * NotificationHistoryResponseIOType response = null;
	 * 
	 * try {
	 * 
	 * SqlRowSet rs = jdbcTemplate.queryForRowSet(sql); response =
	 * populateResponse(rs); } catch (DataAccessException e) {
	 * e.printStackTrace(); throw new NotificationMgrSQLException(); }
	 * //Populating XML Document from response java POJO Document
	 * docResponse=util.populateResponseXML(response); //return docResponse; }
	 */

	private NotificationHistoryResponseIOType populateResponse(SqlRowSet rs)
			throws NotificationMgrGenericException {

		NotificationHistoryResponseIOType response = new NotificationHistoryResponseIOType();

		System.out.println("SqlRowSet rs : " + rs);
		// List searchResponseList = response.getSearchResponse();
		try {

			response.setListOfSearchResponse(populateSearchResponse(rs));

			// populateSearchResponse(rs,
			// searchResponseList.getSearchResponse());
		} catch (NotificationMgrSQLException e) {
			// System.out.println("Exception in response : "+e);
			e.printStackTrace();
			throw new NotificationMgrGenericException(
					"Exception in populating response", e);
		}
		return response;
	}

	// private void populateSearchResponse(SqlRowSet rs, List
	// searchResponseList) {
	private ListOfSearchResponseType populateSearchResponse(SqlRowSet rs)
			throws NotificationMgrSQLException {

		SearchResponseType searchResponse = null;

		ListOfSearchResponseType listofSearchResponse = new ListOfSearchResponseType();
		List<SearchResponseType> searchResponseList = listofSearchResponse
				.getSearchResponse();
		try {
			while (rs.next()) {
				searchResponse = new SearchResponseType();
				searchResponse.setDeliveryMode(rs.getString("DELIVERY_MODE"));
				searchResponse.setDestinationAddress(rs
						.getString("DELIVERY_ADDR"));
				searchResponse.setId(rs.getString("RECIPIENT_ID"));
				searchResponse.setIdType(rs.getString("RECIPIENT_TYPE"));
				searchResponse.setMessageContent(rs.getString("MSG_CONTENT"));
				searchResponse.setMessageDeliveryTime(rs
						.getString("CREATED_DT"));
				searchResponse.setMessageId(rs.getString("MSG_ID"));
				searchResponse.setScenarioId(rs.getString("NOTF_SCENARIO_ID"));
				// searchResponse.setServiceMSISDN(rs.getString("DELIVERY_MODE"));
				searchResponse.setSourceName(rs.getString("SOURCE_NAME"));
				searchResponse.setStatus(rs.getString("STATUS"));
				searchResponseList.add(searchResponse);
			}
		} catch (Exception e) {

			// logger.log(Level.WARNING,"Exception in populating search response");
			throw new NotificationMgrSQLException(
					"Exception in populating search response[" + e.getMessage()
							+ "]", e);

		}
		System.out.println("SearchResponseList[" + searchResponseList + "]");
		System.out
				.println("ListOfSearchResponse[" + listofSearchResponse + "]");
		return listofSearchResponse;
	}

	/**
	 * 
	 */

	private void displayResultset(SqlRowSet rs) {
		while (rs.next()) {
			System.out.println("[" + rs.getString("") + "]");
		}
	}
}
