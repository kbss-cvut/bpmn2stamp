
package org.example.model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import org.example.model.bbo.Vocabulary;

import java.io.Serializable;


/**
 * A sequence or flow of Activities in an organization with the objective of carrying out work. In BPMN, a Process is depicted as a graph of Flow Elements, which are a set of Activities, Events, Gateways, and Sequence Flow that adhere to a finite execution semantics.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Process)
public class Process
    extends FlowElementsContainer
    implements Serializable
{

    /**
     * Allows to specifiy where the activity or the process will take place
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_takesPlaceAt)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_ManufacturingFacility, max = 1)
    })
    protected ManufacturingFacility takesPlaceAt;

    public void setTakesPlaceAt(ManufacturingFacility takesPlaceAt) {
        this.takesPlaceAt = takesPlaceAt;
    }

    public ManufacturingFacility getTakesPlaceAt() {
        return takesPlaceAt;
    }

}