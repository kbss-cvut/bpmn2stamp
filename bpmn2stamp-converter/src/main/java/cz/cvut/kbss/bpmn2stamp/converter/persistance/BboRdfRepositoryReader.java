package cz.cvut.kbss.bpmn2stamp.converter.persistance;

import cz.cvut.kbss.bpmn2stamp.converter.model.bbo.model.Thing;
import cz.cvut.kbss.bpmn2stamp.converter.common.ApplicationConstants;

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
