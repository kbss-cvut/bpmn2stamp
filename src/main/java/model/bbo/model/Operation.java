
package model.bbo.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.jopa.model.annotations.Properties;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import model.bbo.Vocabulary;


/**
 * An Operation defines Messages that are consumed and, optionally, produced when the Operation is called. It can also define zero or more errors that are returned when operation fails. The Operation inherits the attributes and model associations of BaseElement
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Operation)
public class Operation
        extends Thing
    implements Serializable
{
    @OWLObjectProperty(iri = Vocabulary.s_p_has_inMessageRef)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Message, min = 1, max = 1)
    })
    protected Message has_inMessageRef;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_operationInterface)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Interface, min = 1, max = 1)
    })
    protected Interface has_operationInterface;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_outMessageRef)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Message, max = 1)
    })
    protected Set<Thing> has_outMessageRef;

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
        return ((((("Operation {"+ name)+"<")+ id)+">")+"}");
    }

    public void setHas_inMessageRef(Message has_inMessageRef) {
        this.has_inMessageRef = has_inMessageRef;
    }

    public Message getHas_inMessageRef() {
        return has_inMessageRef;
    }

    public void setHas_operationInterface(Interface has_operationInterface) {
        this.has_operationInterface = has_operationInterface;
    }

    public Interface getHas_operationInterface() {
        return has_operationInterface;
    }

    public void setHas_outMessageRef(Set<Thing> has_outMessageRef) {
        this.has_outMessageRef = has_outMessageRef;
    }

    public Set<Thing> getHas_outMessageRef() {
        return has_outMessageRef;
    }

}
