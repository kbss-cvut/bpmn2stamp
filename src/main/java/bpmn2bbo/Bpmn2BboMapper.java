package bpmn2bbo;

import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.mapstruct.factory.Mappers;

public class Bpmn2BboMapper {

    private final MapstructBpmnMapper mapper;

    public Bpmn2BboMapper() {
        this.mapper = Mappers.getMapper(MapstructBpmnMapper.class);
    }

    public Bpmn2BboMappingResult transform(TDefinitions bpmnDefinitions) {
        Bpmn2BboMappingResult mappingResult = mapper.definitions(bpmnDefinitions);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

}
