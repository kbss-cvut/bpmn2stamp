package bpmn2java;

import com.google.common.collect.Sets;
import mapper.bbo2stamp.Bbo2StampMappingResult;
import mapper.bpmn2bbo.Bpmn2BboMappingResult;
import mapper.org2bbo.Org2BboMappingResult;
import model.actor.ActorMappings;
import model.bbo.model.FlowElement;
import model.bbo.model.Process;
import model.bbo.model.UserTask;
import model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import model.organization.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistance.RdfRepositoryReader;
import persistance.RdfRepositoryWriter;
import service.Bbo2StampMappingService;
import service.Bpmn2BboMappingService;
import service.BpmnReaderService;
import service.Organization2BboMappingService;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class BpmnReaderServiceTest {

    private BpmnReaderService bpmnReaderService;

    @BeforeEach
    public void init() {
        this.bpmnReaderService = new BpmnReaderService();
    }

    @Test
    public void mapAndWriteTest() {
        TDefinitions tDefinitions = bpmnReaderService.readBpmn(
                "src/test/java/bpmn2java/data/Jednani-sag.bpmn");
        Organization organization = bpmnReaderService.readOrganizationStructure(
                "src/test/java/bpmn2java/data/ucl-zpracovani-informaci-o-bezpecnosti.xml");
        ActorMappings actors = bpmnReaderService.readActorMappings(
                "src/test/java/bpmn2java/data/Jednani-sag-actor-mapping.xml");
        String bpmnOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn";
        String organizationStructureOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure";
        String preStampOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-prestamp";

        Bpmn2BboMappingService bpmnMapper = new Bpmn2BboMappingService(bpmnOntologyIri);
        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService(organizationStructureOntologyIri);

        Bpmn2BboMappingResult bpmnResult = bpmnMapper.transform(tDefinitions);
        Org2BboMappingResult organizationResult = organization2BboMappingService.transform(organization);
        bpmnMapper.connectByActorMapping(bpmnResult.getRoles().values(), organizationResult.getRoles().values(), actors);

        Bbo2StampMappingService bbo2StampMappingService = new Bbo2StampMappingService(preStampOntologyIri);
        Bbo2StampMappingResult preStampResult = bbo2StampMappingService.transform(bpmnResult.getProcesses().values().stream().findFirst().get());

        RdfRepositoryWriter organizationRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-organization-structure.ttl",
                organizationStructureOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        );
        organizationRepoWriter.write(organizationResult.getMappedObjects().values());

        RdfRepositoryWriter bpmnRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
                bpmnOntologyIri,
                Sets.newHashSet(organizationStructureOntologyIri)
        );
        bpmnRepoWriter.write(bpmnResult.getMappedObjects().values());

        RdfRepositoryWriter preStampRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-prestamp.ttl",
                preStampOntologyIri,
                Sets.newHashSet("http://onto.fel.cvut.cz/ontologies/stamp", bpmnOntologyIri)
        );

        preStampRepoWriter.write(preStampResult.getMappedObjects().values());
    }

    @Test
    public void writeTest_usingNewEntities() {
        String testOntologyIri = "http://test.cz";

        RdfRepositoryWriter organizationRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/test.ttl",
                testOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        );

        Process process = new Process();
        process.setId("http://test.cz/process-1");
        process.setName("process");

        organizationRepoWriter.write(Collections.singleton(process));

        UserTask userTask = new UserTask();
        userTask.setId("http://test.cz/user-task-1");
        userTask.setName("user task");
//        userTask.getHas_container() // inferred

        HashSet<FlowElement> objects = new HashSet<>();
        objects.add(userTask);

        organizationRepoWriter.getEm().getTransaction().begin();
        Process process1 = organizationRepoWriter.getEm().find(Process.class, "http://test.cz/process-1");
        process1.setHas_flowElements(objects);
        organizationRepoWriter.getEm().persist(userTask);
        organizationRepoWriter.getEm().getTransaction().commit();
    }

    @Test
    public void readTest_whenObjectExists_shouldReturnCorrectResult() {
        RdfRepositoryReader rdfRepositoryReader = new RdfRepositoryReader(
                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
                "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn"
        );

        Process actualElementContainer = rdfRepositoryReader.read(Process.class, "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/_k8c2ajzueembjoiocbypca");
        Process expectedElementContainer = new Process();
        expectedElementContainer.setId("http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/_k8c2ajzueembjoiocbypca");
        expectedElementContainer.setName("Jednání SAG");

        assertThat(actualElementContainer).usingRecursiveComparison()
                .ignoringFields("has_flowElements")
                .isEqualTo(expectedElementContainer);

        assertThat(actualElementContainer.getHas_flowElements()).hasSize(15);
    }

    @Test
    public void readTest_whenObjectExists_inferredFieldsShouldBeSet() {
        RdfRepositoryReader rdfRepositoryReader = new RdfRepositoryReader(
                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
                "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn"
        );

        UserTask actualElement = rdfRepositoryReader.read(UserTask.class, "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/_3m86qlqleeq4rbag2swqzq");
        UserTask expectedElement = new UserTask();
        expectedElement.setId("http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/_3m86qlqleeq4rbag2swqzq");
        expectedElement.setName("Informování o stavu otevřených úkolů");

        assertThat(actualElement).usingRecursiveComparison()
                .ignoringFields("has_container")
                .isEqualTo(expectedElement);

        assertThat(actualElement.getHas_container()).isNotNull();
    }
}