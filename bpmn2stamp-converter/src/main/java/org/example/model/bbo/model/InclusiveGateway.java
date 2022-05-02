
package org.example.model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import org.example.model.bbo.Vocabulary;

import java.io.Serializable;


/**
 * A diverging Inclusive Gateway (Inclusive Decision) can be used to create alternative but also parallel paths within a Process flow. Unlike the Exclusive Gateway, all condition Expressions are evaluated. The true evaluation of one condition Expression does not exclude the evaluation of other condition Expressions. All Sequence Flows with a true evaluation will be traversed by a token. Since each path is considered to be independent, all combinations of the paths MAY be taken, from zero to all. However, it should be designed so that at least one path is taken.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_InclusiveGateway)
public class InclusiveGateway
    extends Gateway
    implements Serializable
{

    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other outgoing Sequence Flows evaluate to true. The default Sequence Flow should not have a conditionExpression. Any such Expression SHALL be ignored.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_default)
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