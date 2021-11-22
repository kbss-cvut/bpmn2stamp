
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * Work that a company or organization performs using business processes. An activity can be atomic or non-atomic (compound). The types of activities that are a part of a Process Model are: Process, Sub-Process, and Task.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Activity)
public class Activity
    extends FlowNode
    implements Serializable
{

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other outgoing Sequence Flows evaluate to true. The default Sequence Flow should not have a conditionExpression. Any such Expression SHALL be ignored.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_default)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_SequenceFlow, max = 1)
    })
    protected Set<Thing> has_default;
    /**
     * The InputOutputSpecification defines the inputs and outputs and the InputSets and OutputSets for the Activity. See page 211 for more information on the InputOutputSpecification.
     * 
     * 
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_ioSpecification)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_InputOutputSpecification, max = 1)
    })
    protected Set<Thing> has_ioSpecification;
    /**
     * An Activity MAY be performed once or MAY be repeated. If repeated,
     * the Activity MUST have loopCharacteristics that define the repetition
     * criteria (if the isExecutable attribute of the Process is set to
     * true).
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_loopCharacteristics)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_LoopCharacteristics, max = 1)
    })
    protected Set<Thing> has_loopCharacteristics;
    /**
     * Allows to specifiy where the activity or the process will take place
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_takesPlaceAt)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_ManufacturingFacility, max = 1)
    })
    protected Set<Thing> takesPlaceAt;

    public void setHas_default(Set<Thing> has_default) {
        this.has_default = has_default;
    }

    public Set<Thing> getHas_default() {
        return has_default;
    }

    public void setHas_ioSpecification(Set<Thing> has_ioSpecification) {
        this.has_ioSpecification = has_ioSpecification;
    }

    public Set<Thing> getHas_ioSpecification() {
        return has_ioSpecification;
    }

    public void setHas_loopCharacteristics(Set<Thing> has_loopCharacteristics) {
        this.has_loopCharacteristics = has_loopCharacteristics;
    }

    public Set<Thing> getHas_loopCharacteristics() {
        return has_loopCharacteristics;
    }

    public void setTakesPlaceAt(Set<Thing> takesPlaceAt) {
        this.takesPlaceAt = takesPlaceAt;
    }

    public Set<Thing> getTakesPlaceAt() {
        return takesPlaceAt;
    }

}
