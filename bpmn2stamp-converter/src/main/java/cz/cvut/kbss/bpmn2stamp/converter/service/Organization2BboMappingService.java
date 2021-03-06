package cz.cvut.kbss.bpmn2stamp.converter.service;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.otherMappers.MapstructConfigToActorMapping;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.element.Membership;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.MapstructOrg2BboMapper;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.Org2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.model.actorConfig.Configuration;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Organization;
import org.mapstruct.factory.Mappers;
import cz.cvut.kbss.bpmn2stamp.converter.utils.ConverterMappingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Organization2BboMappingService {

    private final MapstructOrg2BboMapper mapper;
    private final MapstructConfigToActorMapping actorConfigMapper;

    public Organization2BboMappingService() {
        this.mapper = Mappers.getMapper(MapstructOrg2BboMapper.class);
        this.actorConfigMapper = Mappers.getMapper(MapstructConfigToActorMapping.class);
    }

    public Org2BboMappingResult transform(Organization organization, String ontologyIri) {
        this.mapper.getConfiguration().setBaseIri(ontologyIri);
        Org2BboMappingResult result = mapper.process(organization);
        return result;
    }

    public OrganizationAsBbo extendOrganizationHierarchy(OrganizationAsBbo organizationAsBbo, ActorMappings actorMappings) {
        if (organizationAsBbo == null || actorMappings == null)
            return organizationAsBbo;

        List<Membership> memberships = actorMappings.getActorMapping().stream()
                .flatMap(e -> e.getMemberships().getMembership().stream())
                .collect(Collectors.toList());

        HashMap<String, String> groupsIdMapping = new HashMap<>();
        organizationAsBbo.getGroups().forEach((id, group) -> groupsIdMapping.put(group.getName(), id));

        HashMap<String, String> rolesIdMapping = new HashMap<>();
        organizationAsBbo.getRoles().forEach((id, role) -> rolesIdMapping.put(role.getName(), id));

        assignRolesToGroups(
                organizationAsBbo.getGroups(),
                groupsIdMapping,
                organizationAsBbo.getRoles(),
                rolesIdMapping,
                memberships);

        return organizationAsBbo;
    }
    
    public ActorMappings convertActorConfigToActorMappings(Configuration configuration) {
        return actorConfigMapper.doMapping(configuration);
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

            Set<String> isRoleInGroups = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_is_role_in, roleInOrganization::getProperties, roleInOrganization::setProperties);
            isRoleInGroups.add(groupInOrganization.getId());
        }
    }

}
