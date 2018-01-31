/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.xml.ws.BindingProvider;

import oracle.ucs.messaging.ws.ClientConstants;
import oracle.sdp.messaging.ApplicationInfo;
import oracle.sdp.messaging.MessagingConstants;
import oracle.sdp.messaging.userprefs.UserPrefsManager;
import oracle.sdp.messaging.userprefs.UserPrefsServices;
import oracle.sdp.messaging.userprefs.UserPrefsManager;
import oracle.sdp.messaging.userprefs.UserPrefsException;
import com.techm.nms.exceptions.UserPrefInitiationException;
/**
 * @author sm0015566
 *
 */
public class ConnectionManager {

	private final static ConnectionManager conn=null;
	
	private ConnectionManager(){}
	
	public static ConnectionManager getInstance(){
		if(conn==null)
			return new ConnectionManager();
		else
			return conn;
	}
	
	/**
	 * Method used to populate configuration for using UCS messaging web service
	 * and creating MessagingClient instance from the web service
	 * @param host
	 * @param port
	 * @param username
	 * @param pwd
	 * @return
	 */
	public HashMap<String,Object> populateEnvProps4WS(String host,String port,String username,String pwd){
		HashMap<String, Object> config = new HashMap<String, Object>();
		//http://<host>:<port>/ucs/messaging/webservice
		config.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				 getEndpoint(host,port));
		//config.put(ClientConstants.POLICIES, new String[] {"oracle/wss11_username_token_with_message_protection_client_policy"});
		config.put(BindingProvider.USERNAME_PROPERTY, username);
		config.put(oracle.wsm.security.util.SecurityConstants.Config.CLIENT_CREDS_LOCATION, oracle.wsm.security.util.SecurityConstants.Config.CLIENT_CREDS_LOC_SUBJECT);
		config.put(oracle.wsm.security.util.SecurityConstants.ClientConstants.WSS_CSF_KEY,
				 pwd);
		
		return config;
	}
	
	/**
	 * Method used to populate configuration for using basic initial context
	 * @param host
	 * @param port
	 * @param username
	 * @param pwd
	 * @return
	 */
	public HashMap<String,Object> populateEnvProps(String host,String port,String username,String pwd){
		HashMap<String, Object> config = new HashMap<String, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, NotificationUtils.WEBLOGIC_INITIAL_CTX_FACTORY);
		config.put(Context.PROVIDER_URL, populateContextURL(host, port));
		config.put(Context.SECURITY_PRINCIPAL, username);
		config.put(Context.SECURITY_CREDENTIALS, pwd);
		
		
		return config;
	}
	
	/**
	 * Method used to populate configuration for using UCS/SDP Java API
	 * @param host
	 * @param port
	 * @param username
	 * @param pwd
	 * @return
	 */
	public HashMap<String,Object> populateEnvProps4Java(){
		HashMap<String, Object> config = new HashMap<String, Object>();
		/*config.put(ApplicationInfo.APPLICATION_NAME, "Sample");
		//config.put(ApplicationInfo.SDPM_SECURITY_PRINCIPAL, username);
		config.put(ApplicationInfo.SDPM_JNDI_INITIAL_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		config.put(ApplicationInfo.SDPM_SECURITY_CREDENTIALS, pwd);
		config.put(ApplicationInfo.SDPM_SERVER_JNDI_PROVIDER_URL,"t3://localhost:7001");
		*/
		/*
		config.put("sdpm/JNDIInitialFactory",NotificationUtils.WEBLOGIC_INITIAL_CTX_FACTORY);
		config.put("sdpm/ServerJNDIProviderURL",populateContextURL(host, port));
		config.put("sdpm/SecurityPrincipal", username);
		config.put("sdpm/SecurityCredentials",pwd);*/
		config.put(MessagingConstants.STATUS_LISTENER_THREADS, 35);
		return config;
	}
	public UserPrefsServices getUserPrefServices(Hashtable prop)throws UserPrefInitiationException{
		UserPrefsServices userPref=null;
		try{
		userPref=UserPrefsManager.createUserPrefsServices(prop);
		}catch(UserPrefsException e)
		{
			e.printStackTrace();
			System.out.println("Initation Exception");
			throw new UserPrefInitiationException("Initiation Exception",e);
		}
		return userPref;
	}
	
	private static String populateContextURL(String host,String port){
		return "t3://" + host + ":" + port;
	}
	private static String getEndpoint(String host,String port){
		return "http://" + host + ":" + port + "/ucs/messaging/webservice";
	}
}
