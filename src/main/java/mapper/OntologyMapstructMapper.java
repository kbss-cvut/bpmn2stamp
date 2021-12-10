package mapper;

import model.bbo.model.Thing;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.util.HashMap;
import java.util.Map;

abstract public class OntologyMapstructMapper extends SmartMapstructMapper {

    private final Map<String, Thing> mappedObjectsById;
    private String targetIdBase;

    public OntologyMapstructMapper() {
        mappedObjectsById = new HashMap<>();
    }

    @AfterMapping
    public void afterAnyThingMapping(@MappingTarget Thing anyResult) {
        mappedObjectsById.put(anyResult.getId(), anyResult);
    }

    public Map<String, Thing> getMappedObjectsById() {
        return mappedObjectsById;
    }

    protected abstract String processId(String id);

    public String getTargetIdBase() {
        return targetIdBase;
    }

    public void setTargetIdBase(String targetIdBase) {
        this.targetIdBase = targetIdBase;
    }

}
