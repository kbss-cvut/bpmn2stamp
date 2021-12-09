package service;

import cz.cvut.kbss.jopa.model.EntityManager;
import jopa.PersistenceFactory;
import model.bbo.model.Thing;

import java.util.Collection;
import java.util.List;

public class RdfWriterService {

    private final EntityManager em;

    public RdfWriterService() {
        PersistenceFactory.init("c:/Users/bogdan.grigorian/Documents/Study/Diplomka/bpmn2stamp/src/main/resources/jopa/ontiky.owl");
        this.em = PersistenceFactory.createEntityManager();
    }

    public <T extends Thing> void save(T object) {
        save(List.of(object));
    }

    public <T extends Thing> void save(Collection<T> objects) {
        try {
            em.getTransaction().begin();
            clear();
            for (T object : objects) {
                em.persist(object);
            }
            em.getTransaction().commit();
        } finally {
            PersistenceFactory.close(); // Closing EMF closes all entity managers as well
        }
    }

    public void clear() {
        //TODO clear through working with temp file, save everything to it. After successful run, rename temp file (replacing new one)
        //TODO OR just clear output file before run (to start working with empty repository)
//        em.createNativeQuery("DELETE {" +
//                "?a ?b ?c ." +
//                "} WHERE {" +
//                "?a ?b ?c ." +
//                "}").executeUpdate();
    }
}
