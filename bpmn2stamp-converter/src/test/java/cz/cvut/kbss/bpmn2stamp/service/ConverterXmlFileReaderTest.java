package cz.cvut.kbss.bpmn2stamp.service;

import cz.cvut.kbss.bpmn2stamp.converter.model.actor.ActorMappings;
import cz.cvut.kbss.bpmn2stamp.converter.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import cz.cvut.kbss.bpmn2stamp.converter.model.organization.Organization;
import cz.cvut.kbss.bpmn2stamp.converter.service.ConverterMappingService;
import cz.cvut.kbss.bpmn2stamp.converter.service.ConverterXmlFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterXmlFileReaderTest {

    private static final String TESTING_DATA_DIR = "src/test/resources/service/";

    public static final String BPMN_TEST_FILE = TESTING_DATA_DIR + "Jednani-sag.bpmn";
    public static final String ORG_STRUCTURE_TEST_FILE = TESTING_DATA_DIR + "ucl-zpracovani-informaci-o-bezpecnosti.xml";
    public static final String ACTOR_MAPPING_TEST_FILE = TESTING_DATA_DIR + "Jednani-sag-actor-mapping.xml";

    public ConverterXmlFileReader converterXmlFileReader;

    @Before
    public void init() {
        this.converterXmlFileReader = new ConverterXmlFileReader();
    }

    @Test
    public void testReadBpmn_whenFileExists_expectingNotNullResult() {
        TDefinitions tDefinitions = converterXmlFileReader.readBpmn(BPMN_TEST_FILE);
        assertThat(tDefinitions).isNotNull();
    }

    @Test
    public void testReadOrganizationStructure_whenFileExists_expectingNotNullResult() {
        Organization organization = converterXmlFileReader.readOrganizationStructure(ORG_STRUCTURE_TEST_FILE);
        assertThat(organization).isNotNull();
    }

    @Test
    public void testReadActorMappings_whenFileExists_expectingNotNullResult() {
        ActorMappings actorMappings = converterXmlFileReader.readActorMappings(ACTOR_MAPPING_TEST_FILE);
        assertThat(actorMappings).isNotNull();
    }

    @Test
    public void testReadActorMappingFile() {
        ConverterMappingService converterMappingService = new ConverterMappingService(
                "prefix-bpmn",
                "prefix-organization-structure",
                "prefix-pre-stamp"
        );
        ActorMappings actorsFromXml = converterMappingService.readActorMappingFile(new File(TESTING_DATA_DIR + "actor_mapping_1.xml"));
        ActorMappings actorsFromConf = converterMappingService.readActorMappingFile(new File(TESTING_DATA_DIR + "actor_mapping_1.conf"));
        assertThat(actorsFromXml).usingRecursiveComparison().isEqualTo(actorsFromConf);

        actorsFromXml = converterMappingService.readActorMappingFile(new File(TESTING_DATA_DIR + "actor_mapping_2.xml"));
        actorsFromConf = converterMappingService.readActorMappingFile(new File(TESTING_DATA_DIR + "actor_mapping_2.conf"));
        assertThat(actorsFromXml).usingRecursiveComparison().isEqualTo(actorsFromConf);
    }

}