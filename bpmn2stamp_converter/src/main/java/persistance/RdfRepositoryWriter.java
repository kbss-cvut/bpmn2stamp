package persistance;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import jopa.PersistenceHelper;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RdfRepositoryWriter {

    private EntityManagerFactory emf;
    private EntityManager em;

    public RdfRepositoryWriter(String storageFileLocation, String ontologyIRI, Set<String> imports) {
        init(storageFileLocation, ontologyIRI, imports);
    }

    //TODO update mapping file location
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

    public <T> void write(Collection<T> thingsToWrite) {
        if (thingsToWrite == null)
            return;
        EntityManager em = retrieveEntityManager();

        em.getTransaction().begin();
        thingsToWrite.stream().filter(Objects::nonNull).forEach(em::persist);
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
