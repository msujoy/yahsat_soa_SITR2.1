//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.27 at 12:51:19 PM IST 
//


package com.techm.nms.sql.dto.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServicesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServicesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ListOfSearchSpec" type="{http://www.techm.org/notification}ListOfSearchSpecType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServicesType", propOrder = {
    "listOfSearchSpec"
})
public class ServicesType {

    @XmlElement(name = "ListOfSearchSpec")
    protected ListOfSearchSpecType listOfSearchSpec;

    /**
     * Gets the value of the listOfSearchSpec property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfSearchSpecType }
     *     
     */
    public ListOfSearchSpecType getListOfSearchSpec() {
        return listOfSearchSpec;
    }

    /**
     * Sets the value of the listOfSearchSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfSearchSpecType }
     *     
     */
    public void setListOfSearchSpec(ListOfSearchSpecType value) {
        this.listOfSearchSpec = value;
    }

}
