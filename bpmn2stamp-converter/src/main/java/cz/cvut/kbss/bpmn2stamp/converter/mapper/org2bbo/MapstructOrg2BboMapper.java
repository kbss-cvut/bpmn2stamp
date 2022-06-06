package cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo;

import cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.OntologyMapstructMapper;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Role;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Group;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Organization;
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
        for (Group group : source.getGroups().getGroup()) {
            cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group g = groupToGroup(group);
            result.getOrganizationBbo().getGroups().put(getId(g), g);
        }
        for (Role role : source.getRoles().getRole()) {
            cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role r = roleToRole(role);
            result.getOrganizationBbo().getRoles().put(getId(r), r);
        }
        return result;
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processGroupId")
    @Mapping(target = "name", source = "name")
    abstract cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group groupToGroup(Group group);
    @AfterMapping
    void processGroupProperties(Group group, @MappingTarget cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group groupResult) {
        getAfterMapping().add(() -> {
            if (group.getParentPath() == null)
                return;
            String[] split = group.getParentPath().split("/");
            String closestParent = split[split.length-1];
            String calculatedIdFromName = processGroupId(closestParent);
            if (!getMappedObjectsById().containsKey(calculatedIdFromName)) {
                result.getWarnings().add(
                        Warning.create(group, "Parent is not null [%s] but no parents were found", closestParent)
                );
            } else {
                cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group parentGroup = (cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group) getMappedObjectsById().get(calculatedIdFromName);
                Set<cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group> parents = Set.of(parentGroup);
                groupResult.setIs_partOf(parents);
            }
        });
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processRoleId")
    abstract cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role roleToRole(Role role);

    @Override
    protected String getId(Thing individual) {
        return individual.getId();
    }
    
    @Named("processGroupId")
    protected String processGroupId(String name) {
        return processId(name + ApplicationConstants.ORG_GROUP_SUFFIX);
    }
    
    @Named("processRoleId")
    protected String processRoleId(String name) {
        return processId(name + ApplicationConstants.ORG_ROLE_SUFFIX);
    }

    @Named("processId")
    @Override
    protected String processId(String id) {
        return getConfiguration().getIriMappingFunction().apply(id);
    }

}
