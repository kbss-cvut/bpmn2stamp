
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * A Receive Task is a simple Task that is designed to wait for a Message to arrive from an external Participant (relative to the Process). Once the Message has been received, the Task is completed.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ReceiveTask)
public class ReceiveTask
    extends Task
    implements Serializable
{

    /**
     * The Message MUST be supplied (if the isExecutable attribute of the Process is set to true).
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_messageRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Message, max = 1)
    })
    protected Set<Thing> has_messageRef;
    /**
     * This attribute specifies the operation that is invoked
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_operationRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Operation, max = 1)
    })
    protected Set<Thing> has_operationRef;

    public void setHas_messageRef(Set<Thing> has_messageRef) {
        this.has_messageRef = has_messageRef;
    }

    public Set<Thing> getHas_messageRef() {
        return has_messageRef;
    }

    public void setHas_operationRef(Set<Thing> has_operationRef) {
        this.has_operationRef = has_operationRef;
    }

    public Set<Thing> getHas_operationRef() {
        return has_operationRef;
    }

}
