package persistance;

import common.ApplicationConstants;
import model.bbo.model.Thing;

import java.util.List;

public class BboRdfRepositoryReader extends AbstractRdfRepositoryReader<Thing> {

    public BboRdfRepositoryReader(String storageFileLocation, String ontologyIRI) {
        super(storageFileLocation, ontologyIRI);
    }

    @Override
    protected List<Class<? extends Thing>> getOntologyClasses() {
        return ApplicationConstants.BBO_CLASSES;
    }

}
