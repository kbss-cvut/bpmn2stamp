package org.example.service;

import org.example.mapper.bbo2stamp.Bbo2StampMappingResult;
import org.example.mapper.bbo2stamp.MapstructBbo2StampMapper;
import org.example.mapper.bpmn2bbo.Bpmn2BboMappingResult;
import org.example.mapper.bpmn2bbo.MapstructBpmn2BboMapper;
import org.example.mapper.org2bbo.MapstructOrg2BboMapper;
import org.example.mapper.org2bbo.Org2BboMappingResult;
import org.example.model.actor.ActorMappings;
import org.example.model.actor.element.Membership;
import org.example.model.bbo.Vocabulary;
import org.example.model.bbo.model.Group;
import org.example.model.bbo.model.Role;
import org.example.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.example.model.organization.Organization;
import org.apache.commons.lang3.Validate;
import org.mapstruct.factory.Mappers;
import org.example.persistance.BboRdfRepositoryReader;
import org.example.utils.ConverterMappingUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Main class of the bpmn2stamp converter. Provides methods for mapping:
 * <ul>
 *     <li>BPMN definitions to BBO ontology</li>
 *     <li>Organization structure definitions to BBO ontology</li>
 *     <li>BBO ontology to STAMP ontology</li>
 * </ul>
 */
public class ConverterMappingService implements IBpmn2StampConverter {

    private final MapstructOrg2BboMapper org2BboMapper;
    private final MapstructBpmn2BboMapper bpmn2BboMapper;
    private final MapstructBbo2StampMapper bbo2StampMapper;
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
    public Bbo mergeBboOntologies(OrganizationAsBbo organizationAsBbo, BpmnAsBbo
            bpmnAsBbo, List<ActorMappings> actorMappingsList) {
        Bbo bbo = new Bbo(bpmnAsBbo, organizationAsBbo);
        actorMappingsList.stream()
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
        return bbo;
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

            Set<String> isRoleInGroups = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_is_role_in, roleInOrganization::getProperties, roleInOrganization::setProperties);
            isRoleInGroups.add(groupInOrganization.getId());
        }
    }

    private void assignActorToRole(Role actorRole, Role orgRole) {
        Set<String> isRolePartOf = ConverterMappingUtils.ensurePropertyValue(Vocabulary.s_p_is_role_partOf, actorRole::getProperties, actorRole::setProperties);
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
        return Vocabulary.ONTOLOGY_IRI_BPMNbasedOntology;
    }

    public String getStampOntologyIri() {
        return org.example.model.stamp.Vocabulary.ONTOLOGY_IRI_stamp;
    }
}
