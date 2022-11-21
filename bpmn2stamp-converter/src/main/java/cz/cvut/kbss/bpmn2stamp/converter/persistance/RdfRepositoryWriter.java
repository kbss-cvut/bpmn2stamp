package cz.cvut.kbss.bpmn2stamp.converter.persistance;

import com.google.common.io.Resources;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.CardinalityConstraintViolatedException;
import cz.cvut.kbss.jopa.exceptions.RollbackException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.bpmn2stamp.converter.jopa.PersistenceHelper;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

public class RdfRepositoryWriter {
    
    private static final Logger LOG = LoggerFactory.getLogger(RdfRepositoryWriter.class.getSimpleName());

    private EntityManagerFactory emf;
    private EntityManager em;
    
    private String storageFileLocation;
    private String ontologyIRI;
    private Set<String> imports;
    private Map<String, File> additionalImports;
    
    public RdfRepositoryWriter(String storageFileLocation, String ontologyIRI, Set<String> imports, Map<String, File> additionalImports) {
        this.storageFileLocation = storageFileLocation;
        this.ontologyIRI = ontologyIRI;
        this.imports = imports;
        this.additionalImports = additionalImports;
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
        LOG.debug("Writing into file {} with iri {}", storageFileLocation, ontologyIRI);
        LOG.debug("Using imports: {}", imports);
        LOG.debug("Using additional imports: {}", additionalImports);
        
        if (thingsToWrite == null)
            return;
        LOG.debug(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY);
        LOG.debug(JOPAPersistenceProperties.ONTOLOGY_URI_KEY);

        EntityManager em = retrieveEntityManager();
        em.getTransaction().begin();

        Optional<Integer> persistedItemsCount = StreamSupport.stream(thingsToWrite.spliterator(), false)
                .filter(Objects::nonNull)
                .map(this::persist)
                .reduce(Integer::sum);

        try {
            em.getTransaction().commit();
            LOG.debug("Items ({}) were recorded successfully.", persistedItemsCount.orElse(0));
        } catch (RollbackException re) {
            LOG.error("Could not save ontology, rollback...");
            
            if (re.getCause() == null) {
                LOG.error("Unknown cause.");
                return;
            }
            
            if (re.getCause() instanceof CardinalityConstraintViolatedException) {
                LOG.error("Constraints violation error: " + re.getCause().getMessage());
            } else {
                LOG.error("Unknown error.", re);
            }
            throw re;
        } finally {
            em.close();
        }
    }
    
    private <T> int persist(T itemToPersist) {
        try {
            em.persist(itemToPersist);
        } catch (Exception e) {
            LOG.error("Could not persist item {}.", itemToPersist, e);
            return 0;
        }
        return 1;
    }

    private EntityManager retrieveEntityManager() {
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
//            initMappings(em);
        }
        return em;
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
