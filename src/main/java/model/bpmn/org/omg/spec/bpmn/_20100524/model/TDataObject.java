//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.08 at 01:28:46 AM CET 
//


package model.bpmn.org.omg.spec.bpmn._20100524.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tDataObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDataObject">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tFlowElement">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.omg.org/spec/BPMN/20100524/MODEL}dataState" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="itemSubjectRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="isCollection" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDataObject", propOrder = {
    "dataState"
})
public class TDataObject
    extends TFlowElement
{

    protected TDataState dataState;
    @XmlAttribute(name = "itemSubjectRef")
    protected QName itemSubjectRef;
    @XmlAttribute(name = "isCollection")
    protected Boolean isCollection;

    /**
     * Gets the value of the dataState property.
     * 
     * @return
     *     possible object is
     *     {@link TDataState }
     *     
     */
    public TDataState getDataState() {
        return dataState;
    }

    /**
     * Sets the value of the dataState property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDataState }
     *     
     */
    public void setDataState(TDataState value) {
        this.dataState = value;
    }

    /**
     * Gets the value of the itemSubjectRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getItemSubjectRef() {
        return itemSubjectRef;
    }

    /**
     * Sets the value of the itemSubjectRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setItemSubjectRef(QName value) {
        this.itemSubjectRef = value;
    }

    /**
     * Gets the value of the isCollection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsCollection() {
        if (isCollection == null) {
            return false;
        } else {
            return isCollection;
        }
    }

    /**
     * Sets the value of the isCollection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCollection(Boolean value) {
        this.isCollection = value;
    }

}
