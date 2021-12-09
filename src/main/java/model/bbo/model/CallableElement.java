
package model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.*;
import model.bbo.Vocabulary;

import java.io.Serializable;


/**
 * CallableElement is the abstract super class of all Activities that have been defined outside of a Process or Choreography but which can be called (or reused), by a Call Activity, from within a Process or Choreography. It MAY reference Interfaces that define the service operations that it provides. The BPMN elements that can be called by Call Activities (i.e., are CallableElements) are: Process and GlobalTask (see Figure 10.43).
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_CallableElement)
public class CallableElement
    extends RootElement
    implements Serializable
{

    /**
     * The InputOutputSpecification defines the inputs and outputs and the InputSets and OutputSets for the Activity. See page 211 for more information on the InputOutputSpecification.
     * 
     * 
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_ioSpecification)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_InputOutputSpecification, max = 1)
    })
    protected InputOutputSpecification has_ioSpecification;
    /**
     * The descriptive name of the element.
     * 
     */
    @OWLDataProperty(iri = Vocabulary.s_p_name)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = "http://www.w3.org/2001/XMLSchema#string", max = 1)
    })
    protected String name;

    public void setHas_ioSpecification(InputOutputSpecification has_ioSpecification) {
        this.has_ioSpecification = has_ioSpecification;
    }

    public InputOutputSpecification getHas_ioSpecification() {
        return has_ioSpecification;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
