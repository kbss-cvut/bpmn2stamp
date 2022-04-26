package service;

import org.example.model.actor.ActorMappings;
import org.example.model.bpmn.org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.example.model.organization.Organization;
import org.example.service.ConverterXmlFileReader;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterXmlFileReaderTest {

    public static final String BPMN_TEST_FILE = "service/data/Jednani-sag.bpmn";
    public static final String ORG_STRUCTURE_TEST_FILE = "service/data/ucl-zpracovani-informaci-o-bezpecnosti.xml";
    public static final String ACTOR_MAPPING_TEST_FILE = "service/data/Jednani-sag-actor-mapping.xml";

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
}