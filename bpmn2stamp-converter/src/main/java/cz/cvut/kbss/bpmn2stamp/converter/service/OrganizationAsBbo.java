package cz.cvut.kbss.bpmn2stamp.converter.service;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role;

import java.util.HashMap;
import java.util.Map;

public class OrganizationAsBbo {

    private Map<String, Thing> allObjects;
    private Map<String, Group> groups;
    private Map<String, Role> roles;

    public OrganizationAsBbo() {
        allObjects = new HashMap<>();
        groups = new HashMap<>();
        roles = new HashMap<>();
    }

    public Map<String, Thing> getAllObjects() {
        return allObjects;
    }

    public void setAllObjects(Map<String, Thing> allObjects) {
        this.allObjects = allObjects;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }

    public Map<String, Role> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Role> roles) {
        this.roles = roles;
    }
}
