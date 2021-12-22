package service;

import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.bpmn2bbo.MapstructBpmn2BboMapper;
import model.actor.ActorMapping;
import model.actor.ActorMappings;
import model.actor.element.Membership;
import model.bbo.Vocabulary;
import model.bbo.model.Role;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.mapstruct.factory.Mappers;
import utils.MappingUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class Bpmn2BboMappingService {

    private final MapstructBpmn2BboMapper mapper;

    public Bpmn2BboMappingService() {
        this.mapper = Mappers.getMapper(MapstructBpmn2BboMapper.class);
    }

    public Bpmn2BboMappingResult transform(TDefinitions bpmnDefinitions, String ontologyIri) {
        this.mapper.getConfiguration().setBaseIri(ontologyIri);
        Bpmn2BboMappingResult mappingResult = mapper.process(bpmnDefinitions);
        return mappingResult;
    }

    public void connectByActorMapping(Collection<Role> bpmnActors, Collection<Role> orgRoles, ActorMappings actorMappings) {
        for (ActorMapping actorMapping : actorMappings.getActorMapping()) {
            String actorName = actorMapping.getName();
            for (Membership membership : actorMapping.getMemberships().getMembership()) {
                String targetRoleName = membership.getRole();
                Optional<Role> actorOpt = bpmnActors.stream().filter(e -> e.getName().equals(actorName)).findFirst();
                Optional<Role> roleOpt = orgRoles.stream().filter(e -> e.getName().equals(targetRoleName)).findFirst();
                roleOpt.ifPresent(r ->
                        actorOpt.ifPresent(a ->
                                assignActorToRole(a, r))
                );
            }
        }
    }

    private void assignActorToRole(Role actorRole, Role orgRole) {
        Set<String> isRolePartOf = MappingUtils.ensurePropertyValue(Vocabulary.s_p_is_role_partOf, actorRole::getProperties, actorRole::setProperties);
        isRolePartOf.add(orgRole.getId());
    }

}