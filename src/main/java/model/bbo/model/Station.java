
package model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;

import java.io.Serializable;
import java.util.Set;


/**
 * /
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Station)
public class Station
    extends ManufacturingFacility
    implements Serializable
{

    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_is_partOf)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Cell, max = 1)
    })
    protected Set<Thing> is_partOf;

    public void setIs_partOf(Set<Thing> is_partOf) {
        this.is_partOf = is_partOf;
    }

    public Set<Thing> getIs_partOf() {
        return is_partOf;
    }

}
