
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;

import java.io.Serializable;


/**
 * A Call Activity identifies a point in the Process where a global Process or a Global Task is used. The Call Activity acts as a ‘wrapper’ for the invocation of a global Process or Global Task within the execution. The activation of a call Activity results in the transfer of control to the called global Process or Global Task.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_CallActivity)
public class CallActivity
    extends Activity
    implements Serializable
{

    /**
     * The element to be called, which will be either a Process or a
     * GlobalTask.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_calledElement)
    @ParticipationConstraints({
        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_CallableElement, max = 1)
    })
    // should be a Process or a GlobalTask, therefore changed to Thing
    protected Thing has_calledElement;

    public void setHas_calledElement(Thing has_calledElement) {
        this.has_calledElement = has_calledElement;
    }

    public Thing getHas_calledElement() {
        return has_calledElement;
    }

}
