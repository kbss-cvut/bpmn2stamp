//package mapper.org2bbo;
//
//import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.MapstructOrg2BboMapper;
//import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Group;
//import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Role;
//import org.junit.Before;
//import org.junit.Test;
//import org.mapstruct.factory.Mappers;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class MapstructOrg2BboMapperTest {
//
//    public MapstructOrg2BboMapper mapper;
////    public Organization testingOrganization;
//
//    @Before
//    public void init() {
//        this.mapper = Mappers.getMapper(MapstructOrg2BboMapper.class);
//        mapper.getConfiguration().setBaseIri("http://onto.fel.cvut.cz/ontologies/ucl/example/test-prestamp");
////        this.testingOrganization = new BpmnReaderService().readOrganizationStructure("src/test/java/mapper/org2bbo/data/Testing Organization.xml");
//    }
//
//    @Test
//    public void organization() {
//
//    }
//
//    @Test
//    public void groupToGroup() {
//        Group hrGroup = createGroup(
//                "HR group name",
//                "/Management",
//                "HR group description");
//
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group actualGroup = mapper.groupToGroup(hrGroup);
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group expectedGroup = createBboGroup(
//                "http://onto.fel.cvut.cz/ontologies/ucl/example/test-prestamp/hr_group_name",
//                "HR group name",
//                "HR group description"
//        );
//
//        assertThat(actualGroup).usingRecursiveComparison().isEqualTo(expectedGroup);
//    }
//
//    @Test
//    public void groupToGroupProperties() {
//        Group hrGroup = createGroup(
//                "HR group name",
//                "/Management",
//                "HR group description");
//
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group actualGroup = mapper.groupToGroup(hrGroup);
//        mapper.processGroupProperties(hrGroup, actualGroup);
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group expectedGroup = createBboGroup(
//                "http://onto.fel.cvut.cz/ontologies/ucl/example/test-prestamp/hr_group_name",
//                "HR group name",
//                "HR group description"
//        );
//
//        assertThat(actualGroup).usingRecursiveComparison().isEqualTo(expectedGroup);
//    }
//
//    @Test
//    public void roleToRole() {
//        Role hrRole = createRole(
//                "HR role",
////                "/Management",
//                "HR role description");
//
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role actualRole = mapper.roleToRole(hrRole);
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role expectedRole = createBboRole(
//                "http://onto.fel.cvut.cz/ontologies/ucl/example/test-prestamp/hr_role",
//                "HR role",
//                "HR role description"
//        );
//
//        assertThat(actualRole).usingRecursiveComparison().isEqualTo(expectedRole);
//    }
//
//    // TODO move to helper class
//
//    private Group createGroup(
//            String name,
//            String parentPath,
//            String description) {
//        Group group = new Group();
//        group.setName(name);
//        group.setParentPath(parentPath);
//        group.setDescription(description);
//        return group;
//    }
//    private cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group createBboGroup(
//            String id,
//            String name,
//            String description) {
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group group = new cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group();
//        group.setId(id);
//        group.setName(name);
//        group.setDescription(description);
//        return group;
//    }
//    private Role createRole(
//            String name,
////            String parentPath,
//            String description) {
//        Role role = new Role();
//        role.setName(name);
//        role.setDescription(description);
//        return role;
//    }
//    private cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role createBboRole(
//            String id,
//            String name,
//            String description) {
//        cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role role = new cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role();
//        role.setId(id);
//        role.setName(name);
//        role.setDescription(description);
//        return role;
//    }
//
//}