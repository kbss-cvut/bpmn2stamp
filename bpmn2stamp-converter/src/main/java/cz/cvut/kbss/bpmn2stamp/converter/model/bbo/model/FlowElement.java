
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


/**
 * FlowElement is the abstract super class for all elements that can appear in a Process flow, which are FlowNodes (see page 99, which consist of Activities (see page 151), Choreography Activities (see page 321) Gateways (see page 287), and Events (see page 233), Data Objects (see page 205), Data Associations (see page 221), and Sequence Flows (see page 97).
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_FlowElement)
public class FlowElement
        extends Thing
    implements Serializable
{
    /**
     * [IRIT] this relation references the FlowElementContainer that includes the FlowElement
     * 
     */
    @Inferred
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_container)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_FlowElementsContainer, min = 1, max = 1)
    })
    protected Set<FlowElementsContainer> has_container;
    /**
     * The descriptive name of the element.
     * 
     */
    @OWLDataProperty(iri = Vocabulary.s_p_name)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = "http://www.w3.org/2001/XMLSchema#string", max = 1)
    })
    protected String name1;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setProperties(Map<String, Set<String>> properties) {
        this.properties = properties;
    }

    public Map<String, Set<String>> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return ((((("FlowElement {"+ name)+"<")+ id)+">")+"}");
    }

    public void setHas_container(Set<FlowElementsContainer> has_container) {
        this.has_container = has_container;
    }

    public Set<FlowElementsContainer> getHas_container() {
        return has_container;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName1() {
        return name1;
    }

}
