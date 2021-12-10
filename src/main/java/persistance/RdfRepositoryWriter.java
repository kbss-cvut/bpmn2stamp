package persistance;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import jopa.PersistenceHelper;
import model.bbo.model.Thing;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class RdfRepositoryWriter {

    private EntityManagerFactory emf;
    private EntityManager em;

    public RdfRepositoryWriter(String storageFileLocation, String ontologyIRI, Set<String> imports) {
        init(storageFileLocation, ontologyIRI, imports);
    }

    public void init(String storageFileLocation, String ontologyIRI, Set<String> imports) {
        try {
            if (this.emf != null && !this.emf.isOpen()) {
                this.emf.close();
            }
            this.emf = PersistenceHelper.initStorage(storageFileLocation, ontologyIRI, imports, false);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not initialize writer for file %s, with uri %s and imports %s",
                    storageFileLocation, ontologyIRI, imports), e);
        }
    }

    public void write(Collection<Thing> thingsToWrite) {
        EntityManager em = retrieveEntityManager();

        em.getTransaction().begin();
        thingsToWrite.forEach(em::persist);
        em.getTransaction().commit();
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

    public EntityManager getEm() {
        return em;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

}
