package cz.cvut.kbss.bpmn2stamp.converter.persistance;

import cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants;
import cz.cvut.kbss.bpmn2stamp.converter.model.stamp.model.Thing;

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
