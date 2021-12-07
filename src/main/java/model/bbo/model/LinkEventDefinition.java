
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
 * A Link Event is a mechanism for connecting two sections of a Process. Link Events can be used to create looping situations or to avoid long Sequence Flow lines. The use of Link Events is limited to a single Process level (i.e., they cannot link a parent Process with a Sub-Process).
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_LinkEventDefinition)
public class LinkEventDefinition
    extends EventDefinition
    implements Serializable
{

    /**
     * Used to reference the corresponding 'catch' or 'target' LinkEventDefinition, when
     * this LinkEventDefinition represents a 'throw' or 'source' LinkEventDefinition.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_source)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_LinkEventDefinition, min = 1, max = -1)
    })
    protected Set<LinkEventDefinition> has_source;
    /**
     * Used to reference the corresponding 'throw' or 'source' LinkEventDefinition,
     * when this LinkEventDefinition represents a 'catch' or 'target' LinkEventDefinition.
     * 
     */
    @OWLObjectProperty(iri = Vocabulary.s_p_has_target)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_LinkEventDefinition, min = 1, max = 1)
    })
    protected Set<Thing> has_target;
    /**
     * The descriptive name of the element.
     * 
     */
    @OWLDataProperty(iri = Vocabulary.s_p_name)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = "http://www.w3.org/2001/XMLSchema#string", max = 1)
    })
    protected String name;

    public void setHas_source(Set<LinkEventDefinition> has_source) {
        this.has_source = has_source;
    }

    public Set<LinkEventDefinition> getHas_source() {
        return has_source;
    }

    public void setHas_target(Set<Thing> has_target) {
        this.has_target = has_target;
    }

    public Set<Thing> getHas_target() {
        return has_target;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
