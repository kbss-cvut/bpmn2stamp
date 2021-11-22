
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * Events that throw a Result. All End Events and some Intermediate Events are throwing Events that MAY eventually be caught by another Event. Typically the trigger carries information out of the scope where the throw Event occurred into the scope of the catching Events. The throwing of a trigger MAY be either implicit as defined by this standard or an extension to it or explicit by a throw Event
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ThrowEvent)
public class ThrowEvent
    extends Event
    implements Serializable
{

    /**
     * A reference to the InputSets defined by the InputOutputSpecification. Every InputOutputSpecification MUST define at least one InputSet.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_inputSet)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_InputSet, min = 1, max = -1)
    })
    protected Set<Thing> has_inputSet;

    public void setHas_inputSet(Set<Thing> has_inputSet) {
        this.has_inputSet = has_inputSet;
    }

    public Set<Thing> getHas_inputSet() {
        return has_inputSet;
    }

}
