package DEL;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.owlapi.config.OwlapiOntoDriverProperties;
import openllet.owlapi.OpenlletReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class PersistenceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceFactory.class);

    private static final String REPOSITORY_FILE_NAME = "repository.owl";
    private static final String FILE_SCHEMA = "file://";

    private static boolean initialized = false;

    private static EntityManagerFactory emf;

    private PersistenceFactory() {
        throw new AssertionError();
    }

    public static void init(
            String ontologyFile,
            String ontologyIRI
    ) {
        final Map<String, String> props = new HashMap<>();
        // Here we set up basic storage access properties - driver class, physical location of the storage
//        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, setupRepository(ontologyFile));
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, new File(ontologyFile).toURI().toString());
        if (ontologyIRI != null) {
            props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, URI.create(ontologyIRI).toString());
        }
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, "cz.cvut.kbss.ontodriver.owlapi.OwlapiDataSource");
        // View transactional changes during transaction
        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        // Mapping file location to resolve external ontology IRIs
        props.put(OwlapiOntoDriverProperties.MAPPING_FILE_LOCATION, "src/main/resources/jopa/mapping/mapping.map");
        // Reasoner for persistence
//        props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, RDFSRuleReasonerFactory.class.getName());
        props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OpenlletReasonerFactory.class.getName());
        // Ontology language
//        props.put(JOPAPersistenceProperties.LANG, "en");
        // Where to look for entity classes
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "model.bbo.model");
        // Persistence provider name
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());

        emf = Persistence.createEntityManagerFactory("jopaBpmn2Bbo", props);
        initialized = true;
    }

    private static String setupRepository(String ontologyDir) {
        LOG.debug("Setting up repository...");
//        final String ontologyFileAbsolute = resolveAbsolutePath(ontologyFile);
//        final String repoFolder = ontologyFileAbsolute.substring(0,
//                ontologyFileAbsolute.lastIndexOf(File.separatorChar));
        final File repoFile = new File(ontologyDir + File.separator + REPOSITORY_FILE_NAME);
        if (repoFile.exists()) {
            LOG.debug("Repository already exists. Removing it...");
            final boolean res = repoFile.delete();
            assert res;
        }
//        try {
//            LOG.debug("Copying ontology to the repository...");
//            Files.copy(new File(ontologyDir).toPath(), repoFile.toPath());
//        } catch (IOException e) {
//            LOG.error("Unable to copy ontology into the repository", e);
//            System.exit(1);
//        }
        return URI.create(FILE_SCHEMA + repoFile.getAbsolutePath()).toString();
    }

    private static String resolveAbsolutePath(String ontologyFile) {
        final File file = new File(ontologyFile);
        assert file.exists();
        return file.getAbsolutePath();
    }

    public static EntityManager createEntityManager() {
        if (!initialized) {
            throw new IllegalStateException("java2rdf.PersistenceFactory has not been initialized.");
        }
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}