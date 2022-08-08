
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;


/**
 * A parameter related to a specific resource, only one resource
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ResourceParameter)
public class ResourceParameter
    extends Parameter
    implements Serializable
{

    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_resource)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Resource, min = 1, max = 1)
    })
    protected Resource has_resource;
    /**
     * The descriptive name of the element.
     * 
     */
    @OWLDataProperty(iri = Vocabulary.s_p_name)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = "http://www.w3.org/2001/XMLSchema#string", max = 1)
    })
    protected String name;

    public void setHas_resource(Resource has_resource) {
        this.has_resource = has_resource;
    }

    public Resource getHas_resource() {
        return has_resource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
