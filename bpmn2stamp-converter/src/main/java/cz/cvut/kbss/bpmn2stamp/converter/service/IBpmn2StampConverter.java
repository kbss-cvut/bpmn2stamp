package cz.cvut.kbss.bpmn2stamp.converter.service;

import cz.cvut.kbss.bpmn2stamp.converter.mapper.bbo2stamp.Bbo2StampMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.bpmn2bbo.Bpmn2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.mapper.org2bbo.Org2BboMappingResult;
import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Organization;

import java.io.File;
import java.util.List;

public interface IBpmn2StampConverter {

    Bpmn2BboMappingResult transformBpmnToBbo(File bpmnFile);

    Bpmn2BboMappingResult transformBpmnToBbo(TDefinitions bpmnObjects);

    Org2BboMappingResult transformOrganizationStructureToBbo(File organizationStructureFile);

    Org2BboMappingResult transformOrganizationStructureToBbo(Organization organization);

    ActorMappings readActorMappingFile(File actorMapping);

    // TODO wrap result
    void connectRolesToGroups(OrganizationAsBbo organizationAsBbo, List<ActorMappings> actorMappingsList);

    Bbo2StampMappingResult transformBboToStamp(File bboFile);

    Bbo2StampMappingResult transformBboToStamp(Bbo bbo);

    Bbo mergeBboOntologies(OrganizationAsBbo organizationAsBbo, BpmnAsBbo bpmnAsBbo, List<ActorMappings> actorMappingsList);

}
