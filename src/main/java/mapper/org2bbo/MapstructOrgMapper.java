package mapper.org2bbo;

import mapper.OntologyMapstructMapper;
import model.bbo.model.Thing;
import model.organization.Group;
import model.organization.Organization;
import model.organization.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utils.MappingUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mapper
public abstract class MapstructOrgMapper extends OntologyMapstructMapper<Thing> {

    public Org2BboMappingResult organization(Organization organization) {

        Org2BboMappingResult result = new Org2BboMappingResult();

        for (Group group : organization.getGroups().getGroup()) {
            model.bbo.model.Group g = groupToGroup(group);
            result.getGroups().put(g.getName(), g);
        }
        for (Role role : organization.getRoles().getRole()) {
            model.bbo.model.Role r = roleToRole(role);
            result.getRoles().put(r.getName(), r);
        }
        return result;
    }

    @Mapping(target = "id", source = "name")
    public abstract model.bbo.model.Group groupToGroup(Group group);
    @AfterMapping
    public void groupToGroupProperties(Group group, @MappingTarget model.bbo.model.Group groupResult) {
        getAfterMapping().add(() -> {
            if (group.getParentPath() == null)
                return;
            String[] split = group.getParentPath().split("/");
            String closestParent = split[split.length-1];
            Set<model.bbo.model.Group> parents = Set.of((model.bbo.model.Group) getMappedObjects().get(closestParent));
            groupResult.setIs_partOf(parents);
        });
    }

    @Mapping(target = "id", source = "name")
    public abstract model.bbo.model.Role roleToRole(Role role);

    @Override
    protected String getId(Thing obj) {
        return obj.getId();
    }
}
