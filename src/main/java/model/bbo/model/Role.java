
package model.bbo.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.jopa.model.annotations.Properties;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import model.bbo.Vocabulary;


/**
 * This class was generated by OWL2Java 0.16.4
 *
 */
@OWLClass(iri = Vocabulary.s_c_Role)
public class Role
        extends Thing
    implements Serializable
{

    //TODO [review] manually added
    @OWLObjectProperty(iri = Vocabulary.s_p_belongs)
    protected Group belongs_to;

    @OWLObjectProperty(iri = Vocabulary.s_p_is_responsibleFor)
    @ParticipationConstraints({
//            @ParticipationConstraint(owlObjectIRI = Vocabulary.s_p_is_responsibleFor, min = 1, max = 1)
    })
    protected Set<Thing> is_responsibleFor;

    //TODO [review] manually added
    @OWLObjectProperty(iri = Vocabulary.s_p_has_role_part)
    @ParticipationConstraints({
//            @ParticipationConstraint(owlObjectIRI = Vocabulary.s_p_has_role_part)
    })
    protected Set<Role> has_role_part;

    public Set<Role> getHas_role_part() {
        return has_role_part;
    }

    public void setHas_role_part(Set<Role> has_role_part) {
        this.has_role_part = has_role_part;
    }

    public Group getBelongs_to() {
        return belongs_to;
    }

    public void setBelongs_to(Group belongs_to) {
        this.belongs_to = belongs_to;
    }

    public Set<Thing> getIs_responsibleFor() {
        return is_responsibleFor;
    }

    public void setIs_responsibleFor(Set<Thing> is_responsibleFor) {
        this.is_responsibleFor = is_responsibleFor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setProperties(Map<String, Set<String>> properties) {
        this.properties = properties;
    }

    public Map<String, Set<String>> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return ((((("Role {"+ name)+"<")+ id)+">")+"}");
    }

}
