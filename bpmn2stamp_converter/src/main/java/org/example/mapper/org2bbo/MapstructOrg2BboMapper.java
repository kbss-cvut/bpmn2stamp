package org.example.mapper.org2bbo;

import org.example.mapper.OntologyMapstructMapper;
import org.example.model.bbo.model.Thing;
import org.example.model.organization.Organization;
import org.example.model.organization.Role;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;

@Mapper
public abstract class MapstructOrg2BboMapper extends OntologyMapstructMapper<Organization, Thing, Org2BboMappingResult> {

    private final Org2BboMappingResult result;

    protected MapstructOrg2BboMapper() {
        result = new Org2BboMappingResult(getMappedObjectsById());
        result.getOrganizationBbo().setAllObjects(getMappedObjectsById());
    }

    @Override
    protected Org2BboMappingResult doMapping(Organization source) {
        for (org.example.model.organization.Group group : source.getGroups().getGroup()) {
            org.example.model.bbo.model.Group g = groupToGroup(group);
            result.getOrganizationBbo().getGroups().put(g.getId(), g);
        }
        for (Role role : source.getRoles().getRole()) {
            org.example.model.bbo.model.Role r = roleToRole(role);
            result.getOrganizationBbo().getRoles().put(r.getId(), r);
        }
        return result;
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    @Mapping(target = "name", source = "name")
    abstract org.example.model.bbo.model.Group groupToGroup(org.example.model.organization.Group group);
    @AfterMapping
    void processGroupProperties(org.example.model.organization.Group group, @MappingTarget org.example.model.bbo.model.Group groupResult) {
        getAfterMapping().add(() -> {
            if (group.getParentPath() == null)
                return;
            String[] split = group.getParentPath().split("/");
            String closestParent = split[split.length-1];
            String calculatedIdFromName = processId(closestParent);
            if (!getMappedObjectsById().containsKey(calculatedIdFromName)) {
                result.getWarnings().add(
                        Warning.create(group, "Parent is not null [%s] but no parents were found", closestParent)
                );
            } else {
                org.example.model.bbo.model.Group parentGroup = (org.example.model.bbo.model.Group) getMappedObjectsById().get(calculatedIdFromName);
                Set<org.example.model.bbo.model.Group> parents = Set.of(parentGroup);
                groupResult.setIs_partOf(parents);
            }
        });
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    abstract org.example.model.bbo.model.Role roleToRole(Role role);

    @Override
    protected String getId(Thing individual) {
        return individual.getId();
    }

    @Named("processId")
    @Override
    protected String processId(String id) {
        return getConfiguration().getIriMappingFunction().apply(id);
    }

}
