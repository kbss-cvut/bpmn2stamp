package org2bbo;

import model.bbo.model.Group;
import model.bbo.model.Role;

import java.util.HashMap;
import java.util.Map;

public class Org2BboMappingResult {

    private Map<String, Group> groups;
    private Map<String, Role> Roles;

    public Org2BboMappingResult() {
        groups = new HashMap<>();
        Roles = new HashMap<>();
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Group> groups) {
        this.groups = groups;
    }

    public Map<String, Role> getRoles() {
        return Roles;
    }

    public void setRoles(Map<String, Role> roles) {
        Roles = roles;
    }
}
