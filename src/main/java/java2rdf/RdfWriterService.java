package java2rdf;

import cz.cvut.kbss.jopa.model.EntityManager;
import model.rdf.model.Thing;

public class RdfWriterService {

    private final EntityManager em;

    public RdfWriterService() {
        PersistenceFactory.init("c:/Users/bogdan.grigorian/Documents/Study/Diplomka/bpmn2stamp/src/main/resources/jopa/ontiky.owl");
        this.em = PersistenceFactory.createEntityManager();
    }

    public <T extends Thing> void save(T object) {
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } finally {
            PersistenceFactory.close(); // Closing EMF closes all entity managers as well
        }
    }
}
