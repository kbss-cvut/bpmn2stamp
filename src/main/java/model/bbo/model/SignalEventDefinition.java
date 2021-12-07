
package model.bbo.model;

import java.io.Serializable;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * /
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_SignalEventDefinition)
public class SignalEventDefinition
    extends EventDefinition
    implements Serializable
{

    /**
     * [IRIT] specifies the Signal of the SignalEventDefinition
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_signalRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Signal, min = 1, max = 1)
    })
    protected Signal has_signalRef;

    public void setHas_signalRef(Signal has_signalRef) {
        this.has_signalRef = has_signalRef;
    }

    public Signal getHas_signalRef() {
        return has_signalRef;
    }

}
