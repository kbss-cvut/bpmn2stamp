
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * The Complex Gateway can be used to model complex synchronization behavior. An Expression activationCondition is used to describe the precise behavior. For example, this Expression could specify that tokens on three out of five incoming Sequence Flows are needed to activate the Gateway. What tokens are produced by the Gateway is determined by conditions on the outgoing Sequence Flows as in the split behavior of the Inclusive Gateway. If tokens arrive later on the two remaining Sequence Flows, those tokens cause a reset of the Gateway and new token can be produced on the outgoing Sequence Flows. To determine whether it needs to wait for additional tokens before it can reset, the Gateway uses the synchronization semantics of the Inclusive Gateway.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ComplexGateway)
public class ComplexGateway
    extends Gateway
    implements Serializable
{

    /**
     * Determines which combination of incoming tokens will be synchronized for
     * activation of the Gateway.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_activationCondition)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Expression, max = 1)
    })
    protected Expression has_activationCondition;
    /**
     * The Sequence Flow that will receive a token when none of the conditionExpressions on other outgoing Sequence Flows evaluate to true. The default Sequence Flow should not have a conditionExpression. Any such Expression SHALL be ignored.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_default)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_SequenceFlow, max = 1)
    })
    protected Set<Thing> has_default;

    public void setHas_activationCondition(Expression has_activationCondition) {
        this.has_activationCondition = has_activationCondition;
    }

    public Expression getHas_activationCondition() {
        return has_activationCondition;
    }

    public void setHas_default(Set<Thing> has_default) {
        this.has_default = has_default;
    }

    public Set<Thing> getHas_default() {
        return has_default;
    }

}
