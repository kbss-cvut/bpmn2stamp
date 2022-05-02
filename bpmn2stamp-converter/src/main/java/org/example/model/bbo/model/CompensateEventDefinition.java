
package org.example.model.bbo.model;

import cz.cvut.kbss.jopa.model.annotations.CascadeType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.jopa.model.annotations.ParticipationConstraints;
import org.example.model.bbo.Vocabulary;

import java.io.Serializable;
import java.util.Set;


/**
 * Compensation Events are used in the context of triggering or handling compensation (see page 302 for more details on compensation). There are four variations: a Start Event, both a catch and throw Intermediate Event, and an End Event.
 * 
 * This class was generated by OWL2Java 0.16.4
 * 
 */
@OWLClass(iri = Vocabulary.s_c_CompensateEventDefinition)
public class CompensateEventDefinition
    extends EventDefinition
    implements Serializable
{

    /**
     * For a Start Event:
     * This Event “catches” the compensation for an Event Sub-Process. No further
     * information is REQUIRED. The Event Sub-Process will provide the Id necessary
     * to match the Compensation Event with the Event that threw the compensation,
     * or the compensation will have been a broadcast.
     * For an End Event:
     * The Activity to be compensated MAY be supplied. If an Activity is not supplied,
     * then the compensation is broadcast to all completed Activities in the current Sub-
     * Process (if present), or the entire Process instance (if at the global level).
     * For an Intermediate Event within normal flow:
     * The Activity to be compensated MAY be supplied. If an Activity is not supplied,
     * then the compensation is broadcast to all completed Activities in the current Sub-
     * Process (if present), or the entire Process instance (if at the global level). This
     * “throws” the compensation.
     * For an Intermediate Event attached to the boundary of an Activity:
     * This Event “catches” the compensation. No further information is REQUIRED. The
     * Activity the Event is attached to will provide the Id necessary to match the
     * Compensation Event with the Event that threw the compensation, or the
     * compensation will have been a broadcast.
     * 
     */
    @OWLObjectProperty(cascade = CascadeType.PERSIST, iri = Vocabulary.s_p_has_activityRef)
    @ParticipationConstraints({
//        @ParticipationConstraint(owlObjectIRI = Vocabulary.s_c_Activity, max = 1)
    })
    protected Set<Thing> has_activityRef;

    public void setHas_activityRef(Set<Thing> has_activityRef) {
        this.has_activityRef = has_activityRef;
    }

    public Set<Thing> getHas_activityRef() {
        return has_activityRef;
    }

}