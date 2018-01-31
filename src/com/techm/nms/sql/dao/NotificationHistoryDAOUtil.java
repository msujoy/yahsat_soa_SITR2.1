/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dao;

import java.util.List;

import org.apache.log4j.Logger;

import weblogic.wsee.jaxws.spi.ClientInstance.Listener;

import com.techm.nms.dto.pref.generated.CustomerPreferenceIOType;
import com.techm.nms.dto.pref.generated.ListOfPreferencesType;
import com.techm.nms.sql.dto.generated.AccountType;
import com.techm.nms.sql.dto.generated.AssetType;
import com.techm.nms.sql.dto.generated.BillingProfileType;
import com.techm.nms.sql.dto.generated.ListOfAccountType;
import com.techm.nms.sql.dto.generated.NotificationHistoryIOType;
import com.techm.nms.sql.dto.generated.PreferencesType;
import com.techm.nms.sql.dto.generated.RecipientAddressType;
import com.techm.nms.sql.dto.generated.ServicesType;

/**
 * @author Sourav Dey
 * 
 */
public class NotificationHistoryDAOUtil {
	
	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error"); 

	/** Declaration of debug logger responsible for logging error */
	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	private String populateRecipientAddrINQry(NotificationHistoryIOType history) {
		
		List<RecipientAddressType> recipientAddress = history
				.getListOfRecipientAddress().getRecipientAddress();
		StringBuffer deliveryAddrs = new StringBuffer();
		if(recipientAddress!=null){
		for (int i = 0; i < recipientAddress.size(); i++) {
			String msisdn = recipientAddress.get(i).getQueryCriteria()
					.getMSISDN();
			String emailAddr=recipientAddress.get(i).getQueryCriteria()
					.getEmailAddress();
			if (i == recipientAddress.size()-1)
				{
					deliveryAddrs.append(!msisdn.equals("") ? "'"+ msisdn + "'": "");
					deliveryAddrs.append(!emailAddr.equals("") ? "'" + emailAddr + "'": "");
				}
			else
				{
					deliveryAddrs.append(!msisdn.equals("") ? "'" + msisdn + "'" + "," : "");
					deliveryAddrs.append(!emailAddr.equals("") ? "'" + emailAddr + "'" + "," : "");
				}
		}
		}
		debugLogger.debug("Delivery Addr[" + deliveryAddrs.toString() + "]");
		return deliveryAddrs.toString();
	}
	
	private String populateRecipientIDINQry(NotificationHistoryIOType history) {

		StringBuffer searchSpecs = new StringBuffer();
		boolean flag = false;
		
		List<AccountType> accounts = history.getListOfAccount().getAccount();
		
		for (int i = 0; i < accounts.size(); i++) {
			
			if(accounts.get(i).getListOfSearchSpec()!=null)
			{
				String accNumber = accounts.get(i).getListOfSearchSpec().getSearchSpec().get(0).getQueryCriteria().getId();
				if (searchSpecs.length()==0){
					searchSpecs.append(!accNumber.equals("") ? "'"+ accNumber + "'": "");
				}
				else
					searchSpecs.append(!accNumber.equals("") ? ","+ "'" + accNumber + "'" : "");
			}
			List<BillingProfileType> billingProfiles = accounts.get(i).getListOfBillingProfile().getBillingProfile();
			
			for (int j = 0; j < billingProfiles.size(); j++){
				
				if(billingProfiles.get(j).getListOfSearchSpec()!=null){
				String billProfileId = billingProfiles.get(j).getListOfSearchSpec().getSearchSpec().get(0).getQueryCriteria().getId();
				if(searchSpecs.length()==0)
					searchSpecs.append(!billProfileId.equals("") ? "'"+ billProfileId + "'": "");
				else
					searchSpecs.append(!billProfileId.equals("") ?","+ "'"+ billProfileId + "'": "");
				}
				List<ServicesType> services = billingProfiles.get(j).getListOfServices().getServices();
				
				for(int k = 0; k < services.size(); k++){
					
					if(services.get(k).getListOfSearchSpec()!=null){
					String serviceId = services.get(k).getListOfSearchSpec().getSearchSpec().get(0).getQueryCriteria().getId();
					
					if(searchSpecs.length()==0)
						searchSpecs.append(!serviceId.equals("") ? "'"+ serviceId + "'": "");
					else
						searchSpecs.append(!serviceId.equals("") ? ","+"'"+ serviceId + "'": "");
					}
				}
			}
		}
			debugLogger.debug("Recipient Ids[" + searchSpecs.toString() + "]");
			
		return searchSpecs.toString();
	}
	private String populateIntegrationIDINQry(NotificationHistoryIOType history) {

		StringBuffer searchSpecs = new StringBuffer();
		
		List<AccountType> accounts = history.getListOfAccount().getAccount();
		
		for (int i = 0; i < accounts.size(); i++) {
			List<BillingProfileType> billingProfiles = accounts.get(i).getListOfBillingProfile().getBillingProfile();
			
			for (int j = 0; j < billingProfiles.size(); j++){
				List<AssetType> asset = billingProfiles.get(j).getListOfServices().getAsset();
				
				for(int l = 0; l < asset.size(); l++)
				{
					if(asset.get(l).getListOfSearchSpec()!=null){
						String assetIntegrationId = asset.get(l).getListOfSearchSpec().getSearchSpec().get(0).getQueryCriteria().getId();
						
						if(searchSpecs.length()==0)
							searchSpecs.append(!assetIntegrationId.equals("") ? "'"+ assetIntegrationId + "'": "");
						else
							searchSpecs.append(!assetIntegrationId.equals("") ? ","+"'"+ assetIntegrationId + "'": "");
					}
				}
			}
		}
		debugLogger.debug("Integration Ids[" + searchSpecs.toString() + "]");
		return searchSpecs.toString();
	}
	/**
	 * 
	 */
	/* private String populateUpdateCustPrefQry(CustomerPreferenceIOType custPref) {

		//List<RecipientAddressType> recipientAddress = history.getListOfRecipientAddress().getRecipientAddress();
		List<PreferencesType> preferences = custPref.getListOfPreferences().getPreferences();
		StringBuffer prefs = new StringBuffer();
		for (int i = 0; i < preferences.size(); i++) {
			String accnumber = preferences.get(i).getAccountNumber();
			String billprofileid = preferences.get(i).getBillProfileId();
			if (i == preferences.size()-1)
				{
					deliveryAddrs.append(!msisdn.equals("") ? "'"+ msisdn + "'": "");
					deliveryAddrs.append(!emailAddr.equals("") ? "'" + emailAddr + "'": "");
				}
			else
				{
					deliveryAddrs.append(!msisdn.equals("") ? "'" + msisdn + "'" + "," : "");
					deliveryAddrs.append(!emailAddr.equals("") ? "'" + emailAddr + "'" + "," : "");
				}
		}
		System.out.println("Delivery Addr[" + deliveryAddrs.toString() + "]");
		return deliveryAddrs.toString();
	} */
	
