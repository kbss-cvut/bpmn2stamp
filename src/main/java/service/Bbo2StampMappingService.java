package service;

import mapper.bbo2stamp.Bbo2StampMappingResult;
import mapper.bbo2stamp.MapstructBbo2StampMapper;
import model.bbo.model.Thing;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

public class Bbo2StampMappingService {

    private final MapstructBbo2StampMapper mapper;

    public Bbo2StampMappingService(String ontologyIri) {
        this.mapper = Mappers.getMapper(MapstructBbo2StampMapper.class);
        mapper.setTargetIdBase(ontologyIri);
    }

    public Bbo2StampMappingResult transform(Collection<Thing> objects) {
        Bbo2StampMappingResult mappingResult = mapper.convert(objects);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

}
