
package cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model;

import java.io.Serializable;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;


/**
 * Complex: the complexBehaviorDefinitions are consulted to determine if and which Events to throw.
 * Any thrown Events can be caught by boundary Events on the Multi-Instance Activity.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_ComplexMultiInstanceLoopCharacteristics)
public class ComplexMultiInstanceLoopCharacteristics
    extends MultiInstanceLoopCharacteristics
    implements Serializable
{


}