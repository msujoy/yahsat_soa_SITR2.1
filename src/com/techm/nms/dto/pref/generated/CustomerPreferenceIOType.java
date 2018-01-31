
package com.techm.nms.dto.pref.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerPreferenceIOType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerPreferenceIOType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ListOfPreferences" type="{http://www.techm.com/xml/CustomerPreferenceIO}ListOfPreferencesType" minOccurs="0"/>
 *         &lt;element name="Response" type="{http://www.techm.com/xml/CustomerPreferenceIO}ResponseType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPreferenceIOType", namespace = "http://www.techm.com/xml/CustomerPreferenceIO", propOrder = {
    "listOfPreferences",
    "response"
})
public class CustomerPreferenceIOType {

    @XmlElement(name = "ListOfPreferences", namespace = "http://www.techm.com/xml/CustomerPreferenceIO")
    protected ListOfPreferencesType listOfPreferences;
    @XmlElement(name = "Response", namespace = "http://www.techm.com/xml/CustomerPreferenceIO")
    protected ResponseType response;

    /**
     * Gets the value of the listOfPreferences property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfPreferencesType }
     *     
     */
    public ListOfPreferencesType getListOfPreferences() {
        return listOfPreferences;
    }

    /**
     * Sets the value of the listOfPreferences property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfPreferencesType }
     *     
     */
    public void setListOfPreferences(ListOfPreferencesType value) {
        this.listOfPreferences = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseType }
     *     
     */
    public ResponseType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseType }
     *     
     */
    public void setResponse(ResponseType value) {
        this.response = value;
    }

}
