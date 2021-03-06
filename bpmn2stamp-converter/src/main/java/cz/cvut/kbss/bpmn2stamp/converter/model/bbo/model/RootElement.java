
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;


/**
 * RootElement is the abstract super class for all BPMN elements that are contained within Definitions. When contained within Definitions, these elements have their own defined life-cycle and are not deleted with the deletion of other elements. Examples of concrete RootElements include Collaboration, Process, and Choreography. Depending on their use, RootElements can be referenced by multiple other elements (i.e., they can be reused). Some RootElements MAY be contained within other elements instead of Definitions. This is done to avoid the maintenance overhead of an independent life-cycle. For example, an EventDefinition would be contained in a Process since it is used only there. In this case the EventDefinition would be dependent on the tool life-cycle of
 * the Process.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_RootElement)
public class RootElement
        extends Thing
    implements Serializable
{

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
        return ((((("RootElement {"+ name)+"<")+ id)+">")+"}");
    }

}
