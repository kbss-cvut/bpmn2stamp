
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;


/**
 * The Expression class is used to specify an Expression using natural-language text. These Expressions are not executable. The natural language text is captured using the documentation attribute, inherited from BaseElement.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_Expression)
public class Expression
        extends Thing
    implements Serializable
{
    /**
     * [IRIT] references the ComplexGateway which is concerned with the Expression
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_complexGateway)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_ComplexGateway, max = 1)
    })
    protected ComplexGateway has_complexGateway;

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
        return ((((("Expression {"+ name)+"<")+ id)+">")+"}");
    }

    public void setHas_complexGateway(ComplexGateway has_complexGateway) {
        this.has_complexGateway = has_complexGateway;
    }

    public ComplexGateway getHas_complexGateway() {
        return has_complexGateway;
    }

}
