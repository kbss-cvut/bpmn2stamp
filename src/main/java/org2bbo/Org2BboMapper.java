package org2bbo;

import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.org.Organization;
import org.mapstruct.factory.Mappers;

public class Org2BboMapper {

    private final MapstructOrgMapper mapper;

    public Org2BboMapper() {
        this.mapper = Mappers.getMapper(MapstructOrgMapper.class);
    }

    public Org2BboMappingResult transform(Organization organization) {
        Org2BboMappingResult mappingResult = mapper.definitions(organization);
        mapper.getAfterMapping().forEach(Runnable::run);
        return mappingResult;
    }

}
