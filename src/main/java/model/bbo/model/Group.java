
package model.bbo.model;

import java.io.Serializable;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * The Group class represents a collection of individual agents (and may itself play the role of a Agent, ie. something that can perform actions). [Definition extracted from FOAF Vocabulary Specification 0.99]
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Group)
public class Group
    extends HumanResource
    implements Serializable
{

    //TODO [review] added manually, used to define group hierarchy
    @OWLObjectProperty(iri = Vocabulary.s_p_is_partOf)
    @ParticipationConstraints({
            @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Group, max = 1)
    })
    protected Set<Group> is_partOf;

    @OWLObjectProperty(iri = Vocabulary.s_p_groups)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Person, min = 2, max = -1)
    })
    protected Set<Thing> groups;
    @OWLObjectProperty(iri = Vocabulary.s_p_has_leader)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Person, max = 1)
    })
    protected Set<Thing> has_leader;

    public Set<Group> getIs_partOf() {
        return is_partOf;
    }

    public void setIs_partOf(Set<Group> is_partOf) {
        this.is_partOf = is_partOf;
    }

    public void setGroups(Set<Thing> groups) {
        this.groups = groups;
    }

    public Set<Thing> getGroups() {
        return groups;
    }

    public void setHas_leader(Set<Thing> has_leader) {
        this.has_leader = has_leader;
    }

    public Set<Thing> getHas_leader() {
        return has_leader;
    }

}
