
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
 * This element controls when and which Events are thrown in case behavior of the Multi-Instance Activity is set to complex.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ComplexBehaviorDefinition)
public class ComplexBehaviorDefinition
        extends Thing
    implements Serializable
{
    /**
     * This relation has two source concepts. The definition is related to the source concept: (1) conditionalEventDefinition: The Expression might be underspecified and provided in the form of natural language. For executable Processes (isExecutable = true), if the trigger is Conditional, then a FormalExpression MUST be entered. (2) complexBehaviorDefinition: This attribute defines a boolean Expression that when evaluated to true, cancels the remaining Activity instances and produces a token.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_condition)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_FormalExpression, min = 1, max = 1)
    })
    protected Set<Thing> has_condition;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_event)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_ImplicitThrowEvent, max = 1)
    })
    protected Set<Thing> has_event;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_multiInstanceLoopCharacteristics)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_MultiInstanceLoopCharacteristics, min = 1, max = 1)
    })
    protected Set<Thing> has_multiInstanceLoopCharacteristics;

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
        return ((((("ComplexBehaviorDefinition {"+ name)+"<")+ id)+">")+"}");
    }

    public void setHas_condition(Set<Thing> has_condition) {
        this.has_condition = has_condition;
    }

    public Set<Thing> getHas_condition() {
        return has_condition;
    }

    public void setHas_event(Set<Thing> has_event) {
        this.has_event = has_event;
    }

    public Set<Thing> getHas_event() {
        return has_event;
    }

    public void setHas_multiInstanceLoopCharacteristics(Set<Thing> has_multiInstanceLoopCharacteristics) {
        this.has_multiInstanceLoopCharacteristics = has_multiInstanceLoopCharacteristics;
    }

    public Set<Thing> getHas_multiInstanceLoopCharacteristics() {
        return has_multiInstanceLoopCharacteristics;
    }

}
