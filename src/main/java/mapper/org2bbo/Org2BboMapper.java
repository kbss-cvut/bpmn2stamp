//package mapper.org2bbo;
//
//import mapper.OntologyMapstructMapper;
//import model.bbo.model.Thing;
//import model.organization.Organization;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
//
//@Mapper
//public class Org2BboMapper extends OntologyMapstructMapper<Thing> {
//
//    private final MapstructOrgMapper mapper;
//
//    public Org2BboMapper() {
//        this.mapper = Mappers.getMapper(MapstructOrgMapper.class);
//    }
//
//    @Override
//    protected String getId(Thing obj) {
//        return obj.getId();
//    }
//
//    public Org2BboMappingResult organization(Organization organization) {
//        Org2BboMappingResult mappingResult = mapper.organization(organization);
//        mapper.getAfterMapping().forEach(Runnable::run);
//        return mappingResult;
//    }
//
//}
