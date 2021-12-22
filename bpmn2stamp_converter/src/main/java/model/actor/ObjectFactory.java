//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.25 at 01:28:16 AM CET 
//


package model.actor;

import model.actor.element.Membership;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.bonitasoft.ns.actormapping._6 package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.bonitasoft.ns.actormapping._6
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Membership }
     * 
     */
    public Membership createMembership() {
        return new Membership();
    }

    /**
     * Create an instance of {@link ActorMappings }
     * 
     */
    public ActorMappings createActorMappings() {
        return new ActorMappings();
    }

    /**
     * Create an instance of {@link ActorMapping }
     * 
     */
    public ActorMapping createActorMapping() {
        return new ActorMapping();
    }

    /**
     * Create an instance of {@link Groups }
     * 
     */
    public Groups createGroups() {
        return new Groups();
    }

    /**
     * Create an instance of {@link Users }
     * 
     */
    public Users createUsers() {
        return new Users();
    }

    /**
     * Create an instance of {@link Roles }
     * 
     */
    public Roles createRoles() {
        return new Roles();
    }

    /**
     * Create an instance of {@link Membership }
     * 
     */
    public Membership createMembershipMembership() {
        return new Membership();
    }

}