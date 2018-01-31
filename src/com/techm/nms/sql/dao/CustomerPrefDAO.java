/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dao;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.rowset.SqlRowSet;
//import org.w3c.dom.Document;

import com.techm.nms.dto.pref.generated.CustomerPreferenceIOType;
import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.exceptions.NotificationMgrSQLException;
import com.techm.nms.util.XMLJavaBindingUtil;

/**
 * @author sd00358829
 *
 */
public class CustomerPrefDAO {

private JdbcTemplate jdbcTemplate;
	
	private static final String CONTEXT_PATH = "com.techm.nms.dto.pref.generated";
	public void setDataSource(DataSource ds){
		this.jdbcTemplate=new JdbcTemplate(ds);
	}
	/**
	 * 
	 * @return
	 */
	public void syncCustPref(org.w3c.dom.Document doc)
			throws NotificationMgrGenericException {

		XMLJavaBindingUtil util = new XMLJavaBindingUtil(CONTEXT_PATH);
		CustomerPreferenceIOType custPref = (CustomerPreferenceIOType) util.getObject(doc);
		CustomerPrefDAOUtil daoUtil = new CustomerPrefDAOUtil();
		String sql[] = daoUtil.getSQLString(custPref);
		try {
			jdbcTemplate.batchUpdate(sql);
			System.out.println("SQL Update statement executed successfully");
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new NotificationMgrSQLException();
		}
	}

}
