package service;

import mapper.bbo2stamp.Bbo2StampMappingResult;
import mapper.bbo2stamp.MapstructBbo2StampMapper;
import model.bbo.model.Process;
import org.mapstruct.factory.Mappers;

public class Bbo2StampMappingService {

    private final MapstructBbo2StampMapper mapper;

    public Bbo2StampMappingService(String ontologyIri) {
        this.mapper = Mappers.getMapper(MapstructBbo2StampMapper.class);
        mapper.setTargetIdBase(ontologyIri);
    }

    public Bbo2StampMappingResult transform(Process process) {
        Bbo2StampMappingResult mappingResult = mapper.process(process);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

}
