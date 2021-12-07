
package model.bbo.model;

import java.io.Serializable;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_BoundaryEvent)
public class BoundaryEvent
    extends CatchEvent
    implements Serializable
{

    /**
     * Denotes the Activity that boundary Event is attached to.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_is_attachedToRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Activity, min = 1, max = 1)
    })
    protected Activity is_attachedToRef;

    public void setIs_attachedToRef(Activity is_attachedToRef) {
        this.is_attachedToRef = is_attachedToRef;
    }

    public Activity getIs_attachedToRef() {
        return is_attachedToRef;
    }

}
