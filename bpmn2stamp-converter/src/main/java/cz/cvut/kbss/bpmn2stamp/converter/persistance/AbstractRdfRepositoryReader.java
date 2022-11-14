package cz.cvut.kbss.bpmn2stamp.converter.persistance;

import com.google.common.io.Resources;
import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.Vocabulary;
import cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.bpmn2stamp.converter.jopa.PersistenceHelper;
import cz.cvut.kbss.bpmn2stamp.converter.utils.ConverterMappingUtils;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRdfRepositoryReader<THING> {

    private EntityManagerFactory emf;
    private EntityManager em;

    public AbstractRdfRepositoryReader(String storageFileLocation, String ontologyIRI) {
        init(storageFileLocation, ontologyIRI);
    }

    public void init(String storageFileLocation, String ontologyIRI) {
        try {
            if (this.emf != null && !this.emf.isOpen()) {
                this.emf.close();
            }
            this.emf = PersistenceHelper.initStorage(storageFileLocation, ontologyIRI, Collections.emptySet(), Collections.emptyMap(), true);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not initialize reader for file %s, with uri %s",
                    storageFileLocation, ontologyIRI), e);
        }
    }

    public <T extends THING> T read(Class<T> cLass, String id) {
        EntityManager em = retrieveEntityManager();
        return em.find(cLass, id);
    }

    public List<Thing> readAll() {
        EntityManager em = retrieveEntityManager();
        List<Thing> result = new ArrayList<>();
        Map<Class<? extends Thing>, List<String>> results = new HashMap<>();

        //em.unwrap(OWLOntology.class).getOWLOntologyManager().loadOntology(IRI.create("http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-organization-structure"));

        for (Class<? extends Thing> bboClass : ApplicationConstants.BBO_CLASSES) {
            String classIRI = ConverterMappingUtils.getClassIRI(bboClass);
            if (classIRI != null) {
                List<URI> classIRI1 = em.createNativeQuery("SELECT ?a\n" +
                                "WHERE { ?a a ?classIRI . }", URI.class)
                        .setParameter("classIRI", URI.create(classIRI))
                        .getResultList();
                results.put(bboClass, classIRI1.stream().map(URI::toString).collect(Collectors.toList()));
            }
        }
        for (Map.Entry<Class<? extends Thing>, List<String>> classListEntry : results.entrySet()) {
            Class<? extends Thing> key = classListEntry.getKey();
            for (String s : classListEntry.getValue()) {
                Thing thing = em.find(key, s);
                result.add(thing);
            }
        }
        return result;
    }

    /**
     * Returns usable classes in ontology.
     * Used e.g. for retrieving all individuals for ontology.
     */
    protected abstract List<Class<? extends THING>> getOntologyClasses();

    private EntityManager retrieveEntityManager() {
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
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
