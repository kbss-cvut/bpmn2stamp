package service;

import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.bpmn2bbo.MapstructBpmnMapper;
import mapper.org2bbo.Org2BboMappingResult;
import model.bbo.model.Role;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Bpmn2BboService {

    private final MapstructBpmnMapper mapper;

    public Bpmn2BboService() {
        this.mapper = Mappers.getMapper(MapstructBpmnMapper.class);
    }

    public Bpmn2BboMappingResult transform(TDefinitions bpmnDefinitions) {
        Bpmn2BboMappingResult mappingResult = mapper.definitions(bpmnDefinitions);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

    public Bpmn2BboMappingResult mergeWithOrganization(Bpmn2BboMappingResult bpmn, Org2BboMappingResult org) {
        for (String roleName : org.getRoles().keySet()) {
            Optional<String> first = bpmn.getRoles().values()
                    .stream()
                    .map(Role::getName)
                    .filter(roleName::equals)
                    .findFirst();
            System.out.println(first);
        }
        return null;
    }

}
