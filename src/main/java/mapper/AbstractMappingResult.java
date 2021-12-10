package mapper;

import java.util.Map;

public abstract class AbstractMappingResult<BASE_ONTOLOGY_CLASS> {

    private final Map<String, BASE_ONTOLOGY_CLASS> mappedObjects;

    public AbstractMappingResult(Map<String, BASE_ONTOLOGY_CLASS> mappedObjectsById) {
        this.mappedObjects = mappedObjectsById;
    }

    public Map<String, BASE_ONTOLOGY_CLASS> getMappedObjects() {
        return mappedObjects;
    }
}
