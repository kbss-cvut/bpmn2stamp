package persistance;

import common.ApplicationConstants;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import jopa.PersistenceHelper;
import model.bbo.model.Thing;
import utils.MappingUtils;

import javax.annotation.PreDestroy;
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
            this.emf = PersistenceHelper.initStorage(storageFileLocation, ontologyIRI, Collections.emptySet(), true);
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
        Map<Class<? extends model.bbo.model.Thing>, List<String>> results = new HashMap<>();

        for (Class<? extends model.bbo.model.Thing> bboClass : ApplicationConstants.BBO_CLASSES) {
            String classIRI = MappingUtils.getClassIRI(bboClass);
            if (classIRI != null) {
                List<URI> classIRI1 = em.createNativeQuery("SELECT ?a\n" +
                                "WHERE { ?a a ?classIRI . }", URI.class)
                        .setParameter("classIRI", URI.create(classIRI))
                        .getResultList();
                results.put(bboClass, classIRI1.stream().map(URI::toString).collect(Collectors.toList()));
            }
        }
        for (Map.Entry<Class<? extends Thing>, List<String>> classListEntry : results.entrySet()) {
            Class<? extends model.bbo.model.Thing> key = classListEntry.getKey();
            for (String s : classListEntry.getValue()) {
                Thing thing = em.find(key, s);
                result.add(thing);
            }
        }
//        Set<EntityType<?>> entities = em.getMetamodel().getEntities();
//        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
//        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
//        System.out.println("-------------------------");
//        for (OWLClass c : ontology.getClassesInSignature(Imports.INCLUDED)) {
//            NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
//            for (OWLNamedIndividual i : instances.getFlattened()) {
//                System.out.println(i.getIRI().getShortForm());
//            }
//        }
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
