
package model.bbo.model;

import java.io.Serializable;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * A diverging Exclusive Gateway (Decision) is used to create alternative paths within a Process flow. This is basically the “diversion point in the road” for a Process. For a given instance of the Process, only one of the paths can be taken.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ExclusiveGateway)
public class ExclusiveGateway
    extends Gateway
    implements Serializable
{

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other outgoing Sequence Flows evaluate to true. The default Sequence Flow should not have a conditionExpression. Any such Expression SHALL be ignored.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_default)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_SequenceFlow, max = 1)
    })
    protected SequenceFlow has_default;

    public void setHas_default(SequenceFlow has_default) {
        this.has_default = has_default;
    }

    public SequenceFlow getHas_default() {
        return has_default;
    }

}
