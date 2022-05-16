//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.25 at 01:17:54 AM CET 
//


package cz.cvut.kbss.bpmn2stamp.converter.model.organization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Organization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Organization">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customUserInfoDefinitions" type="{http://documentation.bonitasoft.com/organization-xml-schema/1.1}CustomUserInfoDefinitions" minOccurs="0"/>
 *         &lt;element name="users" type="{http://documentation.bonitasoft.com/organization-xml-schema/1.1}Users" minOccurs="0"/>
 *         &lt;element name="roles" type="{http://documentation.bonitasoft.com/organization-xml-schema/1.1}Roles" minOccurs="0"/>
 *         &lt;element name="groups" type="{http://documentation.bonitasoft.com/organization-xml-schema/1.1}Groups" minOccurs="0"/>
 *         &lt;element name="memberships" type="{http://documentation.bonitasoft.com/organization-xml-schema/1.1}Memberships" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Organization", propOrder = {
    "customUserInfoDefinitions",
    "users",
    "roles",
    "groups",
    "memberships"
})
public class Organization {

    protected CustomUserInfoDefinitions customUserInfoDefinitions;
    protected Users users;
    protected Roles roles;
    protected Groups groups;
    protected Memberships memberships;

    /**
     * Gets the value of the customUserInfoDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link CustomUserInfoDefinitions }
     *     
     */
    public CustomUserInfoDefinitions getCustomUserInfoDefinitions() {
        return customUserInfoDefinitions;
    }

    /**
     * Sets the value of the customUserInfoDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomUserInfoDefinitions }
     *     
     */
    public void setCustomUserInfoDefinitions(CustomUserInfoDefinitions value) {
        this.customUserInfoDefinitions = value;
    }

    /**
     * Gets the value of the users property.
     * 
     * @return
     *     possible object is
     *     {@link Users }
     *     
     */
    public Users getUsers() {
        return users;
    }

    /**
     * Sets the value of the users property.
     * 
     * @param value
     *     allowed object is
     *     {@link Users }
     *     
     */
    public void setUsers(Users value) {
        this.users = value;
    }

    /**
     * Gets the value of the roles property.
     * 
     * @return
     *     possible object is
     *     {@link Roles }
     *     
     */
    public Roles getRoles() {
        return roles;
    }

    /**
     * Sets the value of the roles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Roles }
     *     
     */
    public void setRoles(Roles value) {
        this.roles = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link Groups }
     *     
     */
    public Groups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Groups }
     *     
     */
    public void setGroups(Groups value) {
        this.groups = value;
    }

    /**
     * Gets the value of the memberships property.
     * 
     * @return
     *     possible object is
     *     {@link Memberships }
     *     
     */
    public Memberships getMemberships() {
        return memberships;
    }

    /**
     * Sets the value of the memberships property.
     * 
     * @param value
     *     allowed object is
     *     {@link Memberships }
     *     
     */
    public void setMemberships(Memberships value) {
        this.memberships = value;
    }

}