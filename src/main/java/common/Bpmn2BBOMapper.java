package common;

import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class Bpmn2BBOMapper {

    private MapstructBpmnMapper mapper;

    public Bpmn2BBOMapper() {
        this.mapper = Mappers.getMapper(MapstructBpmnMapper.class);
    }

    public BBOMappingResult transform(TDefinitions bpmnDefinitions) {
        BBOMappingResult mappingResult = mapper.definitions(bpmnDefinitions);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

}
