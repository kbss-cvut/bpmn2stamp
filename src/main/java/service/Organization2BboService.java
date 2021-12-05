package service;

import mapper.org2bbo.MapstructOrgMapper;
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

public class Organization2BboService {

    private final MapstructOrgMapper mapper;

    public Organization2BboService() {
        this.mapper = Mappers.getMapper(MapstructOrgMapper.class);
    }

    public Org2BboMappingResult transform(Organization organization, ActorMappings actorMappings) {
        Org2BboMappingResult mappingResult = mapper.organization(organization);
        mapper.getAfterMapping().forEach(Runnable::run);

        if (actorMappings != null) {
            List<Membership> memberships = actorMappings.getActorMapping().stream()
                    .flatMap(e -> e.getMemberships().getMembership().stream())
                    .collect(Collectors.toList());
            assignRolesToGroups(mappingResult.getGroups(), mappingResult.getRoles(), memberships);
        }

        return mappingResult;
    }

    public Org2BboMappingResult transform(Organization organization) {
        return transform(organization, null);
    }

    private void assignRolesToGroups(Map<String, Group> groupsByName, Map<String, Role> rolesByName, List<Membership> memberships) {
        for (Membership membership : memberships) {
            String[] groupsInPath = membership.getGroup().split("/");
            Group group = groupsByName.get(groupsInPath[groupsInPath.length-1]);
            Role role = rolesByName.get(membership.getRole());
            role.setBelongs_to(group);
        }
    }

}
