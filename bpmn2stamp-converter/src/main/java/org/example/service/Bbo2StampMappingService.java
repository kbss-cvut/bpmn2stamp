package org.example.service;

import org.example.mapper.bbo2stamp.Bbo2StampMappingResult;
import org.example.mapper.bbo2stamp.MapstructBbo2StampMapper;
import org.example.model.bbo.model.Thing;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

public class Bbo2StampMappingService {

    private final MapstructBbo2StampMapper mapper;

    public Bbo2StampMappingService() {
        this.mapper = Mappers.getMapper(MapstructBbo2StampMapper.class);
    }

    public Bbo2StampMappingResult transform(Collection<Thing> objects, String ontologyIri) {
        this.mapper.getConfiguration().setBaseIri(ontologyIri);
        Bbo2StampMappingResult mappingResult = mapper.process(objects);
        return mappingResult;
    }

}
