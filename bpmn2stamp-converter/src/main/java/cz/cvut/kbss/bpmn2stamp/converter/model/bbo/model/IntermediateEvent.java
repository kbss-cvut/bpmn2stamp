
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.util.Set;


/**
 * The Intermediate Event indicates where something happens (an Event) somewhere between the start and end of a Process. It will affect the flow of the Process, but will not start or (directly) terminate the process.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_IntermediateEvent)
public class IntermediateEvent
    extends Event
    implements Serializable
{

    /**
     * identifies the outgoing Sequence Flow of the FlowNode.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_outgoing)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_SequenceFlow, min = 1, max = -1)
    })
    protected Set<SequenceFlow> has_outgoing;

    public void setHas_outgoing(Set<SequenceFlow> has_outgoing) {
        this.has_outgoing = has_outgoing;
    }

    public Set<SequenceFlow> getHas_outgoing() {
        return has_outgoing;
    }

}
