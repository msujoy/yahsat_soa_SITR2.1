
package com.techm.nms.dto.pref.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.techm.nms.dto.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CustomerPreferenceIO_QNAME = new QName("http://www.techm.com/xml/CustomerPreferenceIO", "CustomerPreferenceIO");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.techm.nms.dto.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListOfPreferencesType }
     * 
     */
    public ListOfPreferencesType createListOfPreferencesType() {
        return new ListOfPreferencesType();
    }

    /**
     * Create an instance of {@link PreferencesType }
     * 
     */
    public PreferencesType createPreferencesType() {
        return new PreferencesType();
    }

    /**
     * Create an instance of {@link ResponseType }
     * 
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link CustomerPreferenceIOType }
     * 
     */
    public CustomerPreferenceIOType createCustomerPreferenceIOType() {
        return new CustomerPreferenceIOType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerPreferenceIOType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.techm.com/xml/CustomerPreferenceIO", name = "CustomerPreferenceIO")
    public JAXBElement<CustomerPreferenceIOType> createCustomerPreferenceIO(CustomerPreferenceIOType value) {
        return new JAXBElement<CustomerPreferenceIOType>(_CustomerPreferenceIO_QNAME, CustomerPreferenceIOType.class, null, value);
    }

}
