package org2bbo;

import common.OntologyMapstructMapper;
import model.bbo.model.Thing;
import model.org.Group;
import model.org.Groups;
import model.org.Memberships;
import model.org.Organization;
import model.org.Role;
import org.mapstruct.Mapper;

@Mapper
public abstract class MapstructOrgMapper extends OntologyMapstructMapper<Thing> {

    public Org2BboMappingResult definitions(Organization organization) {

        Org2BboMappingResult result = new Org2BboMappingResult();

        for (Group group : organization.getGroups().getGroup()) {
            model.bbo.model.Group g = groupToGroup(group);
            result.getGroups().put(g.getId(), g);
        }
        for (Role role : organization.getRoles().getRole()) {
            model.bbo.model.Role r = roleToRole(role);
            result.getRoles().put(r.getId(), r);
        }
        return result;
    };

    public abstract model.bbo.model.Group groupToGroup(Group group);

    public abstract model.bbo.model.Role roleToRole(Role role);

    public abstract Groups userTaskToUserTask(Groups groups);

    public abstract Memberships userTaskToUserTask(Memberships memberships);

    @Override
    protected String getId(Thing obj) {
        return obj.getId();
    }
}
