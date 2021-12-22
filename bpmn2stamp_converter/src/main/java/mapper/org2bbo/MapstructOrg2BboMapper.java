package mapper.org2bbo;

import mapper.OntologyMapstructMapper;
import model.bbo.model.Group;
import model.bbo.model.Thing;
import model.organization.Organization;
import model.organization.Role;
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
        for (model.organization.Group group : source.getGroups().getGroup()) {
            Group g = groupToGroup(group);
            result.getOrganizationBbo().getGroups().put(g.getId(), g);
        }
        for (Role role : source.getRoles().getRole()) {
            model.bbo.model.Role r = roleToRole(role);
            result.getOrganizationBbo().getRoles().put(r.getId(), r);
        }
        return result;
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    @Mapping(target = "name", source = "name")
    abstract Group groupToGroup(model.organization.Group group);
    @AfterMapping
    void processGroupProperties(model.organization.Group group, @MappingTarget Group groupResult) {
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
                Group parentGroup = (Group) getMappedObjectsById().get(calculatedIdFromName);
                Set<Group> parents = Set.of(parentGroup);
                groupResult.setIs_partOf(parents);
            }
        });
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    abstract model.bbo.model.Role roleToRole(Role role);

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
