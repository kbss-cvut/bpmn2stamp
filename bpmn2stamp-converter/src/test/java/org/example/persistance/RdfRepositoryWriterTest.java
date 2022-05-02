package org.example.persistance;

import org.example.model.bbo.model.Process;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Test for {@link RdfRepositoryWriter}
 */
public class RdfRepositoryWriterTest {

    private static final String TEST_STORAGE = "./test.ttl";
    private static final String TEST_ONTOLOGY_IRI = "http://bpmn2stamp.cz/test";
    private static final Set<String> TEST_IMPORTS = Set.of(
            "http://BPMNbasedOntology"
    );

    @Test
    public void initTest() {
        //TODO storage initiated
        //TODO entity manager factory is created
    }

    @Test
    public void write_shouldCallPersistInTransactionTest() {
        RdfRepositoryWriter writer = new RdfRepositoryWriter(TEST_STORAGE, TEST_ONTOLOGY_IRI, TEST_IMPORTS);
        // TODO check if transaction started
        // TODO check if #persist was called for every item
        // TODO check if transaction finished

//        Process testProcess = new Process();
//        writer.write(Arrays.asList(testProcess));
//
//        Mockito.verify(writer.getEm(), Mockito.times(1)).persist(testProcess);
    }

    @Test
    public void addMappingTest() {
        //TODO check if imports are successful in OWL api
    }
    
}