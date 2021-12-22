
package model.stamp.model;

import java.io.Serializable;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.Properties;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import model.stamp.Vocabulary;


/**
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_stamp_object)
public class StampObject
        extends Thing
    implements Serializable
{

    @Override
    public String toString() {
        return ((((("StampObject {"+ name)+"<")+ id)+">")+"}");
    }

}