package jopa;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.owlapi.config.OwlapiOntoDriverProperties;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PersistenceHelper {

    public static EntityManagerFactory initStorage(String storageFileLocation, String ontologyIRI, Set<String> imports, boolean readOnly) throws IOException {
        File file = new File(storageFileLocation);

        if (!readOnly) {
            createOrRewriteOntology(storageFileLocation, ontologyIRI, imports);
        }

        if (!file.isFile() || !file.exists()) {
            throw new IOException(String.format("File %s if not file or does not exist.", file));
        }

        // TODO check if all imports are present in mapping file, show warning if not

        Map<String, String> props = constructProps(storageFileLocation, ontologyIRI);

        return Persistence.createEntityManagerFactory(ontologyIRI, props);
    }

    private static Map<String, String> constructProps(String storageFileLocation, String ontologyIRI) {
        final Map<String, String> props = new HashMap<>();
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, new File(storageFileLocation).toURI().toString());
        props.put(JOPAPersistenceProperties.ONTOLOGY_URI_KEY, URI.create(ontologyIRI).toString());
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, "cz.cvut.kbss.ontodriver.owlapi.OwlapiDataSource");
        props.put(OwlapiOntoDriverProperties.MAPPING_FILE_LOCATION, "src/main/resources/jopa/mapping/mapping.map");
        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        props.put(OntoDriverProperties.REASONER_FACTORY_CLASS, OpenlletReasonerFactory.class.getName());
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, "model");
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        return props;

    }

    private static void createOrRewriteOntology(String fileLocation, String ontologyIRI, Set<String> imports) throws IOException {
        String iriInBrackets = String.format("<%s>", ontologyIRI);

        StringBuilder stringBuilder = new StringBuilder("{\n");
        stringBuilder.append(iriInBrackets).append(" a owl:Ontology .\n");
        for (String anImport : imports) {
            stringBuilder.append(iriInBrackets).append(" owl:imports <").append(anImport).append("> .\n");
        }
        stringBuilder.append("}\n");
        String fileContent = stringBuilder.toString();

        FileUtils.writeByteArrayToFile(new File(fileLocation), fileContent.getBytes(StandardCharsets.UTF_8));
    }

}
