
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * A software used by an agent to perform a process, or a software agent that executes specific tasks.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_SoftwareResource)
public class SoftwareResource
    extends Resource
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_is_installedOn)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Computer, min = 1, max = -1)
    })
    protected Set<Computer> is_installedOn;

    public void setIs_installedOn(Set<Computer> is_installedOn) {
        this.is_installedOn = is_installedOn;
    }

    public Set<Computer> getIs_installedOn() {
        return is_installedOn;
    }

}
