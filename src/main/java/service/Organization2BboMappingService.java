package service;

import mapper.org2bbo.MapstructOrg2BboMapper;
import mapper.org2bbo.Org2BboMappingResult;
import model.actor.ActorMappings;
import model.actor.element.Membership;
import model.bbo.model.Group;
import model.bbo.model.Role;
import model.organization.Organization;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Organization2BboMappingService {

    private final MapstructOrg2BboMapper mapper;

    public Organization2BboMappingService(String ontologyIri) {
        this.mapper = Mappers.getMapper(MapstructOrg2BboMapper.class);
        mapper.setTargetIdBase(ontologyIri);
    }

    public Org2BboMappingResult transform(Organization organization, ActorMappings actorMappings) {
        Org2BboMappingResult mappingResult = mapper.organization(organization);
        mapper.getAfterMapping().forEach(Runnable::run);

        if (actorMappings != null) {
            List<Membership> memberships = actorMappings.getActorMapping().stream()
                    .flatMap(e -> e.getMemberships().getMembership().stream())
                    .collect(Collectors.toList());
            assignRolesToGroups(
                    mappingResult.getGroups(),
                    mappingResult.getGroupsIdMapping(),
                    mappingResult.getRoles(),
                    mappingResult.getRolesIdMapping(),
                    memberships);
        }

        return mappingResult;
    }

    public Org2BboMappingResult transform(Organization organization) {
        return transform(organization, null);
    }

    private void assignRolesToGroups(Map<String, Group> groups,
                                     Map<String, String> groupsIdMapping,
                                     Map<String, Role> roles,
                                     Map<String, String> rolesIdMapping,
                                     List<Membership> memberships) {
        for (Membership membership : memberships) {
            String[] groupsInPath = membership.getGroup().split("/");
            String s = groupsInPath[groupsInPath.length - 1];
            String lastGroupInPathId = groupsIdMapping.get(s);
            Group group = groups.get(lastGroupInPathId);
            String roleId = rolesIdMapping.get(membership.getRole());
            Role role = roles.get(roleId);
            role.setBelongs_to(group);
        }
    }

}
