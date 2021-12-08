package bpmn2java;

import com.google.common.collect.Streams;
import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.org2bbo.Org2BboMappingResult;
import model.actor.ActorMappings;
import model.bbo.model.Thing;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.organization.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Bpmn2BboService;
import service.BpmnReaderService;
import service.Organization2BboService;
import service.RdfWriterService;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class BpmnReaderServiceTest {

    private BpmnReaderService bpmnReaderService;

    @BeforeEach
    public void init() {
        this.bpmnReaderService = new BpmnReaderService();
    }

    @Test
    public void readFromXmlTest_resultShouldBePresent() throws IOException, JAXBException {
        TDefinitions tDefinitions = bpmnReaderService.readBpmn(
                "src/test/java/bpmn2java/data/Jednani-sag.bpmn");
        Organization organization = bpmnReaderService.readOrganizationStructure(
                "src/test/java/bpmn2java/data/ucl-zpracovani-informaci-o-bezpecnosti.xml");
        ActorMappings actors = bpmnReaderService.readActorMappings(
                "src/test/java/bpmn2java/data/Jednani-sag-actor-mapping.xml");

        Bpmn2BboService bpmnMapper = new Bpmn2BboService();
        Organization2BboService organization2BboService = new Organization2BboService();
        Bpmn2BboMappingResult bpmnResult = bpmnMapper.transform(tDefinitions);
        Org2BboMappingResult organizationResult = organization2BboService.transform(organization, actors);
//        List<Role> roles = bpmnMapper.connectByActorMapping(bpmnResult.getRoles().values(), organizationResult.getRoles().values(), actors);

        RdfWriterService rdfWriterService = new RdfWriterService();
        List<Thing> elements = Streams.concat(
                bpmnResult.getProcesses().values().stream(),
                bpmnResult.getFlowElements().values().stream(),
                bpmnResult.getRoles().values().stream()
        ).collect(Collectors.toList());
        rdfWriterService.save(elements);

        assertThat(bpmnResult).isNotNull();
        assertThat(organizationResult).isNotNull();
        System.out.println(bpmnResult);
        System.out.println(organizationResult);
    }
}