package persistance;

import common.ApplicationConstants;
import model.stamp.model.Thing;

import java.util.List;

public class StampRdfRepositoryReader extends AbstractRdfRepositoryReader<Thing> {

    public StampRdfRepositoryReader(String storageFileLocation, String ontologyIRI) {
        super(storageFileLocation, ontologyIRI);
    }

    @Override
    protected List<Class<? extends Thing>> getOntologyClasses() {
        return ApplicationConstants.STAMP_CLASSES;
    }

}
