
package model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import model.bbo.Vocabulary;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


/**
 * /
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_FlowElementsContainer)
public class FlowElementsContainer
        extends Thing
    implements Serializable
{
    /**
     * This association specifies the particular flow elements contained in a
     * FlowElementContainer. Flow elements are Events, Gateways, Sequence
     * Flows, Activities, Data Objects, Data Associations, and Choreography
     * Activities.
     *
     */
    //TODO [review] property should be defined here, add missing constraints
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_flowElements)
    protected Set<Thing> has_flowElements;

    public Set<Thing> getHas_flowElements() {
        return has_flowElements;
    }

    public void setHas_flowElements(Set<Thing> has_flowElements) {
        this.has_flowElements = has_flowElements;
    }

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
        return ((((("FlowElementsContainer {"+ name)+"<")+ id)+">")+"}");
    }

}
