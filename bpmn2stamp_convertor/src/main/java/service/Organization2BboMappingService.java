package service;

import mapper.org2bbo.MapstructOrg2BboMapper;
import mapper.org2bbo.Org2BboMappingResult;
import model.actor.ActorMappings;
import model.actor.element.Membership;
import model.bbo.Vocabulary;
import model.bbo.model.Group;
import model.bbo.model.Role;
import model.organization.Organization;
import org.mapstruct.factory.Mappers;
import utils.MappingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Organization2BboMappingService {

    private final MapstructOrg2BboMapper mapper;

    public Organization2BboMappingService() {
        this.mapper = Mappers.getMapper(MapstructOrg2BboMapper.class);
    }

    public Org2BboMappingResult transform(Organization organization, String ontologyIri) {
        this.mapper.getConfiguration().setBaseIri(ontologyIri);
        Org2BboMappingResult result = mapper.process(organization);
        return result;
    }

    public OrganizationBbo extendOrganizationHierarchy(OrganizationBbo organizationBbo, ActorMappings actorMappings) {
        if (organizationBbo == null || actorMappings == null)
            return organizationBbo;

        List<Membership> memberships = actorMappings.getActorMapping().stream()
                .flatMap(e -> e.getMemberships().getMembership().stream())
                .collect(Collectors.toList());

        HashMap<String, String> groupsIdMapping = new HashMap<>();
        organizationBbo.getGroups().forEach((id, group) -> groupsIdMapping.put(group.getName(), id));

        HashMap<String, String> rolesIdMapping = new HashMap<>();
        organizationBbo.getRoles().forEach((id, role) -> rolesIdMapping.put(role.getName(), id));

        assignRolesToGroups(
                organizationBbo.getGroups(),
                groupsIdMapping,
                organizationBbo.getRoles(),
                rolesIdMapping,
                memberships);

        return organizationBbo;
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
            Group groupInOrganization = groups.get(lastGroupInPathId);

            String roleId = rolesIdMapping.get(membership.getRole());
            Role roleInOrganization = roles.get(roleId);

            Set<String> isRoleInGroups = MappingUtils.ensurePropertyValue(Vocabulary.s_p_is_role_in, roleInOrganization::getProperties, roleInOrganization::setProperties);
            isRoleInGroups.add(groupInOrganization.getId());
        }
    }

}
