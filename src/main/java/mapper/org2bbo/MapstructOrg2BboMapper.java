package mapper.org2bbo;

import mapper.OntologyMapstructMapper;
import model.organization.Group;
import model.organization.Organization;
import model.organization.Role;
import org.mapstruct.*;
import utils.MappingUtils;

import java.util.Set;

@Mapper
public abstract class MapstructOrg2BboMapper extends OntologyMapstructMapper {

    private final Org2BboMappingResult result;

    protected MapstructOrg2BboMapper() {
        result = new Org2BboMappingResult(getMappedObjectsById());
    }

    public Org2BboMappingResult organization(Organization organization) {
        for (Group group : organization.getGroups().getGroup()) {
            model.bbo.model.Group g = groupToGroup(group);
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
    public abstract model.bbo.model.Group groupToGroup(Group group);
    @AfterMapping
    public void groupToGroupProperties(Group group, @MappingTarget model.bbo.model.Group groupResult) {
        getAfterMapping().add(() -> {
            if (group.getParentPath() == null)
                return;
            String[] split = group.getParentPath().split("/");
            String closestParent = split[split.length-1];
            String calculatedIdFromName = processId(closestParent);
            Set<model.bbo.model.Group> parents = Set.of((model.bbo.model.Group) getMappedObjectsById().get(calculatedIdFromName));
            groupResult.setIs_partOf(parents);
        });
    }

    @Mapping(target = "id", source = "name", qualifiedByName = "processId")
    public abstract model.bbo.model.Role roleToRole(Role role);

//    @AfterMapping
//    public void normalizeResultEntityId(@MappingTarget Thing anyResult) {
//        //TODO check if not already normalized
//        //FIXME move / to config; move to utils
//        String s = Vocabulary.ONTOLOGY_IRI_BPMNbasedOntology + Constants.ONTOLOGY_IRI_SUFFIX + anyResult.getId();
//        anyResult.setId(s);
//    }

    @Named("processId")
    @Override
    protected String processId(String id) {
        return MappingUtils.transformToUriCompliant(getTargetIdBase(), id);
    }

}
