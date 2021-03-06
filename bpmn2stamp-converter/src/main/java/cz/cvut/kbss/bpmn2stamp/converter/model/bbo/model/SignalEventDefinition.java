
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;


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
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_signalRef)
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
