
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * An interface defines a set of operations that are implemented by Services.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Interface)
public class Interface
    extends RootElement
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_has_interfaceOperation)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Operation, min = 1, max = -1)
    })
    protected Set<Operation> has_interfaceOperation;
    /**
     * The descriptive name of the element.
     * 
     */
    @OWLDataProperty(iri = Vocabulary.s_p_name)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = "http://www.w3.org/2001/XMLSchema#string", max = 1)
    })
    protected String name;

    public void setHas_interfaceOperation(Set<Operation> has_interfaceOperation) {
        this.has_interfaceOperation = has_interfaceOperation;
    }

    public Set<Operation> getHas_interfaceOperation() {
        return has_interfaceOperation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
