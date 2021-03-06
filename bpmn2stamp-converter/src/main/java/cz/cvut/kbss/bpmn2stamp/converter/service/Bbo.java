package cz.cvut.kbss.bpmn2stamp.converter.service;

import com.google.common.collect.Iterables;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;

public class Bbo {

    private final Iterable<Thing> allObjects;

    public Bbo(BpmnAsBbo bpmnAsBbo, OrganizationAsBbo organizationAsBbo) {
        this.allObjects = Iterables.concat(organizationAsBbo.getAllObjects().values(), bpmnAsBbo.getAllObjects().values());
    }

    public Bbo(Iterable<Thing> allObjects) {
        this.allObjects = allObjects;
    }

    public Iterable<Thing> getAllObjects() {
        return allObjects;
    }
}
