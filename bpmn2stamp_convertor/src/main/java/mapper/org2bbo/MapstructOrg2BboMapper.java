package mapper.org2bbo;

import common.ApplicationConstants;
import mapper.OntologyMapstructMapper;
import model.bbo.model.Group;
import model.bbo.model.Thing;
import model.organization.Organization;
import model.organization.Role;
import org.mapstruct.*;
import utils.MappingUtils;

import java.util.Set;

@Mapper
public abstract class MapstructOrg2BboMapper extends OntologyMapstructMapper<Thing> {

    private final Org2BboMappingResult result;

    protected MapstructOrg2BboMapper() {
        result = new Org2BboMappingResult(getMappedObjectsById());
    }

    public Org2BboMappingResult organization(Organization organization) {
        for (model.organization.Group group : organization.getGroups().getGroup()) {
            Group g = groupToGroup(group);
            result.getGroups().put(g.getId(), g);
            result.getGroupsIdMapping().put(group.getName(), g.getId());
        }
        for (Role role : organization.getRoles().getRole()) {
            model.bbo.model.Role r = roleToRole(role);
            result.getRoles().put(r.getId(), r);
            result.getRolesIdMapping().put(role.getName(), r.getId());
        }
        return result;
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    public abstract Group groupToGroup(model.organization.Group group);
    @AfterMapping
    public void groupToGroupProperties(model.organization.Group group, @MappingTarget Group groupResult) {
        getAfterMapping().add(() -> {
            if (group.getParentPath() == null)
                return;
            String[] split = group.getParentPath().split("/");
            String closestParent = split[split.length-1];
            String calculatedIdFromName = processId(closestParent);
            Set<Group> parents = Set.of((Group) getMappedObjectsById().get(calculatedIdFromName));
            groupResult.setIs_partOf(parents);
        });
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    public abstract model.bbo.model.Role roleToRole(Role role);

    @Override
    protected String getId(Thing individual) {
        return individual.getId();
    }

    @Named("processId")
    @Override
    protected String processId(String id) {
        String compliantId = MappingUtils.transformToUriCompliant(id);
        if (!getTargetIdBase().endsWith(ApplicationConstants.ONTOLOGY_IRI_SUFFIX)) {
            return getTargetIdBase() + ApplicationConstants.ONTOLOGY_IRI_SUFFIX + compliantId;
        }
        return getTargetIdBase() + compliantId;
    }

}
