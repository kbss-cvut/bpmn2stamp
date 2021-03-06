
package cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model;

import java.io.Serializable;
import java.util.Set;

import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;


/**
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_process_connection)
public class ProcessConnection
    extends StructureConnection
    implements Serializable
{

    @OWLObjectProperty(iri = Vocabulary.s_p_process_connection_from)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_process, min = 1, max = 1)
    })
    protected Set<Thing> process_connection_from;
    @OWLObjectProperty(iri = Vocabulary.s_p_process_connection_to)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_process, min = 1, max = 1)
    })
    protected Process process_connection_to;

    public void setProcess_connection_from(Set<Thing> process_connection_from) {
        this.process_connection_from = process_connection_from;
    }

    public Set<Thing> getProcess_connection_from() {
        return process_connection_from;
    }

    public void setProcess_connection_to(Process process_connection_to) {
        this.process_connection_to = process_connection_to;
    }

    public Process getProcess_connection_to() {
        return process_connection_to;
    }

}
