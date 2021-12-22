
package model.bbo.model;

import java.io.Serializable;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraint;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import model.bbo.Vocabulary;


/**
 * The FormalExpression class is used to specify an executable Expression using a specified Expression language. A natural-language description of the Expression can also be specified, in addition to the formal specification.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_FormalExpression)
public class FormalExpression
    extends Expression
    implements Serializable
{

    /**
     * Overrides the Expression language specified in the Definitions. The language MUST be specified in a URI format.
     * 
     */
    @OWLDataProperty(iri = Vocabulary.s_p_language)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = "http://www.w3.org/2001/XMLSchema#string", max = 1)
    })
    protected String language;

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

}