package org2bbo;

import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.mapstruct.factory.Mappers;

public class Org2BboMapper {

    private final MapstructOrgMapper mapper;

    public Org2BboMapper() {
        this.mapper = Mappers.getMapper(MapstructOrgMapper.class);
    }

    public Org2BboMappingResult transform(TDefinitions bpmnDefinitions) {
        Org2BboMappingResult mappingResult = mapper.definitions(bpmnDefinitions);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

}
