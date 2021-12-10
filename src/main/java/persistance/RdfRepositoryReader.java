package persistance;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import jopa.PersistenceHelper;
import model.bbo.model.Thing;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collections;

public class RdfRepositoryReader {

    private EntityManagerFactory emf;
    private EntityManager em;

    public RdfRepositoryReader(String storageFileLocation, String ontologyIRI) {
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

    public <T extends Thing> T read(Class<T> cLass, String id) {
        EntityManager em = retrieveEntityManager();
        return em.getReference(cLass, id);
    }

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

}
