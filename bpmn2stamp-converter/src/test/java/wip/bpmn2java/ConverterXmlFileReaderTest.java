package wip.bpmn2java;

import com.google.common.collect.Sets;
import org.example.mapper.bpmn2bbo.Bpmn2BboMappingResult;
import org.example.mapper.org2bbo.Org2BboMappingResult;
import org.example.model.actor.ActorMappings;
import org.example.model.bbo.model.FlowElement;
import org.example.model.bbo.model.Process;
import org.example.model.bbo.model.Thing;
import org.example.model.bbo.model.UserTask;
import org.example.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.example.model.organization.Organization;
import org.example.service.Bbo;
import org.example.service.ConverterMappingService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.example.persistance.BboRdfRepositoryReader;
import org.example.persistance.RdfRepositoryWriter;
import org.example.service.ConverterXmlFileReader;
import org.example.service.Organization2BboMappingService;
import org.example.service.OrganizationAsBbo;

import java.io.File;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test is for development purposes only and will be deleted
 */
public class ConverterXmlFileReaderTest {

    private ConverterXmlFileReader converterXmlFileReader;

    @Before
    public void init() {
        this.converterXmlFileReader = new ConverterXmlFileReader();
    }

    @Test
    public void mapAndWriteTestNew() {
//        ConverterMappingService service = new ConverterMappingService();

        TDefinitions tDefinitions = converterXmlFileReader.readBpmn(
                "src/test/java/service/data/Jednani-sag.bpmn");
        Organization organization = converterXmlFileReader.readOrganizationStructure(
                "src/test/java/service/data/ucl-zpracovani-informaci-o-bezpecnosti.xml");
        ActorMappings actors = converterXmlFileReader.readActorMappings(
                "src/test/java/service/data/Jednani-sag-actor-mapping.xml");
        String bpmnOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn";
        String organizationStructureOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure";
        String preStampOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-prestamp";

        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService();
        Org2BboMappingResult organizationResult = organization2BboMappingService.transform(
                organization,
                organizationStructureOntologyIri
        );
        OrganizationAsBbo organizationAsBbo = organization2BboMappingService.extendOrganizationHierarchy(organizationResult.getOrganizationBbo(), actors);
        RdfRepositoryWriter organizationRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-organization-structure.ttl",
                organizationStructureOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        );
        // TODO same problem as in Organization2BboMappingServiceTest
//        organizationRepoWriter.write(organizationAsBbo.getAllObjects().values());
//
//
//        RdfRepositoryWriter bpmnRepoWriter = new RdfRepositoryWriter(
//                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
//                bpmnOntologyIri,
//                Sets.newHashSet(bpmnOntologyIri)
//        );
//
//        BboRdfRepositoryReader rdfRepositoryReader = new BboRdfRepositoryReader(
//                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
//                "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn"
//        );
//        List<Thing> list = rdfRepositoryReader.readAll();
//
//        BboRdfRepositoryReader rdfRepositoryReader2 = new BboRdfRepositoryReader(
//                "./src/main/resources/jopa/jednani-sag-organization-structure.ttl",
//                "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure"
//        );
//        List<Thing> list2 = rdfRepositoryReader2.readAll();
//        list.addAll(list2);
//        RdfRepositoryWriter preStampRepoWriter = new RdfRepositoryWriter(
//                "./src/main/resources/jopa/jednani-sag-prestamp.ttl",
//                preStampOntologyIri,
//                Sets.newHashSet("http://onto.fel.cvut.cz/ontologies/stamp", bpmnOntologyIri)
//        );
    }

    @Test
    public void testRun() {
        ConverterMappingService converterMappingService = new ConverterMappingService(
"prefix-bpmn",
"prefix-organization-structure",
"prefix-pre-stamp"
        );

//        Bbo bbo = performConversionToBbo(bpmnFile, orgFile, actorMappingFiles);
        File bpmnFile = new File("src/test/java/service/data/Jednani-sag.bpmn");
        File orgFile = new File("src/test/java/service/data/ucl-zpracovani-informaci-o-bezpecnosti.xml");
        List<File> actorsFiles = List.of(new File("src/test/java/service/data/Jednani-sag-actor-mapping.xml"));
        Bpmn2BboMappingResult result = converterMappingService.transformBpmnToBbo(bpmnFile);
        Org2BboMappingResult result1 = converterMappingService.transformOrganizationStructureToBbo(orgFile);
        List<ActorMappings> actorMappingsList = new ArrayList<>();
        for (File amFile : actorsFiles) {
            ActorMappings actorMappings = converterMappingService.readActorMappingFile(amFile);
            actorMappingsList.add(actorMappings);
        }
        converterMappingService.connectRolesToGroups(result1.getOrganizationBbo(), actorMappingsList);
        Bbo bbo = converterMappingService.mergeBboOntologies(result1.getOrganizationBbo(), result.getBpmnAsBbo(), actorMappingsList);

        // TODO resolve onto iri conflict
//        RdfRepositoryWriter rdfRepositoryWriter = new RdfRepositoryWriter(
//                new File("src/test/java/service/data/output.ttl").getAbsolutePath(),
//                converterMappingService.getBaseBpmnAsBboOntologyIri(),
//                Sets.newHashSet(converterMappingService.getBboOntologyIri())
//        );
//        rdfRepositoryWriter.write(bbo.getAllObjects());
    }

    @Test
    @Ignore
    public void mapAndWriteTest() {
        TDefinitions tDefinitions = converterXmlFileReader.readBpmn(
                "src/test/java/wip/bpmn2java/data/Zpracování informací o bezpečnosti - 1.0.bpmn");
        Organization organization = converterXmlFileReader.readOrganizationStructure(
                "src/test/java/wip/bpmn2java/data/ucl-zpracovani-informaci-o-bezpecnosti.xml");
        ActorMappings actors = converterXmlFileReader.readActorMappings(
                "src/test/java/wip/bpmn2java/data/Jednani-sag-actor-mapping.xml");
        String bpmnOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn";
        String organizationStructureOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure";
        String preStampOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-prestamp";

        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService();
        Org2BboMappingResult organizationResult = organization2BboMappingService.transform(
                organization,
                organizationStructureOntologyIri
        );
        OrganizationAsBbo organizationAsBbo = organization2BboMappingService.extendOrganizationHierarchy(organizationResult.getOrganizationBbo(), actors);
        RdfRepositoryWriter organizationRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-organization-structure.ttl",
                organizationStructureOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        );
        organizationRepoWriter.write(organizationAsBbo.getAllObjects().values());

        RdfRepositoryWriter bpmnRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
                bpmnOntologyIri,
                Sets.newHashSet(organizationStructureOntologyIri)
        );

        BboRdfRepositoryReader rdfRepositoryReader = new BboRdfRepositoryReader(
                "./src/main/resources/jopa/jednani-sag-bpmn.ttl",
                "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn"
        );
        List<Thing> list = rdfRepositoryReader.readAll();

        BboRdfRepositoryReader rdfRepositoryReader2 = new BboRdfRepositoryReader(
                "./src/main/resources/jopa/jednani-sag-organization-structure.ttl",
                "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure"
        );
        List<Thing> list2 = rdfRepositoryReader2.readAll();
        list.addAll(list2);
        RdfRepositoryWriter preStampRepoWriter = new RdfRepositoryWriter(
                "./src/main/resources/jopa/jednani-sag-prestamp.ttl",
                preStampOntologyIri,
                Sets.newHashSet("http://onto.fel.cvut.cz/ontologies/stamp", bpmnOntologyIri)
        );
    }

    @Test
    @Ignore
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
    @Ignore
    public void readTest_whenObjectExists_shouldReturnCorrectResult() {
        BboRdfRepositoryReader rdfRepositoryReader = new BboRdfRepositoryReader(
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
    @Ignore
    public void readTest_whenObjectExists_inferredFieldsShouldBeSet() {
        BboRdfRepositoryReader rdfRepositoryReader = new BboRdfRepositoryReader(
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