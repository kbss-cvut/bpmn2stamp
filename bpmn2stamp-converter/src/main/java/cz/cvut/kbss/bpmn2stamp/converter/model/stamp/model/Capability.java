
package cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model;

import java.io.Serializable;
import java.util.Set;

import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;


/**
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_capability)
public class Capability
    extends StampObject
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_is_manifested_by)
//    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_stamp_event, min = 1, max = -1)
//    })
    protected Set<Thing> is_manifested_by;

    public void setIs_manifested_by(Set<Thing> is_manifested_by) {
        this.is_manifested_by = is_manifested_by;
    }

    public Set<Thing> getIs_manifested_by() {
        return is_manifested_by;
    }

}
