package DEL;

import cz.cvut.kbss.jopa.model.EntityManager;
import model.bbo.model.Thing;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Deprecated
public class RdfWriter {

    private EntityManager entityManager;
    private final String ontologyLocation;
    private String ontologyIRI;
    private String ontologyVersion;
    private Set<String> imports;

    public RdfWriter(String ontologyLocation) {
        this.ontologyLocation = ontologyLocation;
    }

    public RdfWriter(String ontologyLocation, String ontologyIRI) {
        this.ontologyLocation = ontologyLocation;
        this.ontologyIRI = ontologyIRI;
    }

    public RdfWriter(String ontologyLocation, String ontologyIRI, String ontologyVersion) {
        this.ontologyLocation = ontologyLocation;
        this.ontologyIRI = ontologyIRI;
        this.ontologyVersion = ontologyVersion;
    }

    public void init() {
        PersistenceFactory.init(ontologyLocation, ontologyIRI);

        this.entityManager = PersistenceFactory.createEntityManager();

        /*
        Insert to empty .owl file to provide entity id and import bbo

{
  <http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/> a owl:Ontology;
    owl:imports <http://BPMNbasedOntology> .
}

         */
//        setOntologyId(this.entityManager, ontologyIRI, ontologyVersion);
//        importOntology(this.entityManager, imports);
    }

    public <T extends Thing> void save(T object) {
        save(List.of(object));
    }

    public <T extends Thing> void save(Collection<T> objects) {
        OWLOntology unwrap = entityManager.unwrap(OWLOntology.class);

//            setOntologyId("http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/", "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/0.0.1/");
//            setOntologyId("http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/", "http://onto.fel.cvut.cz/ontologies/ucl/example/jednani-sag-bpmn/0.0.1/");
//            importOntology("http://BPMNbasedOntology");

        entityManager.getTransaction().begin();
        clear();
        for (T object : objects) {
            entityManager.persist(object);
        }
        entityManager.getTransaction().commit();
    }

    private void importOntology(EntityManager entityManager, Collection<String> ontologyIds) {
        if (ontologyIds == null) {
            return;
        }
        OWLOntology unwrap = entityManager.unwrap(OWLOntology.class);
        OWLOntologyManager m = unwrap.getOWLOntologyManager();

        for (String ontologyId : ontologyIds) {
            IRI iri = IRI.create(ontologyId);
            OWLImportsDeclaration importDeclaration = m.getOWLDataFactory().getOWLImportsDeclaration(iri);
            try {
                m.loadOntology(iri);
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
            m.applyChange(new AddImport(unwrap, importDeclaration));
        }
    }

    private void setOntologyId(EntityManager entityManager, String ontologyId, String version) {
        if (ontologyId == null) {
            return;
        }
        OWLOntology unwrap = entityManager.unwrap(OWLOntology.class);
        OWLOntologyManager m = unwrap.getOWLOntologyManager();

        OWLOntologyID owlOntologyID = new OWLOntologyID(
                Optional.of(ontologyId).map(IRI::create),
                Optional.ofNullable(version).map(IRI::create)
        );

        m.applyChange(new SetOntologyID(unwrap, owlOntologyID));
    }


    public <T extends Thing> T read(Class<T> cLass, String id) {
        return entityManager.getReference(cLass, id);
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

    public void close() {
        PersistenceFactory.close();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public String getOntologyIRI() {
        return ontologyIRI;
    }

    public void setOntologyIRI(String ontologyIRI) {
        this.ontologyIRI = ontologyIRI;
    }

    public String getOntologyVersion() {
        return ontologyVersion;
    }

    public void setOntologyVersion(String ontologyVersion) {
        this.ontologyVersion = ontologyVersion;
    }

    public String getOntologyLocation() {
        return ontologyLocation;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }
}
