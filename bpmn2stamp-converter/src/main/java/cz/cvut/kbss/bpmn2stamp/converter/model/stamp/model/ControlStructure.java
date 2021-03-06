
package cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model;

import java.io.Serializable;
import java.util.Set;

import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;


/**
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_control_structure)
public class ControlStructure
    extends Control
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
    protected Set<ControlStructureElement> has_control_structure_element_part;

    public void setHas_control_structure_element_part(Set<ControlStructureElement> has_control_structure_element_part) {
        this.has_control_structure_element_part = has_control_structure_element_part;
    }

    public Set<ControlStructureElement> getHas_control_structure_element_part() {
        return has_control_structure_element_part;
    }

}
