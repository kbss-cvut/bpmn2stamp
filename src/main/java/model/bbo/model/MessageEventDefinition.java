
package model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;

import java.io.Serializable;
import java.util.Set;


/**
 * /
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_MessageEventDefinition)
public class MessageEventDefinition
    extends EventDefinition
    implements Serializable
{

    /**
     * The Message MUST be supplied (if the isExecutable attribute of the Process is set to true).
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_messageRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Message, max = 1)
    })
    protected Message has_messageRef;
    /**
     * This attribute specifies the operation that is invoked
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_operationRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Operation, max = 1)
    })
    protected Set<Thing> has_operationRef;

    public void setHas_messageRef(Message has_messageRef) {
        this.has_messageRef = has_messageRef;
    }

    public Message getHas_messageRef() {
        return has_messageRef;
    }

    public void setHas_operationRef(Set<Thing> has_operationRef) {
        this.has_operationRef = has_operationRef;
    }

    public Set<Thing> getHas_operationRef() {
        return has_operationRef;
    }

}
