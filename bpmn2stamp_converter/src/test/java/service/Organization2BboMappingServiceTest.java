package service;

import com.google.common.collect.Sets;
import org.example.mapper.org2bbo.Org2BboMappingResult;
import org.example.model.actor.ActorMappings;
import org.example.model.bbo.model.Group;
import org.example.model.bbo.model.Role;
import org.example.model.bbo.model.Thing;
import org.example.model.organization.Organization;
import org.example.service.ConverterXmlFileReader;
import org.example.service.Organization2BboMappingService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.persistance.BboRdfRepositoryReader;
import org.example.persistance.RdfRepositoryWriter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Organization2BboMappingServiceTest {
    
    private static final String TESTING_DATA_DIR = "src/test/java/service/data/";
    private static final String TEMP_FILE_SUFFIX = "-actual";

    private Organization2BboMappingService service;
    private ConverterXmlFileReader converterXmlFileReader;

    @Before
    public void init() {
        this.service = new Organization2BboMappingService();
        this.converterXmlFileReader = new ConverterXmlFileReader();
    }

    @Test
    public void testTransform_shouldGenerateCorrectOntology() {
        String outputOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure";
        String inputOrgStructureFile = TESTING_DATA_DIR + "ucl-zpracovani-informaci-o-bezpecnosti.xml";
        String inputActorMappingFile = TESTING_DATA_DIR + "Jednani-sag-actor-mapping.xml";
        String expectedDataFile = TESTING_DATA_DIR + "jednani-sag-organization-structure.ttl";
        String actualDataFile = TESTING_DATA_DIR + "jednani-sag-organization-structure" + TEMP_FILE_SUFFIX + ".ttl";

        // read testing organization structure xml
        Organization organization = converterXmlFileReader.readOrganizationStructure(inputOrgStructureFile);

        // run transformation on read model
        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService();
        Org2BboMappingResult actualTransformationResult = organization2BboMappingService.transform(
                organization,
                outputOntologyIri
        );

        // read testing actor mapping xml
        ActorMappings actorMapping = converterXmlFileReader.readActorMappings(inputActorMappingFile);

        // resolve hierarchy
        organization2BboMappingService.extendOrganizationHierarchy(
                actualTransformationResult.getOrganizationBbo(),
                actorMapping
        );

        // save actual result and read it from file
        new RdfRepositoryWriter(
                actualDataFile,
                outputOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        ).write(actualTransformationResult.getOrganizationBbo().getAllObjects().values());
        List<Thing> actualThings = new BboRdfRepositoryReader(
                actualDataFile,
                outputOntologyIri
        ).readAll();

        // read expected result from file
        BboRdfRepositoryReader bboRdfRepositoryReader = new BboRdfRepositoryReader(
                expectedDataFile,
                outputOntologyIri
        );
        List<Thing> expectedThings = bboRdfRepositoryReader.readAll();
        
        //verification
        // verify transformation result has no warnings
        assertThat(actualTransformationResult.getWarnings()).isEmpty();

        // verify transformation result has correct groups
        Map<String, Group> actualGroups = extractFromCollection(actualThings, Group.class);
        Map<String, Group> expectedGroups = extractFromCollection(expectedThings, Group.class);
        assertThat(actualTransformationResult.getOrganizationBbo().getGroups()).hasSameSizeAs(expectedGroups);
        compareMaps(actualGroups, expectedGroups);

        // verify transformation result has correct roles
        Map<String, Role> actualRoles = extractFromCollection(actualThings, Role.class);
        Map<String, Role> expectedRoles = extractFromCollection(expectedThings, Role.class);
        assertThat(actualTransformationResult.getOrganizationBbo().getRoles()).hasSameSizeAs(expectedRoles);
        compareMaps(actualRoles, expectedRoles);
    }

    @After
    public void cleanup() {
        File testingDataFolder = new File("src/test/java/service/data");
        for (File file : testingDataFolder.listFiles()) {
            if (file.isFile() && file.getName().contains(TEMP_FILE_SUFFIX))
                file.delete();
        }
    }

    private <T extends Thing> void compareMaps(Map<String, T> actual, Map<String, T> expected) {
        for (Map.Entry<String, T> expectedPair : expected.entrySet()) {
            String expectedId = expectedPair.getKey();
            T expectedRole = expectedPair.getValue();
            T actualRole = actual.get(expectedId);
            assertThat(actualRole).usingRecursiveComparison().isEqualTo(expectedRole);
        }
    }

    private <T extends Thing> Map<String, T> extractFromCollection(Collection<Thing> collection, Class<T> classToExtract) {
        Map<String, T> res = new HashMap<>();
        for (Thing thing : collection) {
            if (classToExtract.isInstance(thing)) {
                res.put(thing.getId(), classToExtract.cast(thing));
            }
        }
        return res;
    }

}