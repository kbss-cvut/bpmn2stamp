package service;

import com.google.common.collect.Sets;
import mapper.org2bbo.Org2BboMappingResult;
import model.bbo.model.Group;
import model.bbo.model.Role;
import model.bbo.model.Thing;
import model.organization.Organization;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import persistance.BboRdfRepositoryReader;
import persistance.RdfRepositoryWriter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Bpmn2BboMappingServiceTest {

    private static final String TESTING_DATA_DIR = "src/test/java/service/data/";
    private static final String TEMP_FILE_SUFFIX = "-actual.ttl";

    private Bpmn2BboMappingService service;
    private FileReadingService fileReadingService;

    @Before
    public void init() {
        this.service = new Bpmn2BboMappingService();
        this.fileReadingService = new FileReadingService();
    }

    @Test
    @Ignore
    public void testTransform_shouldGenerateCorrectOntology() {
        String outputOntologyIri = "http://onto.fel.cvut.cz/ontologies/ucl/example/test-bpmn";
        // read testing organization structure xml
        Organization organization = fileReadingService.readOrganizationStructure(
                TESTING_DATA_DIR + "wip/Testing Organization.xml");

        // run transformation on read model
        Organization2BboMappingService organization2BboMappingService = new Organization2BboMappingService();
        Org2BboMappingResult actualTransformationResult = organization2BboMappingService.transform(
                organization,
                outputOntologyIri
        );

        // save actual result and read it from file
        new RdfRepositoryWriter(
                TESTING_DATA_DIR + "test-organization-structure" + TEMP_FILE_SUFFIX,
                outputOntologyIri,
                Sets.newHashSet("http://BPMNbasedOntology")
        ).write(actualTransformationResult.getOrganizationBbo().getAllObjects().values());
        List<Thing> actualThings = new BboRdfRepositoryReader(
                TESTING_DATA_DIR + "wip/test-organization-structure.ttl",
                outputOntologyIri
        ).readAll();

        // read expected result from file
        BboRdfRepositoryReader bboRdfRepositoryReader = new BboRdfRepositoryReader(
                TESTING_DATA_DIR + "wip/test-organization-structure.ttl",
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

        System.out.println("|||");
    }

    @After
    public void cleanup() {
        File testingDataFolder = new File("src/test/java/service/data");
        for (File file : testingDataFolder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(TEMP_FILE_SUFFIX))
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