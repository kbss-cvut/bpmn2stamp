package mapper.org2bbo;

import model.bbo.model.Group;
import model.bbo.model.Role;

import java.util.HashMap;
import java.util.Map;

public class Org2BboMappingResult {

    private Map<String, Group> groups;
//    private Map<String, Group> groupsByName;
    private Map<String, Role> roles;
//    private Map<String, Role> rolesByName;

    public Org2BboMappingResult() {
        groups = new HashMap<>();
//        groupsByName = new HashMap<>();
        roles = new HashMap<>();
//        rolesByName = new HashMap<>();
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

//    public Map<String, Group> getGroupsByName() {
//        return groupsByName;
//    }
//
//    public void setGroupsByName(Map<String, Group> groupsByName) {
//        this.groupsByName = groupsByName;
//    }
//
//    public Map<String, Role> getRolesByName() {
//        return rolesByName;
//    }
//
//    public void setRolesByName(Map<String, Role> rolesByName) {
//        this.rolesByName = rolesByName;
//    }
}
