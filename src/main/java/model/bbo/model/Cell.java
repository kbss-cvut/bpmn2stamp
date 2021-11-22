
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
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
@OWLClass(iri = Vocabulary.s_c_Cell)
public class Cell
    extends ManufacturingFacility
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_is_partOf)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Shop, max = 1)
    })
    protected Set<Thing> is_partOf;

    public void setIs_partOf(Set<Thing> is_partOf) {
        this.is_partOf = is_partOf;
    }

    public Set<Thing> getIs_partOf() {
        return is_partOf;
    }

}
