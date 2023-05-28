package cz.cvut.kbss.bpmn2stamp.other;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import openllet.owlapi.OpenlletReasonerFactory;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.rdf.turtle.renderer.TurtleStorerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class OntologyPersistSpecificCaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyPersistSpecificCaseTest.class);

    private static final String TEST_ONTOLOGY_URI = "https://test-ontology.cz";
    private EntityManagerFactory entityManagerFactory;
    private File testOntologyFile;

    @Before
    public void init() throws IOException {
        Map<String, String> props = constructProps();
        entityManagerFactory = Persistence.createEntityManagerFactory(TEST_ONTOLOGY_URI, props);
    }

    /**
     * This test asserts that string label containing colon symbol is stored correctly, i.e. as a string type.
     */
    @Test
    public void testPersist_withColonInLabel_shouldContainCorrectLabelValue() throws IOException {
        Thing thing = new Thing();
        String labelValue = "test:label";
        thing.setName(labelValue);

        persist(thing);

        String resultOntologyFileContent = java.nio.file.Files.readString(testOntologyFile.toPath());
        LOG.info("Testing ontology updated content: \n{}", resultOntologyFileContent);

        // the result should contain label value as string
        Assertions.assertThat(resultOntologyFileContent).contains("rdfs:label \"" + labelValue + "\"");
        // the result should not contain label value as iri
        Assertions.assertThat(resultOntologyFileContent).doesNotContain("rdfs:label <" + labelValue + ">");
    }

    private void persist(Thing t) {
        LOG.info("Persisting the object: {}", t);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(t);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }

    private Map<String, String> constructProps() throws IOException {
        testOntologyFile = createPhysicalOntology(TEST_ONTOLOGY_URI);

        final Map<String, String> props = new HashMap<>();
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, testOntologyFile.toURI().toString());
        props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, URI.create(TEST_ONTOLOGY_URI).toString());
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, "cz.cvut.kbss.ontodriver.owlapi.OwlapiDataSource");
        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OpenlletReasonerFactory.class.getName());
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.bpmn2stamp.converter.model");
        props.put(JOPAPersistenceProperties.CACHE_ENABLED, Boolean.TRUE.toString());
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        return props;
    }

    private static File createPhysicalOntology(String ontologyIRI) throws IOException {
        File file = Files.newTemporaryFile();
        LOG.info("Created temporary file for the testing ontology: {}", file.getAbsolutePath());

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        TurtleStorerFactory turtleStorerFactory = new TurtleStorerFactory();
        manager.getOntologyStorers().add(turtleStorerFactory);

        org.semanticweb.owlapi.model.IRI ontologyIRIs = org.semanticweb.owlapi.model.IRI.create(ontologyIRI);
        OWLOntology ontology;
        try {
            ontology = manager.createOntology(ontologyIRIs);
        } catch (OWLOntologyCreationException e) {
            throw new RuntimeException(e);
        }

        DefaultPrefixManager pm = new DefaultPrefixManager();
        pm.setDefaultPrefix(ontologyIRI + "#");
        pm.setPrefix("owl:", "http://www.w3.org/2002/07/owl#");
        pm.setPrefix("rdf:", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");

        TurtleDocumentFormat turtleFormat = new TurtleDocumentFormat();
        turtleFormat.copyPrefixesFrom(pm);
        turtleFormat.setDefaultPrefix(ontologyIRI + "/");

        try (OutputStream outputStream = java.nio.file.Files.newOutputStream(file.toPath())) {
            manager.saveOntology(ontology, turtleFormat, outputStream);
        } catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }

        String testOntologyContent = java.nio.file.Files.readString(file.toPath());
        LOG.info("Testing ontology was created with the following content: \n{}", testOntologyContent);

        return file;
    }
}
