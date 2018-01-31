/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.mgmt;

import java.util.HashMap;

import org.apache.log4j.Logger;

import oracle.sdp.messaging.MessagingClientFactory;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.listener.NotificationStatusListener4Java;
import com.techm.nms.util.ConnectionManager;

/**
 * @author sm0015566
 *
 */
public class MessagingClientConnectionMgr {

	private static oracle.sdp.messaging.MessagingClient client;

	private static NotificationStatusListener4Java listener;
	
	

	private static MessagingClientConnectionMgr instance;
	
	/** Declaration of error logger responsible for logging error */
	private static Logger errorLogger = Logger.getLogger("com.techm.nms.error");

	/** Declaration of debug logger responsible for logging error */

	private static Logger debugLogger = Logger.getLogger("com.techm.nms.trace");

	private MessagingClientConnectionMgr(){
		
	}
	
	public static MessagingClientConnectionMgr getInstance(String host,String port,String username,String pwd) throws NotificationMgrGenericException{
		
		if(instance==null)
			instance=new MessagingClientConnectionMgr(host, port, username, pwd);
		return instance;
	}
	
	private MessagingClientConnectionMgr(String host,String port,String username,String pwd)throws NotificationMgrGenericException{

		ConnectionManager con = ConnectionManager.getInstance();
		HashMap<String, Object> props = con.populateEnvProps4Java();
		
		createMessagingClient(props);
		System.out.println("Created Messaging Client collection");
	}
	
	private void createMessagingClient( HashMap<String, Object> props)throws NotificationMgrGenericException{
		try {
			client = MessagingClientFactory.createMessagingClient(props);
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("Created Messaging Client Instance with Object Id [" + client + "]");
			listener=new NotificationStatusListener4Java();
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("Listener have been initiated for [" + client + "]");
			client.setMessageListener(listener);
			if(debugLogger.isDebugEnabled())
				debugLogger.debug("Message Listener has been set sucessfully [" + client + "]");
			
		} catch (oracle.sdp.messaging.MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException("Creating Messaging Client failed with reason [" + e.getMessage() + "]",e);
		} catch(Exception e)
		{
			e.printStackTrace();
			throw new NotificationMgrGenericException("Generic Exception [" + e.getMessage() + "]",e);
		}
		//messagingClient.put("MessagingClient_" + messagingClient.size()+1, client); //Putting this in global collection
	}
	
	/**
	 * @return the client
	 */
	public static oracle.sdp.messaging.MessagingClient getClient() {
		return client;
	}
	/**
	 * @return the listener
	 */
	public static NotificationStatusListener4Java getListener() {
		return listener;
	}
}
