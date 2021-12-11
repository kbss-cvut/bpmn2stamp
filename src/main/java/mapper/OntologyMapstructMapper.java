package mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.util.HashMap;
import java.util.Map;

abstract public class OntologyMapstructMapper<THING> extends SmartMapstructMapper {

    private final Map<String, THING> mappedObjectsById;
    private String targetIdBase;

    public OntologyMapstructMapper() {
        mappedObjectsById = new HashMap<>();
    }

    @AfterMapping
    public void afterAnyThingMapping(@MappingTarget THING anyResult) {
        mappedObjectsById.put(getId(anyResult), anyResult);
    }

    public Map<String, THING> getMappedObjectsById() {
        return mappedObjectsById;
    }

    protected abstract String getId(THING individual);

    protected abstract String processId(String id);

    public String getTargetIdBase() {
        return targetIdBase;
    }

    public void setTargetIdBase(String targetIdBase) {
        this.targetIdBase = targetIdBase;
    }

}
