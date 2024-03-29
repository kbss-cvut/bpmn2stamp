
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.util.Set;


/**
 * Event Definitions refers to the triggers of Catch Events (Start and receive Intermediate Events) and the Results of Throw Events (End Events and send Intermediate Events). The types of Event Definitions are: CancelEventDefinition, CompensationEventDefinition, ConditionalEventDefinition ErrorEventDefinition, EscalationEventDefinition, MessageEventDefinitio, LinkEventDefinition, SignalEventDefinition, TerminateEventDefinition, an TimerEventDefinition (see Table 10.93). A None Event is determined by an Event that does not specify an
 * Event Definition. A Multiple Event is determined by an Event that specifies more than one Event Definition. The different types of Events (Start, End, and Intermediate) utilize a subset of the available types of Event Definitions.
 * <p>
 * This class was generated by OWL2Java 0.16.4
 */
@OWLClass(iri = Vocabulary.s_c_EventDefinition)
public class EventDefinition
        extends RootElement
        implements Serializable {

    @OWLObjectProperty(cascade = {CascadeType.ALL}, iri = Vocabulary.s_p_has_eventDefinitionEvent)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Event, max = 1)
    })
    protected Set<Thing> has_eventDefinitionEvent;

    public void setHas_eventDefinitionEvent(Set<Thing> has_eventDefinitionEvent) {
        this.has_eventDefinitionEvent = has_eventDefinitionEvent;
    }

    public Set<Thing> getHas_eventDefinitionEvent() {
        return has_eventDefinitionEvent;
    }

    //TODO [review] manually added
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_expression)
    protected Set<Thing> has_hasExpression;

    public Set<Thing> getHas_hasExpression() {
        return has_hasExpression;
    }

    public void setHas_hasExpression(Set<Thing> has_hasExpression) {
        this.has_hasExpression = has_hasExpression;
    }
}
