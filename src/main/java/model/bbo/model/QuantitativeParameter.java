
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * parameters that have only quantitative values
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_QuantitativeParameter)
public class QuantitativeParameter
    extends Parameter
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_has_unitOfMeasure)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_UnitOfMeasure, max = 1)
    })
    protected Set<Thing> has_unitOfMeasure;

    public void setHas_unitOfMeasure(Set<Thing> has_unitOfMeasure) {
        this.has_unitOfMeasure = has_unitOfMeasure;
    }

    public Set<Thing> getHas_unitOfMeasure() {
        return has_unitOfMeasure;
    }

}
