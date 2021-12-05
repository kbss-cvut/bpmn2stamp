package bpmn2java;

import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.org2bbo.Org2BboMappingResult;
import model.actor.ActorMappings;
import model.organization.Organization;
import service.Bpmn2BboService;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BpmnReaderService;
import service.Organization2BboService;

import javax.xml.bind.JAXBException;
import java.io.IOException;

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
                "src\\test\\java\\bpmn2java\\data\\Jednani-sag.bpmn");
        Organization organization = bpmnReaderService.readOrganizationStructure(
                "src\\test\\java\\bpmn2java\\data\\ucl-zpracovani-informaci-o-bezpecnosti.xml");
        ActorMappings actors = bpmnReaderService.readActorMappings(
                "src\\test\\java\\bpmn2java\\data\\Jednani-sag-actor-mapping.xml");

        Bpmn2BboService bpmnMapper = new Bpmn2BboService();
        Organization2BboService organization2BboService = new Organization2BboService();
        Bpmn2BboMappingResult bpmnResult = bpmnMapper.transform(tDefinitions);
        Org2BboMappingResult organizationResult = organization2BboService.transform(organization, actors);
        bpmnMapper.mergeWithOrganization(bpmnResult, organizationResult);

//        RdfWriterService s = new RdfWriterService();
//        definitions.object.setInstance_of("ASD");
//        s.save(definitions.object);
//        RdfWriterService rdfWriterService = new RdfWriterService();
//        List<Thing> elements = Streams.concat(
//                transform.getProcesses().values().stream(),
//                transform.getFlowElements().values().stream(),
//                transform.getRoles().values().stream()
//        ).collect(Collectors.toList());
//        rdfWriterService.save(elements);

        assertThat(bpmnResult).isNotNull();
        assertThat(organizationResult).isNotNull();
        System.out.println(bpmnResult);
        System.out.println(organizationResult);
    }
}