package cz.cvut.kbss.bpmn2stamp.converter.jopa;

import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.owlapi.config.OwlapiOntoDriverProperties;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PersistenceHelper {
    
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceHelper.class.getSimpleName());

    public static EntityManagerFactory initStorage(String storageFileLocation, String ontologyIRI, Set<String> imports, Map<String, File> additionalImports, boolean readOnly) throws IOException {
        File file = new File(storageFileLocation);

        if (!readOnly) {
            createOrRewriteOntology(storageFileLocation, ontologyIRI, imports);
        }

        if (!file.isFile() || !file.exists()) {
            throw new IOException(String.format("File %s is not a file or does not exist.", file));
        }

        Map<String, String> props = constructProps(storageFileLocation, ontologyIRI, additionalImports);

        LOG.debug(storageFileLocation);
        LOG.debug(ontologyIRI);
        return Persistence.createEntityManagerFactory(ontologyIRI, props);
    }

    private static Map<String, String> constructProps(String storageFileLocation, String ontologyIRI, Map<String, File> additionalImports) throws IOException {
        final Map<String, String> props = new HashMap<>();
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, new File(storageFileLocation).toURI().toString());
        props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, URI.create(ontologyIRI).toString());
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, URI.create(ontologyIRI).toString());
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, "cz.cvut.kbss.ontodriver.owlapi.OwlapiDataSource");
//        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, "cz.cvut.kbss.ontodriver.sesame.SesameDataSource");

        StringBuilder mappingStr = new StringBuilder();
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp, "jopa/ontology/stamp/stamp.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp + "/0.0.1", "jopa/ontology/stamp/stamp.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp_constraints, "jopa/ontology/stamp/stamp-constraints.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp_constraints + "/0.0.1", "jopa/ontology/stamp/stamp-constraints.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp_control_structure, "jopa/ontology/stamp/stamp-control-structure.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp_control_structure + "/0.0.1", "jopa/ontology/stamp/stamp-control-structure.ttl");
        addMapping(mappingStr, "http://onto.fel.cvut.cz/ontologies/stamp-hazard-and-risk", "jopa/ontology/stamp/stamp-hazard-and-risk.ttl");
        addMapping(mappingStr, "http://onto.fel.cvut.cz/ontologies/stamp-hazard-and-risk" + "/0.0.1", "jopa/ontology/stamp/stamp-hazard-and-risk.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp_hazard_profile, "jopa/ontology/stamp/stamp-hazard-profile.ttl");
        addMapping(mappingStr, Vocabulary.ONTOLOGY_IRI_stamp_hazard_profile + "/0.0.1", "jopa/ontology/stamp/stamp-hazard-profile.ttl");
        addMapping(mappingStr, cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary.ONTOLOGY_IRI_BPMNbasedOntology, "jopa/ontology/bbo/BPMNbasedOntologyV1_2.owl");
        addMapping(mappingStr, "http://purl.obolibrary.org/obo/uo.owl", "jopa/ontology/bbo/uo.owl");
        additionalImports.forEach((e, v) -> addMappingTemp(mappingStr, e, v));

        try {
            File mappingTemp = File.createTempFile("mapping-tmp-", ".map");
            LOG.debug(mappingTemp.getAbsolutePath());
            Files.writeString(mappingTemp.toPath(), mappingStr);
            props.put(OwlapiOntoDriverProperties.MAPPING_FILE_LOCATION, mappingTemp.getAbsolutePath());
        } catch (IOException e) {
            LOG.error("There was an unexpected error during ontology initialization.", e);
        }

        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OpenlletReasonerFactory.class.getName());
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.bpmn2stamp.converter.model");
        props.put(JOPAPersistenceProperties.CACHE_ENABLED, Boolean.TRUE.toString());
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        return props;
    }

    private static StringBuilder addMapping(StringBuilder mappingStr, String importIri, String importFile) throws IOException {
        String absPath = copyToTemp(importFile).getAbsolutePath();
        mappingStr.append(importIri).append(" > ").append(absPath).append("\n");
        return mappingStr;
    }

    private static StringBuilder addMappingTemp(StringBuilder mappingStr, String importIri, File tempImportFile) {
        mappingStr.append(importIri).append(" > ").append(tempImportFile.getAbsolutePath()).append("\n");
        return mappingStr;
    }
    
    private static File copyToTemp(String systemResourcePath) throws IOException {
        try {
            String extension = FileNameUtils.getExtension(systemResourcePath);
            File tempFile = File.createTempFile("jopa_", "." + extension);
            InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(systemResourcePath);
            FileUtils.copyInputStreamToFile(systemResourceAsStream, tempFile);
            if (systemResourceAsStream != null)
                systemResourceAsStream.close();
            return tempFile;
        } catch (IOException e) {
            LOG.error("There was an unexpected error during creating TEMP file.", e);
            throw e;
        }
    }

    private static void createOrRewriteOntology(String fileLocation, String ontologyIRI, Set<String> imports) throws IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        TurtleStorerFactory turtleStorerFactory = new TurtleStorerFactory();
        manager.getOntologyStorers().add(turtleStorerFactory);

        // Create ontology with two imports
        IRI ontologyIRIs = IRI.create(ontologyIRI);
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
        //TODO add prefixed to configuration, update setters usage
        pm.setPrefix("bbo:", cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary.ONTOLOGY_IRI_BPMNbasedOntology + "#");
        pm.setPrefix("stamp:", Vocabulary.ONTOLOGY_IRI_stamp + "/");

        for (String anImport : imports) {
            IRI provIri = IRI.create(anImport);
            OWLImportsDeclaration provImport = factory.getOWLImportsDeclaration(provIri);
            manager.applyChange(new AddImport(ontology, provImport));
        }

        File testFile = new File(fileLocation);

        TurtleDocumentFormat turtleFormat = new TurtleDocumentFormat();
        turtleFormat.copyPrefixesFrom(pm);
        turtleFormat.setDefaultPrefix(ontologyIRI + "/");

        try (OutputStream outputStream = Files.newOutputStream(testFile.toPath())) {
            manager.saveOntology(ontology, turtleFormat, outputStream);
        } catch (OWLOntologyStorageException e) {
            throw new RuntimeException(e);
        }
    }

}
