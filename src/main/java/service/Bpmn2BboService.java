package service;

import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.bpmn2bbo.MapstructBpmn2BboMapper;
import model.actor.ActorMapping;
import model.actor.ActorMappings;
import model.actor.element.Membership;
import model.bbo.model.Role;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.mapstruct.factory.Mappers;

import java.util.*;

public class Bpmn2BboService {

    private final MapstructBpmn2BboMapper mapper;

    public Bpmn2BboService() {
        this.mapper = Mappers.getMapper(MapstructBpmn2BboMapper.class);
    }

    public Bpmn2BboMappingResult transform(TDefinitions bpmnDefinitions) {
        Bpmn2BboMappingResult mappingResult = mapper.processDefinitions(bpmnDefinitions);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

    public List<Role> connectByActorMapping(Collection<Role> bpmnActors, Collection<Role> orgRoles, ActorMappings actorMappings) {
        List<Role> connectedActors = new ArrayList<>();
        for (ActorMapping actorMapping : actorMappings.getActorMapping()) {
            String actorName = actorMapping.getName();
            for (Membership membership : actorMapping.getMemberships().getMembership()) {
                String targetRoleName = membership.getRole();
                Optional<Role> actorOpt = bpmnActors.stream().filter(e -> e.getName().equals(actorName)).findFirst();
                Optional<Role> roleOpt = orgRoles.stream().filter(e -> e.getName().equals(targetRoleName)).findFirst();
                roleOpt.ifPresent(r -> {
                    actorOpt.ifPresent(a -> {
                        if (a.getHas_role_part() == null) a.setHas_role_part(new HashSet<>());
                        a.getHas_role_part().add(r);
                        connectedActors.add(a);
                    });
                });
            }
        }
        return connectedActors;
    }

}
