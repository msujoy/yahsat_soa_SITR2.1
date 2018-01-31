/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.sql.dao;

import java.util.ArrayList;
import java.util.List;

import com.techm.nms.dto.pref.generated.CustomerPreferenceIOType;

/**
 * @author sd00358829
 *
 */
public class CustomerPrefDAOUtil {

	/**
	 * For Update of Customer Preferences stored in database 
	 */
	private String[] populateUpdateCustPrefQry(CustomerPreferenceIOType custPref) {
		
		String accnumber = null;
		String billprofileid = null;
		String msisdn = null;
		String optin = null;
		String phoneno = null;
		String email = null;
		String sqlsms = null;
		String sqlemail = null;
		String notfType = null;
		String notfCat = null;
		List<com.techm.nms.dto.pref.generated.PreferencesType> preferences = custPref.getListOfPreferences().getPreferences();
		System.out.println("preferences.size() : "+preferences.size());
		ArrayList<String> prefList = new ArrayList<String>();
		for (int i = 0; i < preferences.size(); i++) {
			accnumber = preferences.get(i).getAccountNumber();
			billprofileid = preferences.get(i).getBillProfileId();
			msisdn = preferences.get(i).getServiceMSISDN();
			optin = preferences.get(i).getOptIn();
			phoneno = preferences.get(i).getPhoneNumber();
			email = preferences.get(i).getEmailAddress();
			notfType = preferences.get(i).getNotificationType();
			notfCat = preferences.get(i).getNotificationCategory();
			if (notfType.equals("1"))
				{
					prefList.add("UPDATE NOTF_CUSTOMER_PREF SET OPT_IN = '"+optin+"',NOTF_DEST_ADDR = '"+phoneno+"',MODIFY_DT = sysdate WHERE NOTF_ID = '"+accnumber+"' and NOTF_CAT_ID = '"+notfCat+"' and DELIVERY_MODE = 'SMS'");
					prefList.add("UPDATE NOTF_CUSTOMER_PREF SET OPT_IN = '"+optin+"',NOTF_DEST_ADDR = '"+email+"',MODIFY_DT = sysdate WHERE NOTF_ID = '"+accnumber+"' and NOTF_CAT_ID = '"+notfCat+"' and DELIVERY_MODE = 'Email'");
				}
			if (notfType.equals("2"))
				{
					prefList.add("UPDATE NOTF_CUSTOMER_PREF SET OPT_IN = '"+optin+"',NOTF_DEST_ADDR = '"+phoneno+"',MODIFY_DT = sysdate WHERE NOTF_ID = '"+billprofileid+"' and NOTF_CAT_ID = '"+notfCat+"' and DELIVERY_MODE = 'SMS'");
					prefList.add("UPDATE NOTF_CUSTOMER_PREF SET OPT_IN = '"+optin+"',NOTF_DEST_ADDR = '"+email+"',MODIFY_DT = sysdate WHERE NOTF_ID = '"+billprofileid+"' and NOTF_CAT_ID = '"+notfCat+"' and DELIVERY_MODE = 'Email'");
				}
			if (notfType.equals("3"))
				{
					prefList.add("UPDATE NOTF_CUSTOMER_PREF SET OPT_IN = '"+optin+"',NOTF_DEST_ADDR = '"+phoneno+"',MODIFY_DT = sysdate WHERE NOTF_ID = '"+msisdn+"' and NOTF_CAT_ID = '"+notfCat+"' and DELIVERY_MODE = 'SMS'");
					prefList.add("UPDATE NOTF_CUSTOMER_PREF SET OPT_IN = '"+optin+"',NOTF_DEST_ADDR = '"+email+"',MODIFY_DT = sysdate WHERE NOTF_ID = '"+msisdn+"' and NOTF_CAT_ID = '"+notfCat+"' and DELIVERY_MODE = 'Email'");
				}
			else{
				System.out.println("No match found");
			}
		}
		String[] prefs = new String[prefList.size()];
		prefs = prefList.toArray(prefs);
		System.out.println("Update statement[" + prefs + "]");
		return prefs;
	}
	
	public String[] getSQLString(CustomerPreferenceIOType custPref){
		
		String SQL[] = populateUpdateCustPrefQry(custPref);
		return SQL;
	}
}
