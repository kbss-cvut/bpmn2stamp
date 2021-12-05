
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
 * Activities MAY be repeated sequentially, essentially behaving like a loop. The presence of LoopCharacteristics signifies that the Activity has looping behavior. LoopCharacteristics is an abstract class. Concrete subclasses define specific kinds of looping behavior.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_LoopCharacteristics)
public class LoopCharacteristics
        extends Thing
    implements Serializable
{
    @OWLObjectProperty(iri = Vocabulary.s_p_has_loopActivity)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Activity, max = 1)
    })
    protected Activity has_loopActivity;

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
        return ((((("LoopCharacteristics {"+ name)+"<")+ id)+">")+"}");
    }

    public void setHas_loopActivity(Activity has_loopActivity) {
        this.has_loopActivity = has_loopActivity;
    }

    public Activity getHas_loopActivity() {
        return has_loopActivity;
    }

}
