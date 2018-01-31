/**
 * Author   : Sujoy Mondal
 * Version  : 
 * Purpose  :
 */
package com.techm.nms.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import oracle.xml.parser.v2.XMLDocument;

import com.techm.nms.exceptions.NotificationMgrGenericException;
import com.techm.nms.sql.dto.generated.NotificationHistoryResponseIOType;
import com.techm.nms.sql.dto.generated.ObjectFactory;

/**
 * @author sd00358829
 * 
 */
public class XMLJavaBindingUtil {

	private JAXBContext jaxb;
	private static final Logger logger=Logger.getLogger("XMLJavaBindingUtil");
	
	public XMLJavaBindingUtil(String contextPath) throws NotificationMgrGenericException{
		try {
			jaxb = javax.xml.bind.JAXBContext.newInstance(contextPath);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException("Creating JAXBContext failed",e);
		}
	}

	private Object getPOJO(org.w3c.dom.Document doc, Unmarshaller unmarshaller) throws NotificationMgrGenericException{
		Object obj = null;
		try {
			obj = unmarshaller.unmarshal(doc);

			JAXBIntrospector jaxbIntrospector = jaxb.createJAXBIntrospector();
			obj = jaxbIntrospector.getValue(obj);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException("Populating Java POJO from XML Document failed[" + e.getMessage() +"]",e);

		}
		return obj;
	}

	private Document populateXML(NotificationHistoryResponseIOType responseObj,
			Marshaller jaxbMarshaller) throws NotificationMgrGenericException {

		//Creating blank document placeholder
		Document doc = createDocument();
		ObjectFactory factory=new ObjectFactory();
		
		try {
			jaxbMarshaller.marshal(factory.createNotificationHistoryResponseIO(responseObj), doc);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException(
					"Marshalling from response object to XML Document failed",
					e);
		}
		//logger.debug("Document after marshalling[" + doc.getDocumentElement().getTextContent() + "]");
		return doc;
	}

	private javax.xml.bind.Unmarshaller getUnmarshaller() throws NotificationMgrGenericException{

		Unmarshaller jaxbUnmarshaller = null;
		try {

			jaxbUnmarshaller = jaxb.createUnmarshaller();
			logger.debug("XMLJavaBindingUtil jaxbUnmarshaller : "
					+ jaxbUnmarshaller);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new NotificationMgrGenericException("Creating unmarshaller failed[" + e.getMessage() + "]",e);

		}
		return jaxbUnmarshaller;

	}

	private javax.xml.bind.Marshaller getMarshaller() {

		Marshaller jaxbMarshaller = null;
		try {

			jaxbMarshaller = jaxb.createMarshaller();
			logger.debug("XMLJavaBindingUtil jaxbUnmarshaller : "
					+ jaxbMarshaller);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return jaxbMarshaller;

	}

	public Object getObject(org.w3c.dom.Document doc) throws NotificationMgrGenericException{

		Object pojo = getPOJO(doc, getUnmarshaller());

		return pojo;
	}

	public Document populateResponseXML(
			NotificationHistoryResponseIOType responseObj) throws NotificationMgrGenericException {

		Document doc = populateXML(responseObj, getMarshaller());
		return doc;
	}

	private Document createDocument() {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = db.newDocument();

		return doc;
	}
}
