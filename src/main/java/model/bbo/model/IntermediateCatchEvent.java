
package model.bbo.model;

import java.io.Serializable;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import model.bbo.Vocabulary;


/**
 * A Message Intermediate Event can be used to either send a Message or receive a Message. When used to “catch” the Message,
 * then the Event marker MUST be unfilled (see the lower figure on the right).
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_IntermediateCatchEvent)
public class IntermediateCatchEvent
    extends CatchEvent
    implements Serializable
{


}
