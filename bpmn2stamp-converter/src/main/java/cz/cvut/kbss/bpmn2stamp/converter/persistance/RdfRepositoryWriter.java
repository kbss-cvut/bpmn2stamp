package cz.cvut.kbss.bpmn2stamp.converter.persistance;

import com.google.common.io.Resources;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.bpmn2stamp.converter.jopa.PersistenceHelper;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

public class RdfRepositoryWriter {

    private EntityManagerFactory emf;
    private EntityManager em;
    private String storageFileLocation;
    
    public RdfRepositoryWriter(String storageFileLocation, String ontologyIRI, Set<String> imports, Map<String, File> additionalImports) {
        init(storageFileLocation, ontologyIRI, imports, additionalImports);
    }
    
    public RdfRepositoryWriter(String storageFileLocation, String ontologyIRI, Set<String> imports) {
        this(storageFileLocation, ontologyIRI, imports, Map.of());
    }

    //TODO update mapping file location
    public void init(String storageFileLocation, String ontologyIRI, Set<String> imports, Map<String, File> additionalImports) {
        try {
            if (this.emf != null && !this.emf.isOpen()) {
                this.emf.close();
            }
            this.emf = PersistenceHelper.initStorage(storageFileLocation, ontologyIRI, imports, additionalImports, false);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not initialize writer for file %s, with uri %s and imports %s",
                    storageFileLocation, ontologyIRI, imports), e);
        }
    }

    public <T> void write(Iterable<T> thingsToWrite) {
        if (thingsToWrite == null)
            return;
        System.out.println(emf.getProperties().get(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY));
        System.out.println(emf.getProperties().get(JOPAPersistenceProperties.ONTOLOGY_URI_KEY));

        EntityManager em = retrieveEntityManager();
        em.getTransaction().begin();
        StreamSupport.stream(thingsToWrite.spliterator(), false)
                .filter(Objects::nonNull)
                .forEach(em::persist);
        em.getTransaction().commit();
    }

    private EntityManager retrieveEntityManager() {
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
//            initMappings(em);
        }
        return em;
    }

    private void initMappings(EntityManager em) {
        addMapping(em, Vocabulary.ONTOLOGY_IRI_stamp, new File(Resources.getResource("jopa/ontology/stamp/stamp.ttl").getFile()));
        addMapping(em, Vocabulary.ONTOLOGY_IRI_stamp_constraints, new File(Resources.getResource("jopa/ontology/stamp/stamp-constraints.ttl").getFile()));
        addMapping(em, Vocabulary.ONTOLOGY_IRI_stamp_control_structure, new File(Resources.getResource("jopa/ontology/stamp/stamp-control-structure.ttl").getFile()));
        addMapping(em, "http://onto.fel.cvut.cz/ontologies/stamp-hazard-and-risk", new File(Resources.getResource("jopa/ontology/stamp/stamp-hazard-and-risk.ttl").getFile()));
        addMapping(em, Vocabulary.ONTOLOGY_IRI_stamp_hazard_profile, new File(Resources.getResource("jopa/ontology/stamp/stamp-hazard-profile.ttl").getFile()));
        addMapping(em, cz.cvut.kbss.bpmn2stamp.converter.model.bbo.Vocabulary.ONTOLOGY_IRI_BPMNbasedOntology, new File(Resources.getResource("jopa/ontology/bbo/BPMNbasedOntologyV1_2.owl").getFile()));
    }

    public void addMapping(EntityManager em, String ontologyIRI, File document) {
        em.unwrap(OWLOntology.class).getOWLOntologyManager().getIRIMappers().add(
                new SimpleIRIMapper(IRI.create(ontologyIRI), IRI.create(document)));
    }

    @PreDestroy
    public void close() {
        emf.close();
    }

    public EntityManager getEm() {
        return em;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

}
