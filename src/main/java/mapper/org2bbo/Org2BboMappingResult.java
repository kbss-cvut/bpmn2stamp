package mapper.org2bbo;

import model.bbo.model.Group;
import model.bbo.model.Role;

import java.util.HashMap;
import java.util.Map;

public class Org2BboMappingResult {

    private Map<String, Group> groups;
    private Map<String, String> groupsIdMapping;
    private Map<String, Role> roles;
    private Map<String, String> rolesIdMapping;

    public Org2BboMappingResult() {
        groups = new HashMap<>();
        groupsIdMapping = new HashMap<>();
        roles = new HashMap<>();
        rolesIdMapping = new HashMap<>();
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

    public Map<String, String> getGroupsIdMapping() {
        return groupsIdMapping;
    }

    public void setGroupsIdMapping(Map<String, String> groupsIdMapping) {
        this.groupsIdMapping = groupsIdMapping;
    }

    public Map<String, String> getRolesIdMapping() {
        return rolesIdMapping;
    }

    public void setRolesIdMapping(Map<String, String> rolesIdMapping) {
        this.rolesIdMapping = rolesIdMapping;
    }
}