	/**
	 * To Populate the SQL string 
	 */
	public String getSQLString(NotificationHistoryIOType history)
	  {
		
	    String recipientAddrIN = history
				.getListOfRecipientAddress()!=null? populateRecipientAddrINQry(history):"";
		System.out.println("recipientAddrIN : "+recipientAddrIN);
	    String recipientIDIN = history.getListOfAccount()!=null?populateRecipientIDINQry(history):"";
	    System.out.println("recipientIDIN : "+recipientIDIN);
	    String integrationIDIN = history.getListOfAccount()!=null?populateIntegrationIDINQry(history):"";
	    System.out.println("integrationIDIN : "+integrationIDIN);
	    String SQL = "";
	    if(!recipientAddrIN.equals("")){
	    	SQL+="select * from NOTF_HIST where DELIVERY_ADDR in (" + recipientAddrIN + ") order by CREATED_DT desc";
	    }
	    if(!recipientIDIN.equals("")){
	    	//String sqlRecipientId="select * from NOTF_HIST where RECIPIENT_ID in (" + recipientIDIN + ") order by CREATED_DT desc";
	    	String sqlRecipientId="(select * from NOTF_HIST where RECIPIENT_ID in (" + recipientIDIN + "))";
	    	if(SQL!="")
	    		SQL+=" UNION " + sqlRecipientId ;
	    	else
	    		SQL+=sqlRecipientId;
	    }
	    if(!integrationIDIN.equals("")){
	    	//String sql="select * from NOTF_HIST where INTEGRATION_ID in (" + integrationIDIN + ") order by CREATED_DT desc";
	    	String sqlIntegrationId="(select * from NOTF_HIST where INTEGRATION_ID in (" + integrationIDIN + "))";
	    	if(SQL!="")
	    		SQL+=" UNION " + sqlIntegrationId ;
	    	else
	    		SQL+=sqlIntegrationId;
	    }	
	    /*
	    if ((!recipientAddrIN.equals("")) && (!recipientIDIN.equals("")) && (!integrationIDIN.equals(""))) {
		      SQL = "select * from NOTF_HIST where DELIVERY_ADDR in (" + recipientAddrIN + ") UNION select * from NOTF_HIST where RECIPIENT_ID in (" + recipientIDIN + ") UNION select * from NOTF_HIST where INTEGRATION_ID in (" + integrationIDIN + ")";
		    }
	    
	    if ((!recipientAddrIN.equals("")) && (recipientIDIN.equals("") && integrationIDIN.equals(""))) {
	      SQL = "select * from NOTF_HIST where DELIVERY_ADDR in (" + recipientAddrIN + ")";
	    }
	    if ((recipientAddrIN.equals("")) && (!recipientIDIN.equals(""))) {
	      SQL = "select * from NOTF_HIST where RECIPIENT_ID in (" + recipientIDIN + ")";
	    }
	    if ((recipientAddrIN.equals("")) && (!integrationIDIN.equals(""))) {
		      SQL = "select * from NOTF_HIST where INTEGRATION_ID in (" + integrationIDIN + ")";
		}*/
	    String finalExecuteSQL = "select c.* from ("+SQL+")  c order by c.created_dt desc";
	    debugLogger.debug("Full SQL string : " + finalExecuteSQL);
	    return finalExecuteSQL;
	  }
	/* public String getSQLStringPreference(CustomerPreferenceIOType custPref){
		
		String recipientAddrIN = populateUpdateCustPrefQry(custPref);
		String SQL="select * from NOTF_HIST where DELIVERY_ADDR in ("+recipientAddrIN+") UNION select * from NOTF_HIST where DELIVERY_ADDR in ("+recipientIDIN+")";
		System.out.println("Full SQL string : "+SQL);
		return SQL;
	} */
	
}
