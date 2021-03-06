
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.util.Set;


/**
 * /
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ErrorEventDefinition)
public class ErrorEventDefinition
    extends EventDefinition
    implements Serializable
{

    /**
     * for an Operation instance: specifies errors that the Operation may return. An Operation MAY refer to zero or more Error elements.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_errorRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Error, max = 1)
    })
    protected Set<Thing> has_errorRef;

    public void setHas_errorRef(Set<Thing> has_errorRef) {
        this.has_errorRef = has_errorRef;
    }

    public Set<Thing> getHas_errorRef() {
        return has_errorRef;
    }

}
