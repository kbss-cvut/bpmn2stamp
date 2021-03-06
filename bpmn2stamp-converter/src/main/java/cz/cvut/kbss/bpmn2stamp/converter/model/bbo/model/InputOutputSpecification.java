
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


/**
 * /
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_InputOutputSpecification)
public class InputOutputSpecification
        extends Thing
    implements Serializable
{
    /**
     * [IRIT]denotes the activity related to the source element
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_activity)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Activity, max = 1)
    })
    protected Set<Thing> has_activity;
    /**
     * A reference to the InputSets defined by the InputOutputSpecification. Every InputOutputSpecification MUST define at least one InputSet.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_inputSet)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_InputSet, min = 1, max = -1)
    })
    protected Set<Thing> has_inputSet;
    /**
     * A reference to the OutputSets defined by the InputOutputSpecification. Every InputOutputSpecification MUST define at least one OutputSet.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_outputSet)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_OutputSet, min = 1, max = -1)
    })
    protected Set<OutputSet> has_outputSet;

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
        return ((((("InputOutputSpecification {"+ name)+"<")+ id)+">")+"}");
    }

    public void setHas_activity(Set<Thing> has_activity) {
        this.has_activity = has_activity;
    }

    public Set<Thing> getHas_activity() {
        return has_activity;
    }

    public void setHas_inputSet(Set<Thing> has_inputSet) {
        this.has_inputSet = has_inputSet;
    }

    public Set<Thing> getHas_inputSet() {
        return has_inputSet;
    }

    public void setHas_outputSet(Set<OutputSet> has_outputSet) {
        this.has_outputSet = has_outputSet;
    }

    public Set<OutputSet> getHas_outputSet() {
        return has_outputSet;
    }

}
