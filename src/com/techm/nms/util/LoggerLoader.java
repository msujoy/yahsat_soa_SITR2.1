/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author dg00352798
 *
 */
public class LoggerLoader {
	
	private static LoggerLoader loader;
	
	public static LoggerLoader getInstance(String logFileLocation){
	if(loader==null)
		loader=new LoggerLoader(logFileLocation);
	return loader;
	}
	
	private LoggerLoader(String logFileLocation){
		System.out.println("Inside LoggerLoader before calling DOMConfigurator");
		try{
			DOMConfigurator.configure(logFileLocation);
		}catch(FactoryConfigurationError e)
		{
			e.printStackTrace();
		}
		System.out.println("Log4J has been uploaded properly");
	}


}
