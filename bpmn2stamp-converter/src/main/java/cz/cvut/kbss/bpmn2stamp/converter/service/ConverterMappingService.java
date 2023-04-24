package cz.cvut.kbss.bpmn2stamp.converter.service;

import com.google.common.io.Files;
import cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.otherMappers.MapstructConfigToActorMapping;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.element.Membership;
import cz.cvut.kbss.bpmn2stamp.converter.model.actorConfig.Configuration;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.persistance.BboRdfRepositoryReader;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bbo2stamp.Bbo2StampMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bbo2stamp.MapstructBbo2StampMapper;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.Bpmn2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.MapstructBpmn2BboMapper;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.MapstructOrg2BboMapper;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.Org2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Group;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Role;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Organization;
import org.apache.commons.lang3.Validate;
import org.mapstruct.factory.Mappers;
import cz.cvut.kbss.bpmn2stamp.converter.utils.ConverterMappingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main class of the bpmn2stamp converter. Provides methods for mapping:
 * <ul>
 *     <li>BPMN definitions to BBO ontology</li>
 *     <li>Organization structure definitions to BBO ontology</li>
 *     <li>BBO ontology to STAMP ontology</li>
 * </ul>
 */
public class ConverterMappingService implements IBpmn2StampConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ConverterMappingService.class.getSimpleName());

    private final MapstructOrg2BboMapper org2BboMapper;
    private final MapstructBpmn2BboMapper bpmn2BboMapper;
    private final MapstructBbo2StampMapper bbo2StampMapper;
    private final MapstructConfigToActorMapping configToActorMappingMapper;
    private final ConverterOntologyFileReader fileReader;

    private final String baseBpmnAsBboOntologyIri;
    private final String baseOrganizationStructureAsBboOntologyIri;
    private final String baseStampOntologyIri;

    public ConverterMappingService(String baseBpmnAsBboOntologyIri, String baseOrganizationStructureAsBboOntologyIri, String baseStampOntologyIri) {
        this(baseBpmnAsBboOntologyIri, baseOrganizationStructureAsBboOntologyIri, baseStampOntologyIri, null);
    }

    public ConverterMappingService(String baseBpmnAsBboOntologyIri, String baseOrganizationStructureAsBboOntologyIri, String baseStampOntologyIri, Function<String, String> iriMappingFunction) {
        this.baseBpmnAsBboOntologyIri = baseBpmnAsBboOntologyIri;
        this.baseOrganizationStructureAsBboOntologyIri = baseOrganizationStructureAsBboOntologyIri;
        this.baseStampOntologyIri = baseStampOntologyIri;
        this.bpmn2BboMapper = Mappers.getMapper(MapstructBpmn2BboMapper.class);
        this.bpmn2BboMapper.getConfiguration().setBaseIri(baseBpmnAsBboOntologyIri);
        this.org2BboMapper = Mappers.getMapper(MapstructOrg2BboMapper.class);
        this.org2BboMapper.getConfiguration().setBaseIri(baseOrganizationStructureAsBboOntologyIri);
        this.bbo2StampMapper = Mappers.getMapper(MapstructBbo2StampMapper.class);
        this.bbo2StampMapper.getConfiguration().setBaseIri(baseStampOntologyIri);
        this.configToActorMappingMapper = Mappers.getMapper(MapstructConfigToActorMapping.class);
        this.fileReader = new ConverterOntologyFileReader();
        if (iriMappingFunction != null) {
            this.bpmn2BboMapper.getConfiguration().setIriMappingFunction(iriMappingFunction);
            this.org2BboMapper.getConfiguration().setIriMappingFunction(iriMappingFunction);
            this.bbo2StampMapper.getConfiguration().setIriMappingFunction(iriMappingFunction);
        }
    }

    @Override
    public Bpmn2BboMappingResult transformBpmnToBbo(TDefinitions bpmnObjects) {
        Validate.notNull(bpmnObjects);
        return bpmn2BboMapper.process(bpmnObjects);
    }

    @Override
    public Org2BboMappingResult transformOrganizationStructureToBbo(Organization organization) {
        Validate.notNull(organization);
        return org2BboMapper.process(organization);
    }

    @Override
    public void connectRolesToGroups(OrganizationAsBbo organizationAsBbo, List<ActorMappings> actorMappingsList) {
        if (organizationAsBbo == null || actorMappingsList == null) return;
        actorMappingsList.stream()
                .filter(Objects::nonNull)
                .map(ActorMappings::getActorMapping)
                .forEach(actorMappings -> {
                    List<Membership> memberships = actorMappings.stream()
                            .flatMap(e -> e.getMemberships().getMembership().stream())
                            .collect(Collectors.toList());

                    HashMap<String, String> groupsIdMapping = new HashMap<>();
                    organizationAsBbo.getGroups().forEach((id, group) -> groupsIdMapping.put(group.getName(), id));

                    HashMap<String, String> rolesIdMapping = new HashMap<>();
                    organizationAsBbo.getRoles().forEach((id, role) -> rolesIdMapping.put(role.getName(), id));

                    assignRolesToGroups(
                            organizationAsBbo.getGroups(),
                            groupsIdMapping,
                            organizationAsBbo.getRoles(),
                            rolesIdMapping,
                            memberships);
                });
    }

    @Override
    public Bbo2StampMappingResult transformBboToStamp(Bbo bbo) {
        Validate.notNull(bbo);
        Validate.notNull(bbo.getAllObjects());
        return bbo2StampMapper.process(bbo.getAllObjects());
    }

    @Override
    public List<Thing> mergeBboOntologies(OrganizationAsBbo organizationAsBbo, BpmnAsBbo
            bpmnAsBbo, List<ActorMappings> actorMappingsList) {
        actorMappingsList.stream()
                .filter(Objects::nonNull)
                .map(ActorMappings::getActorMapping)
                .flatMap(Collection::stream)
                .forEach(actorMapping -> {
                    String actorName = actorMapping.getName();
                    for (Membership membership : actorMapping.getMemberships().getMembership()) {
                        String targetRoleName = membership.getRole();
                        Optional<Role> actorOpt = bpmnAsBbo.getPerformers().values().stream().filter(e -> e.getName().equals(actorName)).findFirst();
                        Optional<Role> roleOpt = organizationAsBbo.getRoles().values().stream().filter(e -> e.getName().equals(targetRoleName)).findFirst();
                        roleOpt.ifPresent(r ->
                                actorOpt.ifPresent(a ->
                                        assignActorToRole(a, r))
                        );
                    }
                });
        return Stream.concat(organizationAsBbo.getAllObjects().values().stream(), bpmnAsBbo.getAllObjects().values().stream()).collect(Collectors.toList());
    }

    @Override
    public Bpmn2BboMappingResult transformBpmnToBbo(File bpmnFile) {
        TDefinitions tDefinitions = fileReader.readBpmn(bpmnFile.getAbsolutePath());
        return transformBpmnToBbo(tDefinitions);
    }

    @Override
    public Org2BboMappingResult transformOrganizationStructureToBbo(File organizationStructureFile) {
        Organization organization = fileReader.readOrganizationStructure(organizationStructureFile.getAbsolutePath());
        return transformOrganizationStructureToBbo(organization);
    }

    @Override
    public ActorMappings readActorMappingFile(File actorMappingFile) {
        String fileExtension = Files.getFileExtension(actorMappingFile.getName());
        if (fileExtension.equals(ApplicationConstants.CONF_FILE_EXTENSION)) {
            LOG.info("Actor mapping file {} was parsed as a 'conf' file", actorMappingFile.getName());
            Configuration config = fileReader.readActorMappingConfig(actorMappingFile.getAbsolutePath());
            return configToActorMappingMapper.doMapping(config);
        }
        LOG.info("Actor mapping file {} was parsed as a normal mapping file", actorMappingFile.getName());
        return fileReader.readActorMappings(actorMappingFile.getAbsolutePath());
    }

    @Override
    public Bbo2StampMappingResult transformBboToStamp(File bboFile) {
        BboRdfRepositoryReader bboRdfRepositoryReader = new BboRdfRepositoryReader(
                bboFile.getAbsolutePath(),
                baseBpmnAsBboOntologyIri
        );
        Bbo bbo = new Bbo(bboRdfRepositoryReader.readAll());
        return transformBboToStamp(bbo);
    }

    private void assignRolesToGroups(Map<String, Group> groups,
                                     Map<String, String> groupsIdMapping,
                                     Map<String, Role> roles,
                                     Map<String, String> rolesIdMapping,
                                     List<Membership> memberships) {
        for (Membership membership : memberships) {
            String[] groupsInPath = membership.getGroup().split("/");
            String s = groupsInPath[groupsInPath.length - 1];
            String lastGroupInPathId = groupsIdMapping.get(s);
            Group groupInOrganization = groups.get(lastGroupInPathId);

            String roleId = rolesIdMapping.get(membership.getRole());
            Role roleInOrganization = roles.get(roleId);

            Set<String> isRoleInGroups = ConverterMappingUtils.ensurePropertyValue(cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary.s_p_is_role_in, roleInOrganization::getProperties, roleInOrganization::setProperties);
            isRoleInGroups.add(groupInOrganization.getId());
        }
    }

    private void assignActorToRole(Role actorRole, Role orgRole) {
        Set<String> isRolePartOf = ConverterMappingUtils.ensurePropertyValue(cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary.s_p_is_role_partOf, actorRole::getProperties, actorRole::setProperties);
        isRolePartOf.add(orgRole.getId());
    }

    public String getBaseBpmnAsBboOntologyIri() {
        return baseBpmnAsBboOntologyIri;
    }

    public String getBaseOrganizationStructureAsBboOntologyIri() {
        return baseOrganizationStructureAsBboOntologyIri;
    }

    public String getBaseStampOntologyIri() {
        return baseStampOntologyIri;
    }

    public String getBboOntologyIri() {
        return cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary.ONTOLOGY_IRI_bbo_extension;
    }

    public String getStampOntologyIri() {
        return Vocabulary.ONTOLOGY_IRI_stamp;
    }
}
